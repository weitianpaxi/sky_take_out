package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 新增员工
     * @param employeeDTO
     * @return boolean
     * @author paxi
     * @data 2023/8/20
     **/
    boolean save(EmployeeDTO employeeDTO);

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return com.sky.result.PageResult
     * @author paxi
     * @data 2023/8/21
     **/
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 启用和禁用员工
     * @param status
     * @param id
     * @return void
     * @author paxi
     * @data 2023/8/21
     **/
    void startOrStop(Integer status, long id);

    /**
     * 根据ID查询员工
     * @param id
     * @return com.sky.entity.Employee
     * @author paxi
     * @data 2023/8/21
     **/
    Employee getById(long id);

    /**
     * 编辑员工信息
     * @param employeeDTO
     * @return void
     * @author paxi
     * @data 2023/8/21
     **/
    void update(EmployeeDTO employeeDTO);
}
