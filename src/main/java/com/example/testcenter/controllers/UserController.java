package com.example.testcenter.controllers;


import com.example.testcenter.model.dto.request.EmployeeInfoReq;
import com.example.testcenter.model.dto.request.UserInfoReq;
import com.example.testcenter.model.dto.response.TaskInfoResp;
import com.example.testcenter.model.dto.response.UserInfoResp;
import com.example.testcenter.model.enums.Role;
import com.example.testcenter.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;

import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;
import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Пользователи")
@SecurityScheme(type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT", name = "Authorization")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по id")
    public UserInfoResp getUser(@PathVariable ("id") Long id){
        return userService.getUser(id);
    }


    @PutMapping("/{id}")
    @Operation(summary = "Обновить пароль")
    public UserInfoResp updateUser(@PathVariable ("id") Long id, @RequestBody String password, Principal principal){
       return userService.updateUser(id, password, principal);
    }


    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить пользователя по id")
    public void deleteUser(@PathVariable ("id") Long id){
        userService.deleteUser(id);
    }


    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/all")
    @Operation(summary = "Получить всех пользователей")
    public List<UserInfoResp> getAllUser(){
        return userService.getAllUser();
    }


    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}/addRole")
    @Operation(summary = "Добавить роль пользователю")
    public UserInfoResp addRoleToUser(@PathVariable ("id") Long id, @RequestBody String role){
        return userService.addRoleToUser(id, role);
    }


    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/{id}/roles")
    @Operation(summary = "Получить все роли пользователя")
    public Set<Role> getAllUserRoles(@PathVariable ("id") Long id){
        return userService.getAllUserRoles(id);
    }


    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}/deleteRole")
    @Operation(summary = "Удалить роль у пользователя")
    public void deleteUserRole(@PathVariable ("id") Long id, @RequestBody String role){
        userService.deleteUserRole(id, role);
    }


    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/roles")
    @Operation(summary = "Получить все возможные роли")
    public Set<Role> getExistRoles(){
        return userService.getExistRoles();
    }


    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/{id}/myTasks")
    @Operation(summary = "Получить все задачи пользователя")
    public List<TaskInfoResp> getUserTasks(@PathVariable ("id") Long id){
        return userService.getUserTasks(id);
    }


    @PutMapping("/{id}/addEmployee")
    @Operation(summary = "Заполнить и создать сотрудника")
    public UserInfoResp addEmployee(@PathVariable ("id") Long id, @RequestBody EmployeeInfoReq employeeReq, Principal principal){
        return userService.addEmployee(id, employeeReq, principal);
    }


    @GetMapping("/myTasksInfo")
    @Operation(summary = "Информация о задачах назначенных пользователю")
    public List<TaskInfoResp> userData(Principal principal) {
        return userService.userTaskInfo(principal);
    }


}
