package com.example.testcenter.service;

import com.example.testcenter.model.db.entity.Laboratory;
import com.example.testcenter.model.dto.request.LaboratoryInfoReq;
import com.example.testcenter.model.dto.response.EmployeeInfoResp;
import com.example.testcenter.model.dto.response.LaboratoryInfoResp;
import com.example.testcenter.model.dto.response.TaskInfoResp;
import com.example.testcenter.model.enums.LaboratoryStatus;

import java.util.List;

public interface LaboratoryService {

    Laboratory getLaboratoryFromDB(Long id);

    LaboratoryInfoResp getLaboratory(Long id);

    LaboratoryInfoResp  addLaboratory(LaboratoryInfoReq laboratoryInfoReq);

    LaboratoryInfoResp  updateLaboratory(Long id, LaboratoryInfoReq laboratoryInfoReq);

    void deleteLaboratory(Long id);

    List<LaboratoryInfoResp > getAllLaboratory(String status);

    List<EmployeeInfoResp> getLaboratoryEmployees(Long id);

    void updateLabListEmployee(Laboratory laboratory);

    List<TaskInfoResp> getAllTasks(Long id, String status);

    List<LaboratoryStatus> getAllLabStatus();
}
