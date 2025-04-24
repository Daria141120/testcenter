package com.example.testcenter.model.db.repository;

import com.example.testcenter.model.db.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
