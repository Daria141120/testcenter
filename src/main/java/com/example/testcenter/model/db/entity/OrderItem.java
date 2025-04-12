package com.example.testcenter.model.db.entity;


import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private ClientOrder order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equip_exam_id")
    private EquipExam equipExam;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "exam_result")
    private String examResult;

    @Column(name = "additional_info")
    private String info;



}
