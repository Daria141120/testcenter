package com.example.testcenter.mapper;

import com.example.testcenter.model.db.entity.Employee;
import com.example.testcenter.model.dto.response.EmployeeInfoResp;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EmployeeMapper {

    EmployeeInfoResp toEmployeeInfoResp(Employee employee);

    List <EmployeeInfoResp> toEmployeeInfoRespList (List<Employee> employees);

}
