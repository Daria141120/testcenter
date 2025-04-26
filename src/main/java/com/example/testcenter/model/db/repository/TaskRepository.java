package com.example.testcenter.model.db.repository;

import com.example.testcenter.model.db.entity.Task;
import com.example.testcenter.model.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByStatus(TaskStatus status);

    List<Task> findAllByStatusNot(TaskStatus status);

    @Query("select t from Task t where t.orderItem.clientOrder.id = :orderId and not t.status = 'CLOSED' ")
    List<Task> findAllNotCompletedTask(@Param("orderId") Long id);

    long countByStatusNotAndOrderItem_ClientOrder_Id(TaskStatus status, Long id);

    List<Task> findAllByEmployee_IdAndStatus(Long id, TaskStatus status);

    @Query("select t from Task t where t.status = :status and t.orderItem.equipExam.exam.laboratory.id = :labId")
    List<Task> findAllByStatusAndLab(@Param("status") TaskStatus status, @Param("labId") Long labId);

    @Query("select t from Task t where t.orderItem.equipExam.exam.laboratory.id = :labId")
    List<Task> findAllByLab(@Param("labId") Long labId);


    // аналогично выше... 4 join - :(
    List<Task> findAllByStatusAndOrderItem_EquipExam_Exam_Laboratory_Id(TaskStatus status, Long id);



//    @Query("select count(t) from Task t where t.orderItem.clientOrder.id = :orderId and not t.status = 'CLOSED' ")
//    Integer countAllNotCompletedTask (@Param("orderId") Long id);

}
