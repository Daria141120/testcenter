package com.example.testcenter.model.db.repository;


import com.example.testcenter.model.db.entity.ClientOrder;
import com.example.testcenter.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ClientOrderRepository extends JpaRepository<ClientOrder, Long> {
    List<ClientOrder> findAllByStatus(OrderStatus status);

    long countByCreatedAtBetween(LocalDateTime dateStart, LocalDateTime dateCurrent);

    Optional<ClientOrder> findByOrderNumber(String orderNumber);


}
