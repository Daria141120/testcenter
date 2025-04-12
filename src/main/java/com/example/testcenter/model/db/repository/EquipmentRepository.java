package com.example.testcenter.model.db.repository;

import com.example.testcenter.model.db.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EquipmentRepository extends JpaRepository <Equipment, Long> {


    Optional<Equipment> findFirstByName(String name);
}
