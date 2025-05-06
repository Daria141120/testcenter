package com.example.testcenter.service;

import com.example.testcenter.model.dto.request.UserInfoReq;
import com.example.testcenter.model.dto.response.TaskInfoResp;
import com.example.testcenter.model.dto.response.UserInfoResp;
import com.example.testcenter.model.enums.Role;

import java.util.List;
import java.util.Set;

public interface UserService {
    UserInfoResp createNewUser(UserInfoReq req);

    UserInfoResp getUser(Long id);

    UserInfoResp updateUser(Long id, UserInfoReq req);

    void deleteUser(Long id);

    List<UserInfoResp> getAllUser();

    Set<Role> getExistRoles();

    void deleteUserRole(Long id, String role);

    Set<Role> getAllUserRoles(Long id);

    UserInfoResp addRoleToUser(Long id, String role);

    List<TaskInfoResp> getUserTasks(Long id);
}
