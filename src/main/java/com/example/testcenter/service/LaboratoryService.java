package com.example.testcenter.service;

import com.example.testcenter.model.dto.request.LaboratoryInfoReq;
import com.example.testcenter.model.dto.response.LaboratoryInfoResp;

import java.util.List;

public interface LaboratoryService {

    LaboratoryInfoResp getLaboratory(Long id);

    LaboratoryInfoResp  addLaboratory(LaboratoryInfoReq laboratoryInfoReq);

    LaboratoryInfoResp  updateLaboratory(Long id, LaboratoryInfoReq laboratoryInfoReq);

    void deleteLaboratory(Long id);

    List<LaboratoryInfoResp > getAllLaboratory();


}
