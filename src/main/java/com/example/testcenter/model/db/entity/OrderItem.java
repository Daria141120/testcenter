package com.example.testcenter.model.db.entity;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "order_items", uniqueConstraints = { @UniqueConstraint( columnNames = {"order_id", "equipmentId", "examId"} )  })
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", nullable = false)
    private ClientOrder clientOrder;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "equip_exam_id")
//    private EquipExam equipExam;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name = "equipmentId", nullable = false),
            @JoinColumn(name = "examId", nullable = false)
    })
    private EquipExam2 equipExam;


    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "exam_result")
    private String examResult;


    @Column(name = "additional_info")
    private String info;


}
