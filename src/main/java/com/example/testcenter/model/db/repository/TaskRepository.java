package com.example.testcenter.model.db.repository;

import com.example.testcenter.model.db.entity.Task;
import com.example.testcenter.model.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByStatus(TaskStatus status);
}
