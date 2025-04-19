package com.example.demo.repository;


import com.example.demo.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,String> {
    Optional<Transaction> findByTransactionId(String id);


    @Query(value = "select t from transaction where created_at between :starDate and :endDate AND status = :status",nativeQuery = true)
    List<Transaction> findByTransactionDate(LocalDate startDate, LocalDate endDate, String status);
}
