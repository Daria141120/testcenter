package com.example.testcenter.controllers;


import com.example.testcenter.model.dto.request.ExamInfoReq;
import com.example.testcenter.model.dto.request.LaboratoryInfoReq;
import com.example.testcenter.model.dto.response.ExamInfoResp;
import com.example.testcenter.model.dto.response.LaboratoryInfoResp;
import com.example.testcenter.service.ExamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/exams")
@RequiredArgsConstructor
@Tag(name = "Испытания")
public class ExamController {

    private final ExamService examService;

    @GetMapping("/{id}")
    @Operation(summary = "Получить испытание по id")
    public ExamInfoResp getExam(@PathVariable("id") Long id){
        return examService.getExam(id);
    }

    @PostMapping
    @Operation(summary = "Добавить испытание")
    public ExamInfoResp addExam(@RequestBody @Valid ExamInfoReq examInfoReq){
        return examService.addExam(examInfoReq);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить даннные испытания по id")
    public ExamInfoResp updateExam(@PathVariable ("id") Long id, @RequestBody ExamInfoReq examInfoReq){
        return examService.updateExam(id, examInfoReq);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить испытание")
    public void deleteExam(@PathVariable ("id") Long id){
        examService.deleteExam(id);
    }

    @GetMapping("/all")
    @Operation(summary = "Получить все испытания")
    public List<ExamInfoResp> getAllExam (){
        return examService.getAllExam();
    }



}
