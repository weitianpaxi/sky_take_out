package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    /**
     * 向菜品表批量插入数据
     * @param dishFlavorList
     * @return void
     * @author paxi
     * @data 2023/8/27
     **/
    void insertBatch(List<DishFlavor> dishFlavorList);
}
