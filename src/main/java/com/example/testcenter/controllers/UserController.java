package com.example.testcenter.controllers;


import com.example.testcenter.model.dto.request.UserInfoReq;
import com.example.testcenter.model.dto.response.TaskInfoResp;
import com.example.testcenter.model.dto.response.UserInfoResp;
import com.example.testcenter.model.enums.Role;
import com.example.testcenter.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Пользователи")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по id")
    public UserInfoResp getUser(@PathVariable ("id") Long id){
        return userService.getUser(id);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    // @PreAuthorize("#req.username == authentication.principal.username || hasAuthority('ROLE_ADMIN') ")
    @PutMapping("/{id}")
    @Operation(summary = "Обновить даннные пользователя по id")
    public UserInfoResp updateUser(@PathVariable ("id") Long id, @RequestBody UserInfoReq req){
       return userService.updateUser(id, req);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Удалить пользователя по id")
    public void deleteUser(@PathVariable ("id") Long id){
        userService.deleteUser(id);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
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
    @PutMapping("/{id}")
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


    //@PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/{id}/myTasks")
    @Operation(summary = "Получить все задачи пользователя")
    public List<TaskInfoResp> getUserTasks(@PathVariable ("id") Long id){
        return userService.getUserTasks(id);
    }


}
