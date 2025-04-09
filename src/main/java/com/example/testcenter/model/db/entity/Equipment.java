package com.example.testcenter.model.db.entity;

import com.example.testcenter.model.enums.EquipStatus;
import com.example.testcenter.model.enums.TypeEquipment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "equipments")
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private TypeEquipment typeEquipment;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EquipStatus status;


}
