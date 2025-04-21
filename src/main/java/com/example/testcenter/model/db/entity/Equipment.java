package com.example.testcenter.model.db.entity;

import com.example.testcenter.model.enums.EquipStatus;
import com.example.testcenter.model.enums.TypeEquipment;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "equipments")
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private TypeEquipment typeEquipment;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EquipStatus status;

    @OneToMany(mappedBy = "equipment")
    @JsonManagedReference
    List<EquipExam2> equipExam2List;



}
