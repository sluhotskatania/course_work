package com.example.course_work.repository;

import com.example.course_work.entity.Client;
import com.example.course_work.entity.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ClientRepository extends JpaRepository <Client, Long>, JpaSpecificationExecutor<Client> {
    Page<Client> findAll(Pageable pageable);

    Page<Client> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Client> findBySurnameContainingIgnoreCase(String surname, Pageable pageable);
}
