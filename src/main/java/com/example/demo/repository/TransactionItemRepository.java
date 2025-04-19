package com.example.demo.repository;


import com.example.demo.model.entity.TransactionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionItemRepository extends JpaRepository<TransactionItem,String> {
    List<TransactionItem> findByTransactionId(String trxId);
}
