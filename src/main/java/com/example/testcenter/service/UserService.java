package com.example.testcenter.service;

import com.example.testcenter.model.db.entity.User;
import com.example.testcenter.model.dto.request.EmployeeInfoReq;
import com.example.testcenter.model.dto.request.UserInfoReq;
import com.example.testcenter.model.dto.response.TaskInfoResp;
import com.example.testcenter.model.dto.response.UserInfoResp;
import com.example.testcenter.model.enums.Role;

import java.security.Principal;
import java.util.List;
import java.util.Set;

public interface UserService {
    void createAdmin();

    UserInfoResp createNewUser(UserInfoReq req);

    UserInfoResp getUser(Long id);

    User getUserFromDB(Long id);

    UserInfoResp updateUser(Long id, String req, Principal principal);

    void deleteUser(Long id);

    List<UserInfoResp> getAllUser();

    User getUserByUsername(String username);

    Set<Role> getExistRoles();

    void deleteUserRole(Long id, String role);

    Set<Role> getAllUserRoles(Long id);

    UserInfoResp addRoleToUser(Long id, String role);

    List<TaskInfoResp> getUserTasks(Long id);

    UserInfoResp addEmployee(Long id, EmployeeInfoReq employeeReq, Principal principal);

    List<TaskInfoResp> userTaskInfo(Principal principal);
}
