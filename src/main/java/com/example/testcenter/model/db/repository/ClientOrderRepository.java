package com.example.testcenter.model.db.repository;


import com.example.testcenter.model.db.entity.ClientOrder;
import com.example.testcenter.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientOrderRepository extends JpaRepository<ClientOrder, Long> {
    List<ClientOrder> findAllByStatus(OrderStatus status);



}
