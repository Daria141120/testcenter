package com.example.testcenter.model.db.repository;

import com.example.testcenter.model.db.entity.Laboratory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LaboratoryRepository extends JpaRepository<Laboratory, Long> {
    Optional<Laboratory> findFirstByName(String name);


}
