package com.example.testcenter.model.db.repository;


import com.example.testcenter.model.db.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository <Client, Long> {


}
