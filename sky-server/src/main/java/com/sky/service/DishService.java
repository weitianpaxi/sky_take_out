package com.sky.service;

import com.sky.dto.DishDTO;
import org.springframework.stereotype.Service;

public interface DishService {

   /**
    * 新增菜品和对应的口味数据
    * @param dishDTO
    * @return void
    * @author paxi
    * @data 2023/8/27
    **/
    void saveWithFlavor(DishDTO dishDTO);
}
