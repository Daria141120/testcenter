package com.example.testcenter.service.impl;

import com.example.testcenter.exception.CommonBackendException;
import com.example.testcenter.model.db.entity.Employee;
import com.example.testcenter.model.db.entity.Laboratory;
import com.example.testcenter.model.db.repository.LaboratoryRepository;
import com.example.testcenter.model.dto.request.LaboratoryInfoReq;
import com.example.testcenter.model.dto.response.EmployeeInfoResp;
import com.example.testcenter.model.dto.response.LaboratoryInfoResp;
import com.example.testcenter.model.enums.LaboratoryStatus;
import com.example.testcenter.service.LaboratoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LaboratoryServiceImpl implements LaboratoryService {

    private final ObjectMapper objectMapper;
    private final LaboratoryRepository laboratoryRepository;

    @Override
    public Laboratory getLaboratoryFromDB(Long id){
        Optional<Laboratory> laboratoryFromDB = laboratoryRepository.findById(id);
        final String errMsg = String.format("laboratory with id : %s not found", id);
        return laboratoryFromDB.orElseThrow(() -> new CommonBackendException(errMsg, HttpStatus.NOT_FOUND));
    }

    @Override
    public LaboratoryInfoResp getLaboratory(Long id) {
        Laboratory laboratoryFromDB = getLaboratoryFromDB(id);
        return objectMapper.convertValue(laboratoryFromDB, LaboratoryInfoResp.class);
    }

    @Override
    public LaboratoryInfoResp addLaboratory(LaboratoryInfoReq laboratoryInfoReq) {
        laboratoryRepository.findFirstByName(laboratoryInfoReq.getName()).ifPresent(
                lab -> {throw new CommonBackendException("Laboratory already exist", HttpStatus.CONFLICT);
                });

        Laboratory laboratory = objectMapper.convertValue(laboratoryInfoReq, Laboratory.class);
        laboratory.setStatus(LaboratoryStatus.CREATED);

        Laboratory laboratorySaved = laboratoryRepository.save(laboratory);
        return objectMapper.convertValue(laboratorySaved, LaboratoryInfoResp.class);
    }


    @Override
    public LaboratoryInfoResp updateLaboratory(Long id, LaboratoryInfoReq laboratoryInfoReq) {
        Laboratory laboratoryFromDB = getLaboratoryFromDB(id);
        Laboratory laboratoryForUpdate = objectMapper.convertValue(laboratoryInfoReq, Laboratory.class);

        if (laboratoryForUpdate.getName() != null) {
            laboratoryFromDB.setName(laboratoryForUpdate.getName());
        }

        if (laboratoryForUpdate.getDescription() != null) {
            laboratoryFromDB.setDescription(laboratoryForUpdate.getDescription());
        }

        laboratoryFromDB.setStatus(LaboratoryStatus.UPDATED);
        laboratoryFromDB = laboratoryRepository.save(laboratoryFromDB);
        return objectMapper.convertValue(laboratoryFromDB, LaboratoryInfoResp.class);
    }


    @Override
    public void deleteLaboratory(Long id) {
        Laboratory laboratoryFromDB = getLaboratoryFromDB(id);

        laboratoryFromDB.setStatus(LaboratoryStatus.LIQUIDATED);
        laboratoryRepository.save(laboratoryFromDB);
    }


    @Override
    public List<LaboratoryInfoResp> getAllLaboratory() {
        return laboratoryRepository.findAll().stream()
                .map(lab -> objectMapper.convertValue(lab, LaboratoryInfoResp.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeInfoResp> getLaboratoryEmployees(Long id) {
        Laboratory laboratory = getLaboratoryFromDB(id);
        LaboratoryInfoResp laboratoryInfoResp = objectMapper.convertValue(laboratory, LaboratoryInfoResp.class);

        List <Employee> employeeList = laboratory.getEmployeeList();
        List <EmployeeInfoResp> infoRespList = employeeList.stream()
                .map(employee -> objectMapper.convertValue(employee, EmployeeInfoResp.class))
                .collect(Collectors.toList());

        infoRespList.forEach(employeeInfoResp -> employeeInfoResp.setLaboratory(laboratoryInfoResp));
        return infoRespList;
    }

    @Override
    public void updateLabListEmployee(Laboratory laboratory){
        laboratoryRepository.save(laboratory);
    }


}
