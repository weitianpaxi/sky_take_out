package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * 新增员工
     * @param employee
     * @return int
     * @author paxi
     * @data 2023/8/20
     **/
    @Insert("insert into employee(name, username, password, phone, sex, id_number, status, create_time, " +
            "update_time, create_user, update_user) " +
            "values(#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{status},#{createTime}," +
            "#{updateTime},#{createUser},#{updateUser}) ")
    int insert(Employee employee);

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return com.github.pagehelper.Page<com.sky.entity.Employee>
     * @author paxi
     * @data 2023/8/21
     **/
    Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 更新员工信息
     * @param employee
     * @return void
     * @author paxi
     * @data 2023/8/21
     **/
    void update(Employee employee);

    /**
     * 根据ID查询员工信息
     * @param id
     * @return com.sky.entity.Employee
     * @author paxi
     * @data 2023/8/21
     **/
    @Select("select * from employee where id = #{id}")
    Employee getById(long id);
}
