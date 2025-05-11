package com.example.testcenter.service.impl;

import com.example.testcenter.exception.CommonBackendException;
import com.example.testcenter.model.db.entity.Employee;
import com.example.testcenter.model.db.entity.User;
import com.example.testcenter.model.db.repository.UserRepository;
import com.example.testcenter.model.dto.request.EmployeeInfoReq;
import com.example.testcenter.model.dto.request.UserInfoReq;
import com.example.testcenter.model.dto.response.EmployeeInfoResp;
import com.example.testcenter.model.dto.response.TaskInfoResp;
import com.example.testcenter.model.dto.response.UserInfoResp;
import com.example.testcenter.model.enums.Role;
import com.example.testcenter.service.EmployeeService;
import com.example.testcenter.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeService employeeService;

    @Value("${admin.login}")
    private String adminLogin;

    @Value("${admin.password}")
    private String password;

    @Override
    public void createAdmin() {
        if (!userRepository.findByUsername(adminLogin).isPresent()) {
            User admin = new User();
            admin.setUsername(adminLogin);
            admin.setPassword(passwordEncoder.encode(password));
            Set<Role> roles = new HashSet<>();
            roles.add(Role.ROLE_ADMIN);
            admin.setRoles(roles);
            userRepository.save(admin);
            log.info("Admin with name - {} created", admin.getUsername());
        } else {
            log.info("Admin with name - {} already exists", adminLogin);
        }
    }


    @Override
    public UserInfoResp createNewUser(UserInfoReq req) {
        if (!req.getPassword().equals(req.getPasswordConfirmation())) {
            throw new CommonBackendException("Password and password confirmation do not match", HttpStatus.BAD_REQUEST);
        }
        if (userRepository.findByUsername(req.getUsername()).isPresent()) {
            throw new CommonBackendException("User already exists", HttpStatus.CONFLICT);
        }

        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        Set<Role> roles = new HashSet<>();
        roles.add(Role.ROLE_USER);
        user.setRoles(roles);
        user = userRepository.save(user);
        log.info("User with username - {} created", user.getUsername());
        return new UserInfoResp(user.getId(), user.getUsername());
    }

    @Override
    public UserInfoResp getUser(Long id) {
        User user = getUserFromDB(id);
        UserInfoResp resp = new UserInfoResp();
        resp.setId(user.getId());
        resp.setUsername(user.getUsername());
        return resp;
    }

    private User getUserFromDB(Long id) {
        Optional<User> user = userRepository.findById(id);
        final String errMsg = String.format("user with id : %s not found", id);
        return user.orElseThrow(() -> new CommonBackendException(errMsg, HttpStatus.NOT_FOUND));
    }


    @Override
    public UserInfoResp updateUser(Long id, UserInfoReq req, Principal principal) {
        User userFromDB = getUserFromDB(id);
        if (!userFromDB.getUsername().equals(principal.getName())){
            throw new CommonBackendException("Access denied!", HttpStatus.FORBIDDEN);
        }

        User userForUpdate = new User();
        userForUpdate.setUsername(req.getUsername());
        userForUpdate.setPassword(passwordEncoder.encode(req.getPassword()));
        userFromDB.setUsername(req.getUsername() == null ? userFromDB.getUsername() : userForUpdate.getUsername());
        userFromDB.setPassword(req.getPassword() == null ? userFromDB.getPassword() : userForUpdate.getPassword());

        return new UserInfoResp(userFromDB.getId(), userFromDB.getUsername());
    }

    @Override
    public void deleteUser(Long id) {
        getUserFromDB(id);
        userRepository.deleteById(id);
    }

    @Override
    public List<UserInfoResp> getAllUser() {
        List<User> userList = userRepository.findAll();
        return userList.stream().map(user -> {
            UserInfoResp userResp = new UserInfoResp();
            userResp.setId(user.getId());
            userResp.setUsername(user.getUsername());
            return userResp;
        }).collect(Collectors.toList());
    }

    @Override
    public Set<Role> getAllUserRoles(Long id) {
        User userFromDB = getUserFromDB(id);
        return userFromDB.getRoles();
    }

    @Override
    public UserInfoResp addRoleToUser(Long id, String role) {
        User userFromDB = getUserFromDB(id);
        Set<Role> roles = userFromDB.getRoles();
        roles.add(Role.valueOf(role));
        userFromDB.setRoles(roles);
        userFromDB = userRepository.save(userFromDB);
        return new UserInfoResp(userFromDB.getId(), userFromDB.getUsername());
    }


    @Override
    public void deleteUserRole(Long id, String role) {
        User userFromDB = getUserFromDB(id);
        Set<Role> roles = userFromDB.getRoles();
        roles.remove(Role.valueOf(role));
        userFromDB.setRoles(roles);
        userRepository.save(userFromDB);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new CommonBackendException(String.format("User with username %s not found", username), HttpStatus.BAD_REQUEST)
        );
    }

    @Override
    public Set<Role> getExistRoles() {
        return Arrays.stream(Role.values()).collect(Collectors.toSet());
    }

    @Override
    public UserInfoResp addEmployee(Long id, EmployeeInfoReq employeeReq, Principal principal) {
        User userFromDB = getUserFromDB(id);

        if (!userFromDB.getUsername().equals(principal.getName())){
            throw new CommonBackendException("Access denied!", HttpStatus.FORBIDDEN);
        }

        EmployeeInfoResp employeeResp = employeeService.addEmployee(employeeReq);
        Employee employeeCreated = employeeService.getEmployeeFromDB(employeeResp.getId());
        userFromDB.setEmployee(employeeCreated);
        userFromDB = userRepository.save(userFromDB);
        return new UserInfoResp(userFromDB.getId(), userFromDB.getUsername());
    }

    @Override
    public List<TaskInfoResp> getUserTasks(Long id) {
        User userFromDB = getUserFromDB(id);
        Employee employee = userFromDB.getEmployee();
        return employeeService.getAllAssignedTasks(employee.getId());
    }


    @Override
    public List<TaskInfoResp> userTaskInfo(Principal principal) {
        List<TaskInfoResp> taskRespList = List.of();
        String username = principal.getName();
        User user = getUserByUsername(username);
        Employee employee = user.getEmployee();
        if (employee != null) {
            taskRespList = employeeService.getAllAssignedTasks(employee.getId());
        }
        return taskRespList;
    }
}
