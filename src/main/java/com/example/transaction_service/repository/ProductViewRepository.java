package com.example.transaction_service.repository;

import com.example.transaction_service.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductViewRepository extends JpaRepository<Product,String> {
    Optional<Product> findByProductId(Long productId);
    List<Product> findAllByProductIdIn(List<Long> productId);

}
