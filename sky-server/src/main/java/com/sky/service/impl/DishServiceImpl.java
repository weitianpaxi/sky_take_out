package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 新增菜品和对应的口味
     * @param dishDTO
     * @return void
     * @author paxi
     * @data 2023/8/27
     **/
    @Override
    // 因为涉及两张表的操作，开启Spring管理事务，保证操作的原子性
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        // 向菜品表插入单条菜品数据
        dishMapper.insert(dish);
        // 获取菜品ID，这是刚才insert语句插入最新数据的菜品主键ID
        Long dishId = dish.getId();

        List<DishFlavor> dishFlavorList = dishDTO.getFlavors();
        if (dishFlavorList != null && !dishFlavorList.isEmpty()) {
            dishFlavorList.forEach(dishFlavor -> {
                // 遍历列表给菜品ID赋值
                dishFlavor.setDishId(dishId);
            });
            // 向口味表插入数据
            dishFlavorMapper.insertBatch(dishFlavorList);
        }
    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return com.sky.result.PageResult
     * @author paxi
     * @data 2023/8/28
     **/
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO> page =  dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 根据ID批量删除菜品
     * @param ids
     * @return void
     * @author paxi
     * @data 2023/8/28
     **/
    @Override
    @Transactional
    public void deleteByIds(List<Long> ids) {
        // 判断当前菜品是否能够删除——--是否存在起售中的菜品
        for (Long id :ids) {
            Dish dish = dishMapper.getById(id);
            // 此处亦可在查询中加入状态的判断，判断结果是否大于0
            if (Objects.equals(dish.getStatus(), StatusConstant.ENABLE)) {
                // 当前菜品处于售卖中，不能删除
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        // 判断当前菜品是否能够删除——--是否有套餐关联了该菜品
        List<Long> setmealIdsByDishIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
        if (setmealIdsByDishIds != null && !setmealIdsByDishIds.isEmpty()) {
            // 当前菜品有被套餐关联，不能删除
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        // 删除菜品表中的菜品数据
        for (Long id : ids) {
            dishMapper.deleteById(id);
            // 删除菜品关联的口味数据
            dishFlavorMapper.deleteByDishId(id);
        }
    }
}
