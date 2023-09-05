package com.sky.service;

import com.sky.entity.AddressBook;
import java.util.List;

public interface AddressBookService {

    /**
     * 按条件查询用户所有地址信息
     * @param addressBook
     * @return java.util.List<com.sky.entity.AddressBook>
     * @author paxi
     * @data 2023/9/5
     **/
    List<AddressBook> list(AddressBook addressBook);

    /**
     * 新增地址
     * @param addressBook
     * @return void
     * @author paxi
     * @data 2023/9/5
     **/
    void save(AddressBook addressBook);

    /**
     * 根据id查询
     * @param id
     * @return com.sky.entity.AddressBook
     * @author paxi
     * @data 2023/9/5
     **/
    AddressBook getById(Long id);

    /**
     * 根据id修改地址
     * @param addressBook
     * @return void
     * @author paxi
     * @data 2023/9/5
     **/
    void update(AddressBook addressBook);

    /**
     * 设置默认地址
     * @param addressBook
     * @return void
     * @author paxi
     * @data 2023/9/5
     **/
    void setDefault(AddressBook addressBook);

    /**
     * 根据id删除地址
     * @param id
     * @return void
     * @author paxi
     * @data 2023/9/5
     **/
    void deleteById(Long id);

}
