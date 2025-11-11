package com.final_project.serverapp.repositories;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.final_project.serverapp.models.Order;
import com.final_project.serverapp.models.User;

import java.util.List;



@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUserOrderByCreatedAtDesc(User user);
    
    List<Order> findAllByOrderByCreatedAtDesc();
    
    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
