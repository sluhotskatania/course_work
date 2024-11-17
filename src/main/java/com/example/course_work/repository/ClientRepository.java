package com.example.course_work.repository;

import com.example.course_work.entity.Client;
import com.example.course_work.entity.Tour;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepository extends JpaRepository <Client, Long> {
    List<Client> findByNameContainingIgnoreCase(String name);
    List<Client> findBySurnameContainingIgnoreCase(String surname);
    List<Client> findByNameContainingIgnoreCaseAndSurnameContainingIgnoreCase(String name, String surname);
}
