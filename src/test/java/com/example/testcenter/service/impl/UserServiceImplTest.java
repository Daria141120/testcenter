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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private  UserRepository userRepository;

    @Spy
    public PasswordEncoder passwordEncoder;

    @Mock
    private  EmployeeService employeeService;


    @Test
    public void createAdmin() {
        User user = new User();
        user.setId(1L);
        user.setUsername("user1");

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        userService.createAdmin();
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    public void createAdminAlreadyExist() {
        User user = new User();
        user.setId(1L);
        user.setUsername("user1");

        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);
        userService.createAdmin();
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test(expected = CommonBackendException.class)
    public void createNewUserWrongPassConfirm() {
        UserInfoReq req = new UserInfoReq();
        req.setPassword("123");
        req.setPasswordConfirmation("abc");
        userService.createNewUser(req);
    }

    @Test(expected = CommonBackendException.class)
    public void createNewUserExist() {
        UserInfoReq req = new UserInfoReq();
        req.setPassword("123");
        req.setPasswordConfirmation("123");
        req.setUsername("name");

        when(userRepository.findByUsername(req.getUsername())).thenReturn(Optional.of(new User()));
        userService.createNewUser(req);
    }

    @Test
    public void createNewUser() {
        UserInfoReq req = new UserInfoReq();
        req.setPassword("123");
        req.setPasswordConfirmation("123");
        req.setUsername("name");

        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(req.getPassword());

        when(userRepository.save(any(User.class))).thenReturn(user);
        UserInfoResp resp = userService.createNewUser(req);
        assertEquals(user.getUsername(), resp.getUsername());
    }

    @Test
    public void getUserFromDB() {
        User user = new User();
        user.setId(1L);
        user.setUsername("name");

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        User userFromDB = userService.getUserFromDB(user.getId());
        assertEquals(user.getUsername(), userFromDB.getUsername());
    }

    @Test(expected = CommonBackendException.class)
    public void getUserFromDBNotFound() {
        userService.getUserFromDB(1L);
    }

    @Test
    public void getUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("name");

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        UserInfoResp resp = userService.getUser(user.getId());
        assertEquals(user.getUsername(), resp.getUsername());
    }

    @Test
    public void updateUser() {
        String newPassword = "123";

        User userFromDB = new User();
        userFromDB.setId(1L);
        userFromDB.setUsername("name");

        Principal principal = new Principal() {
            @Override
            public String getName() {
                return userFromDB.getUsername();
            }
        };

        when(userRepository.findById(userFromDB.getId())).thenReturn(Optional.of(userFromDB));
        when(passwordEncoder.encode(newPassword)).thenReturn(newPassword);
        when(userRepository.save(any(User.class))).thenReturn(userFromDB);

        UserInfoResp resp = userService.updateUser(userFromDB.getId(), newPassword, principal);

        assertEquals(newPassword, userFromDB.getPassword());
        assertEquals(userFromDB.getId(), resp.getId());
    }


    @Test(expected = CommonBackendException.class)
    public void updateUserAccessDenied() {
        User user = new User();
        user.setUsername("name");

        Principal principal = new Principal() {
            @Override
            public String getName() {
                return "another name";
            }
        };

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        userService.updateUser(user.getId(), anyString(), principal);
    }

    @Test
    public void deleteUser() {
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        userService.deleteUser(user.getId());
        verify(userRepository, times(1)).deleteById(anyLong());
    }


    @Test
    public void getAllUser() {
        User user1 = new User();
        User user2 = new User();
        List<User> userList = List.of(user1, user2);

        when(userRepository.findAll()).thenReturn(userList);
        List<UserInfoResp> userRespList = userService.getAllUser();
        assertEquals(userList.size(), userRespList.size());
    }

    @Test
    public void getAllUserRoles() {
        User user = new User();
        user.setId(1L);
        Set<Role> roles = Set.of(Role.ROLE_ADMIN, Role.ROLE_USER);
        user.setRoles(roles);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Set<Role> rolesResp = userService.getAllUserRoles(user.getId());
        assertEquals(roles.size(), rolesResp.size());
    }

    @Test
    public void addRoleToUser() {
        Role newRole = Role.ROLE_CHIEF;

        User user = new User();
        user.setId(1L);
        Set<Role> roles = new HashSet<>();
        roles.add(Role.ROLE_USER);
        roles.add(Role.ROLE_ADMIN);
        user.setRoles(roles);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        UserInfoResp userResp = userService.addRoleToUser(user.getId(), newRole.name());

        assertEquals(user.getId(), userResp.getId());
        assertEquals(3, user.getRoles().size());
        assertTrue(user.getRoles().contains(newRole));
    }

    @Test
    public void deleteUserRole() {
        Role deleteRole = Role.ROLE_CHIEF;

        User user = new User();
        user.setId(1L);
        Set<Role> roles = new HashSet<>();
        roles.add(Role.ROLE_CHIEF);
        roles.add(Role.ROLE_ADMIN);
        user.setRoles(roles);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.deleteUserRole(user.getId(), deleteRole.name());
        assertFalse(user.getRoles().contains(deleteRole));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void getUserByUsername() {
        String username = "Tom";
        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        User userFromDB = userService.getUserByUsername(username);

        assertEquals(username, userFromDB.getUsername());
    }

    @Test(expected = CommonBackendException.class)
    public void getUserByUsernameNotFound() {
        userService.getUserByUsername(anyString());
    }


    @Test
    public void addEmployee() {
        User user = new User();
        user.setId(1L);
        user.setUsername("name");

        EmployeeInfoReq emplReq = new EmployeeInfoReq();
        EmployeeInfoResp emplResp = new EmployeeInfoResp();
        emplResp.setId(1L);
        Employee employee = new Employee();
        employee.setId(emplResp.getId());

        Principal principal = new Principal() {
            @Override
            public String getName() {
                return user.getUsername();
            }
        };

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(employeeService.addEmployee(emplReq)).thenReturn(emplResp);
        when(employeeService.getEmployeeFromDB(emplResp.getId())).thenReturn(employee);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserInfoResp userResp = userService.addEmployee(user.getId(), emplReq, principal);
        assertEquals(employee.getId(), user.getEmployee().getId());
    }

    @Test(expected = CommonBackendException.class)
    public void addEmployeeAccessDenied() {
        User user = new User();
        user.setUsername("name");
        EmployeeInfoReq emplReq = new EmployeeInfoReq();

        Principal principal = new Principal() {
            @Override
            public String getName() {
                return "another name";
            }
        };
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        userService.addEmployee(user.getId(), emplReq, principal);
    }


    @Test
    public void userTaskInfo() {
        List<TaskInfoResp> taskRespList = List.of(new TaskInfoResp(), new TaskInfoResp());

        Employee employee = new Employee();
        employee.setId(1L);

        User user = new User();
        user.setUsername("name");
        user.setEmployee(employee);

        Principal principal = new Principal() {
            @Override
            public String getName() {
                return user.getUsername();
            }
        };

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(employeeService.getAllAssignedTasks(employee.getId())).thenReturn(taskRespList);

        List<TaskInfoResp> taskRespListFromDB = userService.userTaskInfo(principal);
        assertEquals(taskRespList.size(), taskRespListFromDB.size());
    }

    @Test
    public void userTaskInfoEmpty() {
        User user = new User();
        user.setUsername("name");

        Principal principal = new Principal() {
            @Override
            public String getName() {
                return user.getUsername();
            }
        };

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        List<TaskInfoResp> taskRespListFromDB = userService.userTaskInfo(principal);
        assertEquals(0, taskRespListFromDB.size());
    }
}