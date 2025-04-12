package com.example.testcenter.model.db.repository;

import com.example.testcenter.model.db.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExamRepository extends JpaRepository <Exam, Long> {

    Optional<Exam> findFirsByName(String name);





}
