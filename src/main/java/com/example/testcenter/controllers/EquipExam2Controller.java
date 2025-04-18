package com.example.testcenter.controllers;


import com.example.testcenter.model.db.entity.EquipExam2Key;
import com.example.testcenter.model.dto.request.EmployeeInfoReq;
import com.example.testcenter.model.dto.request.EquipExam2InfoReq;
import com.example.testcenter.model.dto.response.EmployeeInfoResp;
import com.example.testcenter.model.dto.response.EquipExam2InfoResp;
import com.example.testcenter.service.EquipExam2Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/equipExamConnection")
@RequiredArgsConstructor
@Tag(name = "Выполняемые работы (оборудование + испытание)")
public class EquipExam2Controller {

     private final EquipExam2Service equipExam2Service;

    @GetMapping("/{id}&{id2}")
    @Operation(summary = "Получить связь испытание-оборудование")
    public EquipExam2InfoResp getEquipExam(@PathVariable("id") Long id, @PathVariable("id2") Long id2) {
        return equipExam2Service.getEquipExam(id, id2);
    }

    @GetMapping("/all")
    @Operation(summary = "Получить все связи испытание-оборудование")
    public List<EquipExam2InfoResp> getEquipExamAll() {
        return equipExam2Service.getEquipExamAll();
    }


    @PostMapping
    @Operation(summary = "Добавить связь испытание-оборудование")
    public EquipExam2InfoResp addEquipExam(@RequestBody @Valid EquipExam2InfoReq req) {
        return equipExam2Service.addEquipExam(req);
    }






//    @PostMapping
//    @Operation(summary = "Добавить связи для оборудования со списком испытаний")
//    public EquipExam2InfoResp addEquipExam(@RequestBody @Valid EquipExam2InfoReq req) {
//        return equipExam2Service.addEquipExam(req);
//    }








}
