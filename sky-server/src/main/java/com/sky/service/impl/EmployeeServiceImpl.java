package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        //对前台数据进行MD5加密处理
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 新增员工
     * @param employeeDTO
     * @return boolean
     * @author paxi
     * @data 2023/8/20
     **/
    @Override
    public boolean save(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        // 将DTO对象转换为实体类对象，使用对象属性拷贝
        BeanUtils.copyProperties(employeeDTO,employee);

        // 继续设置实体类的其他属性
        // 将默认密码转换为MD5格式
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        // 账号默认是启用的
        employee.setStatus(StatusConstant.ENABLE);
        // 创建时间为当前系统时间
        // employee.setCreateTime(LocalDateTime.now());
        // employee.setUpdateTime(LocalDateTime.now());

        // 记录当前记录的创建人ID和修改人ID
        // employee.setCreateUser(BaseContext.getCurrentId());
        // employee.setUpdateUser(BaseContext.getCurrentId());

         return employeeMapper.insert(employee) > 0;

    }

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return com.sky.result.PageResult
     * @author paxi
     * @data 2023/8/21
     **/
    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        // 分页查询实现 基于 PageHelper
         PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());

        // 开始查询
        Page<Employee> page = employeeMapper.pageQuery(employeePageQueryDTO);

        long total = page.getTotal();                       // 获取全部查询总数
        List<Employee> employeeList = page.getResult();     // 拿到当前页查询结果

        return new PageResult(total,employeeList);
    }

    /**
     * 员工状态管理
     * @param status
     * @param id
     * @return void
     * @author paxi
     * @data 2023/8/21
     **/
    @Override
    public void startOrStop(Integer status, long id) {
        Employee employee =  Employee.builder()
                .status(status)
                .id(id)
                .build();
        employeeMapper.update(employee);
    }

    /**
     * 根据ID获取员工信息
     * @param id
     * @return com.sky.entity.Employee
     * @author paxi
     * @data 2023/8/22
     **/
    @Override
    public Employee getById(long id) {
        Employee employee = employeeMapper.getById(id);
        employee.setPassword("*******");
        return employee;
    }

    /**
     * 编辑员工信息
     * @param employeeDTO
     * @return void
     * @author paxi
     * @data 2023/8/22
     **/
    @Override
    public void update(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        // 属性拷贝
        BeanUtils.copyProperties(employeeDTO, employee);

        // 设置更新时间和操作用户ID
        // employee.setUpdateTime(LocalDateTime.now());
        // employee.setUpdateUser(BaseContext.getCurrentId());

        employeeMapper.update(employee);
    }

    /**
     * 修改密码
     * @param passwordEditDTO
     * @return void
     * @author paxi
     * @data 2023/8/22
     **/
    @Override
    public void updatePassword(PasswordEditDTO passwordEditDTO) {
        passwordEditDTO.setEmpId(BaseContext.getCurrentId());
        String password = passwordEditDTO.getOldPassword();
        Employee employee = employeeMapper.getById(passwordEditDTO.getEmpId());

        // 异常处理
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        // 旧密码比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            // 旧密码输入错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        // 新密码加密存储
        employee.setPassword(DigestUtils.md5DigestAsHex(passwordEditDTO.getNewPassword().getBytes()));
        // 更新密码
        employeeMapper.update(employee);
    }


}
