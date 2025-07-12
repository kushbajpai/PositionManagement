package com.kush.position_management.repository;

import com.kush.position_management.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findTopByTradeIdOrderByVersionDesc(Long tradeId);

    @Modifying
    @Transactional
    @Query("UPDATE Transaction t SET t.quantity = t.quantity + :qty WHERE t.securityCode = :code")
    void adjustPosition(@Param("code") String code, @Param("qty") int qty);
}