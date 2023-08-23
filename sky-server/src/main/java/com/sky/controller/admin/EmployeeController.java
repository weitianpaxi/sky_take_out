package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工操作相关接口")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation(value = "员工退出登录")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 新增员工
     * @param employeeDTO
     * @return com.sky.result.Result<java.lang.String>
     * @author paxi
     * @data 2023/8/20
     **/
    @PostMapping
    @ApiOperation(value = "新增员工")
    public Result save(@RequestBody EmployeeDTO employeeDTO) {
        log.info("新增员工: {}", employeeDTO);

        if (employeeService.save(employeeDTO)) {
            return Result.success();
        } else {
            return Result.error("新增失败");
        }
    }

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return com.sky.result.Result<com.sky.result.PageResult>
     * @author paxi
     * @data 2023/8/21
     **/
    @GetMapping("/page")
    @ApiOperation(value = "员工分页查询")
    public Result<PageResult> getPage(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("员工分页查询：{}",employeePageQueryDTO);

        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 员工状态管理
     * @return com.sky.result.Result
     * @author paxi
     * @data 2023/8/21
     **/
    @PostMapping("/status/{status}")
    @ApiOperation(value = "启用，禁用员工账号")
    public Result startOrStop(@PathVariable Integer status,long id) {
        log.info("启用和禁用员工账号：{},{}",id ,status);
        employeeService.startOrStop(status,id);
        return Result.success();
    }

    /**
     * 根据ID查询员工信息
     * @param id
     * @return com.sky.result.Result
     * @author paxi
     * @data 2023/8/21
     **/
    @GetMapping("/{id}")
    @ApiOperation(value = "根据ID查询员工信息")
    public Result<Employee> getById(@PathVariable long id) {
        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }

    /**
     * 编辑员工信息
     * @param employeeDTO
     * @return com.sky.result.Result
     * @author paxi
     * @data 2023/8/21
     **/
    @PutMapping
    @ApiOperation(value = "编辑员工信息")
    public Result update(@RequestBody EmployeeDTO employeeDTO) {
        log.info("编辑员工信息：{}",employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success();
    }

    @PutMapping("/editPassword")
    @ApiOperation(value = "修改密码")
    public Result editPassword(@RequestBody PasswordEditDTO passwordEditDTO) {
        log.info("更改密码：{}",passwordEditDTO);
        employeeService.updatePassword(passwordEditDTO);
        return Result.success();
    }
}
