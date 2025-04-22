package com.example.testcenter.model.db.entity;


import com.example.testcenter.model.enums.Availability;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "equip_exams2")
public class EquipExam2 {

    @EmbeddedId
    private EquipExam2Key id = new EquipExam2Key();  // без new не конвертирует при пост запросе: Could not set field value [4] value by reflection  - для ключа


    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("equipmentId")
    @JoinColumn(name = "equipment_id", nullable = false)
    @JsonBackReference
    private Equipment equipment;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("examId")
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @Column(name = "availability", length = 20)
    @Enumerated(EnumType.STRING)
    private Availability availability;



}
