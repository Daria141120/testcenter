package com.example.testcenter.model.db.repository;

import com.example.testcenter.model.db.entity.Employee;
import com.example.testcenter.model.enums.EmployeeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository <Employee, Long> {
    List<Employee> findAllByStatus(EmployeeStatus status);

    Optional<Employee> findFirstByEmailAndLastName(String email, String lastName);

    List<Employee> findEmployeesByLaboratory_Id(Long id);
}
