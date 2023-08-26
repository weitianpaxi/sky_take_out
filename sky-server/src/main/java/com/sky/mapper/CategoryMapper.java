package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CategoryMapper {

    /**
     * 新增分类
     * @param category
     * @return int
     * @author paxi
     * @data 2023/8/23
     **/
    @Insert("insert into sky_take_out.category(type, name, sort, status, create_time, update_time," +
            " create_user, update_user)" +
            "values " +
            "(#{type},#{name},#{sort},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    int save(Category category);

    /**
     * 修改分类
     * @param category
     * @return int
     * @author paxi
     * @data 2023/8/23
     **/
    int update(Category category);

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return com.github.pagehelper.Page<com.sky.entity.Category>
     * @author paxi
     * @data 2023/8/23
     **/
    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 根据ID删除分类
     * @param id
     * @return int
     * @author paxi
     * @data 2023/8/26
     **/
    @Delete("delete from sky_take_out.category where id = #{id}")
    int deleteById(long id);
}
