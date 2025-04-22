package com.example.testcenter.model.db.repository;

import com.example.testcenter.model.db.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
