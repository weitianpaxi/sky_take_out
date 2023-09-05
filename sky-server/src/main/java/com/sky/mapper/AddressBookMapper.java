package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface AddressBookMapper {

    /**
     * 条件查询
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
    void insert(AddressBook addressBook);

    /**
     * 根据id查询地址
     * @param id
     * @return com.sky.entity.AddressBook
     * @author paxi
     * @data 2023/9/5
     **/
    @Select("select * from address_book where id = #{id}")
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
     * 根据用户id修改 是否是默认地址
     * @param addressBook
     * @return void
     * @author paxi
     * @data 2023/9/5
     **/
    @Update("update address_book set is_default = #{isDefault} where user_id = #{userId}")
    void updateIsDefaultByUserId(AddressBook addressBook);

    /**
     * 根据id删除地址
     * @param id
     * @return void
     * @author paxi
     * @data 2023/9/5
     **/
    @Delete("delete from address_book where id = #{id}")
    void deleteById(Long id);

}
