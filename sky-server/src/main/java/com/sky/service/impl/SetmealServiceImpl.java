package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private DishMapper dishMapper;
    /**
     * 新增套餐
     * @param setmealDTO
     * @return void
     * @author paxi
     * @data 2023/8/31
     **/
    @Override
    @Transactional
    public void saveWithDish(SetmealDTO setmealDTO) {
        // DTO中包含有套餐基本信息以及套餐和菜品的关联关系，需要操作两张表套餐表和套餐菜品表
        // 1. 拿到套餐基本信息插入套餐表
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        // 此时并未获得菜品ID
        setmealMapper.save(setmeal);
        // 获取新增套餐的ID
        Long setmealId = setmeal.getId();
        // 套餐菜品对应关系列表
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> {
            // 遍历菜品套餐表，设置套餐ID
            setmealDish.setSetmealId(setmealId);
        });
        // 2. 拿到套餐和菜品对应表插入套餐菜品表
        setmealDishMapper.saveWithDish(setmealDishes);
    }

    /**
     * 套餐分页查询
     *
     * @param setmealPageQueryDTO
     * @return com.sky.result.PageResult
     * @author paxi
     * @data 2023/8/31
     **/
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 根据ID查询套餐
     * @param id
     * @return com.sky.vo.SetmealVO
     * @author paxi
     * @data 2023/9/2
     **/
    @Override
    public SetmealVO getByIdWithDish(Long id) {
        // 1. 查询菜品信息
        Setmeal setmeal = setmealMapper.getById(id);
        // 2. 根据套餐ID查询对应的菜品信息
        List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(id);

        SetmealVO setmealVO = new SetmealVO();
        // 属性拷贝
        BeanUtils.copyProperties(setmeal,setmealVO);
        // 设置套餐的菜品信息
        setmealVO.setSetmealDishes(setmealDishes);

        return  setmealVO;
    }

    /**
     * 修改套餐信息
     * @param setmealDTO
     * @return void
     * @author paxi
     * @data 2023/9/3
     **/
    @Override
    @Transactional
    public void update(SetmealDTO setmealDTO) {
        // 1. 更新套餐基本信息
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.update(setmeal);
        // 记录套餐ID
        Long setmealId = setmealDTO.getId();
        // 获取套餐对应菜品信息
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        // 2. 先删除原有菜品信息，再重新新增菜品信息
        setmealDishMapper.deleteBySetmealId(setmealId);
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealId);
        });
        // 重新插入套餐对应菜品信息
        setmealDishMapper.saveWithDish(setmealDishes);
    }

    /**
     * 起售和禁售套餐
     * @param status
     * @param id
     * @return void
     * @author paxi
     * @data 2023/9/3
     **/
    @Override
    public void startAndStop(Integer status, Long id) {
        // 如果是起售套餐，则需要首先判断套餐内是否包含已经停止售卖的菜品，若有，则无法起售套餐
        if (Objects.equals(status, StatusConstant.ENABLE)) {
            // 根据套餐菜品对应关系表中的菜品ID去查询菜品并判断每个菜品的状态，即涉及setmeal_dish和dish两张表的查询
            List<Dish> dishList = dishMapper.getBySetmealId(id);
            dishList.forEach(dish -> {
                // 遍历套餐对应菜品
                if (Objects.equals(dish.getStatus(), StatusConstant.DISABLE))
                    throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
            });
        }
        // 更新套餐信息
        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        setmealMapper.update(setmeal);
    }

    /**
     * 根据ID批量删除套餐
     * @param ids
     * @return void
     * @author paxi
     * @data 2023/9/3
     **/
    @Override
    public void deleteByIds(List<Long> ids) {
        ids.forEach(setmealId -> {
            Setmeal setmeal = setmealMapper.getById(setmealId);
            // 起售中的套餐无法删除
            if (StatusConstant.ENABLE.equals(setmeal.getStatus()))
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            else {
                // 删除套餐信息
                setmealMapper.deleteById(setmealId);
                // 删除套菜对应菜品信息
                setmealDishMapper.deleteBySetmealId(setmealId);
            }
        });
    }

    /**
     * 小程序端条件查询套餐
     *
     * @param setmeal
     * @return java.util.List<com.sky.entity.Setmeal>
     * @author paxi
     * @data 2023/9/4
     **/
    @Override
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    /**
     * 小程序端根据分类id查询套餐
     *
     * @param id
     * @return java.util.List<com.sky.vo.DishItemVO>
     * @author paxi
     * @data 2023/9/4
     **/
    @Override
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }
}
