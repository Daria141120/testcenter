package com.example.testcenter.model.db.repository;


import com.example.testcenter.model.db.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository <Client, Long> {

    Optional<Client> findFirstByEmailOrPhone(String email, String phone);





}
