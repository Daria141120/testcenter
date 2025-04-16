package com.example.testcenter.model.db.repository;


import com.example.testcenter.model.db.entity.ClientOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientOrderRepository extends JpaRepository<ClientOrder, Long> {



}
