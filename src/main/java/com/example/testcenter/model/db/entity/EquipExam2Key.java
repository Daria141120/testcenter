package com.example.testcenter.model.db.entity;


import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class EquipExam2Key implements Serializable {

    private static final long serialVersionUID = 8488050786082163956L;

    @Column(name = "equipment_id", nullable = false)
    private Long equipmentId;

    @Column(name = "exam_id", nullable = false)
    private Long examId;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EquipExam2Key entity = (EquipExam2Key) o;
        return Objects.equals(this.equipmentId, entity.equipmentId) &&
                Objects.equals(this.examId, entity.examId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(equipmentId, examId);
    }


    @Override
    public String toString() {
        return "EquipExam2Key{" +
                "equipmentId=" + equipmentId +
                ", examId=" + examId +
                '}';
    }



}
