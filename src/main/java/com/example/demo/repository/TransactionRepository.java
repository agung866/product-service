package com.example.demo.repository;


import com.example.demo.model.projection.TransactionResultProjection;
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


    @Query(value = """
            select 
            t.transaction_id,
            t.email,t.total_price,
            ti.product_id,
            p.product_name,
            ti.quantity,
            ti.price,
            t.status
            from schema_transaction."transaction" t
            join schema_transaction."transaction_item" ti ON ti.transaction_id = t.transaction_id
            join schema_product.product p ON p.product_id = ti.product_id
            where t.created_at between :startDate and :endDate AND status = :status
            """,nativeQuery = true)
    List<TransactionResultProjection> findByTransactionDate(LocalDate startDate, LocalDate endDate, String status);
}
