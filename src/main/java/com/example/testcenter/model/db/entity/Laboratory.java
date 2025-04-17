package com.example.testcenter.model.db.entity;

import com.example.testcenter.model.enums.LaboratoryStatus;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "laboratories")
public class Laboratory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private LaboratoryStatus status;

    @OneToMany(mappedBy = "laboratory")
    @JsonManagedReference
    List<Employee> employeeList;

}
