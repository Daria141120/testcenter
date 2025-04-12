package com.example.testcenter.model.db.entity;


import javax.persistence.*;

import com.example.testcenter.model.enums.ClientStatus;
import com.example.testcenter.model.enums.ExamStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "exams")
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "laboratory_id")
    private Laboratory laboratory;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ExamStatus status;


}
