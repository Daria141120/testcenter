package com.example.testcenter.service.impl;

import com.example.testcenter.exception.CommonBackendException;
import com.example.testcenter.model.db.entity.Employee;
import com.example.testcenter.model.db.entity.Laboratory;
import com.example.testcenter.model.db.repository.LaboratoryRepository;
import com.example.testcenter.model.dto.request.LaboratoryInfoReq;
import com.example.testcenter.model.dto.response.EmployeeInfoResp;
import com.example.testcenter.model.dto.response.LaboratoryInfoResp;
import com.example.testcenter.model.dto.response.TaskInfoResp;
import com.example.testcenter.model.enums.LaboratoryStatus;
import com.example.testcenter.service.LaboratoryService;
import com.example.testcenter.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LaboratoryServiceImpl implements LaboratoryService {

    private final ObjectMapper objectMapper;
    private final LaboratoryRepository laboratoryRepository;
    private final TaskService taskService;

    @Override
    public Laboratory getLaboratoryFromDB(Long id){
        Optional<Laboratory> labFromDB = laboratoryRepository.findById(id);
        final String errMsg = String.format("laboratory with id : %s not found", id);
        return labFromDB.orElseThrow(() -> new CommonBackendException(errMsg, HttpStatus.NOT_FOUND));
    }

    @Override
    public LaboratoryInfoResp getLaboratory(Long id) {
        Laboratory labFromDB = getLaboratoryFromDB(id);
        return objectMapper.convertValue(labFromDB, LaboratoryInfoResp.class);
    }

    @Override
    public LaboratoryInfoResp addLaboratory(LaboratoryInfoReq req) {
        laboratoryRepository.findFirstByName(req.getName()).ifPresent(
                lab -> {throw new CommonBackendException("Laboratory already exist", HttpStatus.CONFLICT);
                });

        Laboratory lab = objectMapper.convertValue(req, Laboratory.class);
        lab.setStatus(LaboratoryStatus.CREATED);

        Laboratory labSaved = laboratoryRepository.save(lab);
        return objectMapper.convertValue(labSaved, LaboratoryInfoResp.class);
    }


    @Override
    public LaboratoryInfoResp updateLaboratory(Long id, LaboratoryInfoReq req) {
        Laboratory labFromDB = getLaboratoryFromDB(id);
        Laboratory labForUpdate = objectMapper.convertValue(req, Laboratory.class);

        labFromDB.setName(labForUpdate.getName() == null ? labFromDB.getName() : labForUpdate.getName());
        labFromDB.setDescription(labForUpdate.getDescription() == null ? labFromDB.getDescription() : labForUpdate.getDescription());

        labFromDB.setStatus(LaboratoryStatus.UPDATED);
        labFromDB = laboratoryRepository.save(labFromDB);
        return objectMapper.convertValue(labFromDB, LaboratoryInfoResp.class);
    }


    @Override
    public void deleteLaboratory(Long id) {
        Laboratory labFromDB = getLaboratoryFromDB(id);
        labFromDB.setStatus(LaboratoryStatus.LIQUIDATED);
        laboratoryRepository.save(labFromDB);
    }


    @Override
    public List<LaboratoryInfoResp> getAllLaboratory(String status) {
        List<LaboratoryInfoResp> labRespList;
        if (StringUtils.hasText(status)){

            List<String> listStatus = getAllLabStatus().stream().map(Enum::name).collect(Collectors.toList());
            if (!listStatus.contains(status)) {
                throw new CommonBackendException("Error in the status, there is no such status.", HttpStatus.BAD_REQUEST);
            } else {
                LaboratoryStatus labStatus = LaboratoryStatus.valueOf(status);
                labRespList = laboratoryRepository.findAllByStatus(labStatus).stream()
                        .map(lab -> objectMapper.convertValue(lab, LaboratoryInfoResp.class))
                        .collect(Collectors.toList());
            }

        }else {
            labRespList = laboratoryRepository.findAll().stream()
                    .map(lab -> objectMapper.convertValue(lab, LaboratoryInfoResp.class))
                    .collect(Collectors.toList());
        }
        return labRespList;
    }

    @Override
    public List<EmployeeInfoResp> getLaboratoryEmployees(Long id) {
        Laboratory lab = getLaboratoryFromDB(id);
        LaboratoryInfoResp labResp = objectMapper.convertValue(lab, LaboratoryInfoResp.class);

        List <Employee> employeeList = lab.getEmployeeList();
        List <EmployeeInfoResp> respList = employeeList.stream()
                .map(employee -> objectMapper.convertValue(employee, EmployeeInfoResp.class))
                .collect(Collectors.toList());

        respList.forEach(employeeInfoResp -> employeeInfoResp.setLaboratory(labResp));
        return respList;
    }

    @Override
    public void updateLabListEmployee(Laboratory lab){
        laboratoryRepository.save(lab);
    }

    @Override
    public List<TaskInfoResp> getAllTasks(Long id, String status) {
        return taskService.getAllTasksByLaboratory(id, status);
    }

    @Override
    public List<LaboratoryStatus> getAllLabStatus(){
        return Arrays.stream(LaboratoryStatus.values()).collect(Collectors.toList());
    }


}
