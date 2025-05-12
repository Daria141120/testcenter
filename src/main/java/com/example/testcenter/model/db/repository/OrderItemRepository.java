package com.example.testcenter.model.db.repository;

import com.example.testcenter.model.db.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findAllByClientOrder_Id(Long id);

}
