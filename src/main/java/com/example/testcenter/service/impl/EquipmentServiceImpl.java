package com.example.testcenter.service.impl;


import com.example.testcenter.exception.CommonBackendException;
import com.example.testcenter.model.db.entity.Equipment;
import com.example.testcenter.model.db.repository.EquipmentRepository;
import com.example.testcenter.model.dto.request.EquipmentInfoReq;
import com.example.testcenter.model.dto.response.EquipmentInfoResp;
import com.example.testcenter.model.enums.EquipStatus;
import com.example.testcenter.service.EquipmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final ObjectMapper objectMapper;

    private Equipment getEquipmentFromDB (Long id){
        Optional<Equipment> equipmentFromDB = equipmentRepository.findById(id);
        final String errMsg = String.format("equipment with id : %s not found", id);
        return equipmentFromDB.orElseThrow(() -> new CommonBackendException(errMsg, HttpStatus.NOT_FOUND));
    }


    @Override
    public EquipmentInfoResp getEquipment(Long id) {
        Equipment equipmentFromDB = getEquipmentFromDB(id);
        return objectMapper.convertValue(equipmentFromDB, EquipmentInfoResp.class);
    }


    @Override
    public EquipmentInfoResp addEquipment(EquipmentInfoReq equipmentInfoReq) {
        equipmentRepository.findFirstByName(equipmentInfoReq.getName()).ifPresent(
                equip -> { throw new CommonBackendException("Equipment already exist", HttpStatus.CONFLICT);
                });

        Equipment equipment = objectMapper.convertValue(equipmentInfoReq, Equipment.class);
        equipment.setStatus(EquipStatus.CREATED);

        equipment = equipmentRepository.save(equipment);
        return objectMapper.convertValue(equipment, EquipmentInfoResp.class);
    }


    @Override
    public EquipmentInfoResp updateEquipment(Long id, EquipmentInfoReq equipmentInfoReq) {
        Equipment equipmentFromDB = getEquipmentFromDB(id);
        Equipment equipmentForUpdate = objectMapper.convertValue(equipmentInfoReq, Equipment.class);

        equipmentFromDB.setName(equipmentForUpdate.getName() == null ? equipmentFromDB.getName() : equipmentForUpdate.getName());
        equipmentFromDB.setTypeEquipment(equipmentForUpdate.getTypeEquipment() == null ? equipmentFromDB.getTypeEquipment() : equipmentForUpdate.getTypeEquipment());
        equipmentFromDB.setStatus(EquipStatus.UPDATED);

        equipmentFromDB = equipmentRepository.save(equipmentFromDB);
        return objectMapper.convertValue(equipmentFromDB, EquipmentInfoResp.class);
    }

    @Override
    public void deleteEquipment(Long id) {
        Equipment equipment = getEquipmentFromDB(id);
        equipment.setStatus(EquipStatus.DELETED);
        equipmentRepository.save(equipment);
    }

    @Override
    public List<EquipmentInfoResp> getAllEquipment() {
        return equipmentRepository.findAll().stream().map(equip -> objectMapper.convertValue(equip, EquipmentInfoResp.class))
                .toList();
    }
}
