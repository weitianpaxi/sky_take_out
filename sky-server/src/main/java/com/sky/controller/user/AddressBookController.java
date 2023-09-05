package com.sky.controller.user;

import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user/addressBook")
@Slf4j
@Api(tags = "小程序端-地址管理接口")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 查询当前登录用户的所有地址信息
     * @return com.sky.result.Result<java.util.List<com.sky.entity.AddressBook>>
     * @author paxi
     * @data 2023/9/5
     **/
    @GetMapping("/list")
    @ApiOperation(value = "查询当前登录用户的所有地址信息")
    public Result<List<AddressBook>> list() {
        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(BaseContext.getCurrentId());
        List<AddressBook> list = addressBookService.list(addressBook);
        return Result.success(list);
    }

    /**
     * 新增地址
     * @param addressBook
     * @return com.sky.result.Result
     * @author paxi
     * @data 2023/9/5
     **/
    @PostMapping
    @ApiOperation(value = "新增地址")
    public Result<String> save(@RequestBody AddressBook addressBook) {
        log.info("新增地址为:{}",addressBook);
        addressBookService.save(addressBook);
        return Result.success();
    }

    /**
     * 根据id查询地址
     * @param id
     * @return com.sky.result.Result<com.sky.entity.AddressBook>
     * @author paxi
     * @data 2023/9/5
     **/
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询地址")
    public Result<AddressBook> getById(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(addressBook);
    }

    /**
     * 根据id修改地址
     * @param addressBook
     * @return com.sky.result.Result
     * @author paxi
     * @data 2023/9/5
     **/
    @PutMapping
    @ApiOperation(value = "根据id修改地址")
    public Result<String> update(@RequestBody AddressBook addressBook) {
        log.info("修改地址为:{}",addressBook);
        addressBookService.update(addressBook);
        return Result.success();
    }

    /**
     * 设置默认地址
     * @param addressBook
     * @return com.sky.result.Result
     * @author paxi
     * @data 2023/9/5
     **/
    @PutMapping("/default")
    @ApiOperation(value = "设置默认地址")
    public Result<String> setDefault(@RequestBody AddressBook addressBook) {
        log.info("设置默认地址为:{}",addressBook);
        addressBookService.setDefault(addressBook);
        return Result.success();
    }

    /**
     * 根据id删除地址
     * @param id
     * @return com.sky.result.Result
     * @author paxi
     * @data 2023/9/5
     **/
    @DeleteMapping
    @ApiOperation(value = "根据id删除地址")
    public Result deleteById(Long id) {
        addressBookService.deleteById(id);
        return Result.success();
    }

   /**
    * 查询默认地址
    * @return com.sky.result.Result<com.sky.entity.AddressBook>
    * @author paxi
    * @data 2023/9/5
    **/
    @GetMapping("default")
    @ApiOperation(value = "查询默认地址")
    public Result<AddressBook> getDefault() {
        AddressBook addressBook = new AddressBook();
        addressBook.setIsDefault(1);
        addressBook.setUserId(BaseContext.getCurrentId());
        List<AddressBook> list = addressBookService.list(addressBook);

        if (list != null && list.size() == 1) {
            return Result.success(list.get(0));
        }

        return Result.error(MessageConstant.NO_DEFAULT_ADDRESS);
    }

}
