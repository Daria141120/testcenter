package com.example.testcenter.model.db.repository;

import com.example.testcenter.model.db.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository <Employee, Long> {


    Optional<Employee> findFirstByEmailAndLastName(String email, String lastName);
}
