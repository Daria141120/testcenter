package com.example.testcenter.model.db.entity;

import com.example.testcenter.model.enums.EmployeeStatus;
import com.example.testcenter.model.enums.Post;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "post")
    @Enumerated(EnumType.STRING)
    private Post post;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EmployeeStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "laboratory_id")
    private Laboratory laboratory;

}
