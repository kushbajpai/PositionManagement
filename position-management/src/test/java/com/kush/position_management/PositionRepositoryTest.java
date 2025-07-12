package com.kush.position_management;

import com.kush.position_management.model.Transaction;
import com.kush.position_management.repository.PositionRepository;
import com.kush.position_management.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PositionRepositoryTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private PositionRepository positionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void savesNewInsertTransactionAndAdjustsPosition() {
        Transaction transaction = new Transaction();
        transaction.setTradeId(1L);
        transaction.setVersion(1);
        transaction.setAction(Transaction.Action.INSERT);
        transaction.setBuySell(Transaction.BuySell.BUY);
        transaction.setQuantity(100);
        transaction.setSecurityCode("AAPL");

        when(transactionRepository.findTopByTradeIdOrderByVersionDesc(1L)).thenReturn(Optional.empty());

        positionRepository.saveTransaction(transaction);

        verify(transactionRepository).adjustPosition("AAPL", 100);
        verify(transactionRepository).save(transaction);
    }

    @Test
    void doesNotSaveTransactionWithLowerOrEqualVersion() {
        Transaction transaction = new Transaction();
        transaction.setTradeId(2L);
        transaction.setVersion(1);

        Transaction latest = new Transaction();
        latest.setVersion(2);

        when(transactionRepository.findTopByTradeIdOrderByVersionDesc(2L)).thenReturn(Optional.of(latest));

        positionRepository.saveTransaction(transaction);

        verify(transactionRepository, never()).save(transaction);
    }

    @Test
    void cancelsTransactionAndReversesPreviousAdjustment() {
        Transaction transaction = new Transaction();
        transaction.setTradeId(3L);
        transaction.setVersion(2);
        transaction.setAction(Transaction.Action.CANCEL);

        Transaction latest = new Transaction();
        latest.setTradeId(3L);
        latest.setVersion(1);
        latest.setAction(Transaction.Action.INSERT);
        latest.setBuySell(Transaction.BuySell.BUY);
        latest.setQuantity(50);
        latest.setSecurityCode("GOOG");

        when(transactionRepository.findTopByTradeIdOrderByVersionDesc(3L)).thenReturn(Optional.of(latest));

        positionRepository.saveTransaction(transaction);

        verify(transactionRepository).adjustPosition("GOOG", -50);
        verify(transactionRepository).save(transaction);
    }

    @Test
    void updatesPositionOnNewerVersionInsert() {
        Transaction transaction = new Transaction();
        transaction.setTradeId(4L);
        transaction.setVersion(2);
        transaction.setAction(Transaction.Action.INSERT);
        transaction.setBuySell(Transaction.BuySell.BUY);
        transaction.setQuantity(200);
        transaction.setSecurityCode("MSFT");

        Transaction latest = new Transaction();
        latest.setTradeId(4L);
        latest.setVersion(1);
        latest.setAction(Transaction.Action.INSERT);
        latest.setBuySell(Transaction.BuySell.BUY);
        latest.setQuantity(100);
        latest.setSecurityCode("MSFT");

        when(transactionRepository.findTopByTradeIdOrderByVersionDesc(4L)).thenReturn(Optional.of(latest));

        positionRepository.saveTransaction(transaction);

        verify(transactionRepository).adjustPosition("MSFT", -100);
        verify(transactionRepository).adjustPosition("MSFT", 200);
        verify(transactionRepository).save(transaction);
    }

    @Test
    void getPositionsReturnsAggregatedPositions() {
        Transaction t1 = new Transaction();
        t1.setTradeId(1L);
        t1.setVersion(1);
        t1.setAction(Transaction.Action.INSERT);
        t1.setBuySell(Transaction.BuySell.BUY);
        t1.setQuantity(100);
        t1.setSecurityCode("AAPL");

        Transaction t2 = new Transaction();
        t2.setTradeId(2L);
        t2.setVersion(1);
        t2.setAction(Transaction.Action.INSERT);
        t2.setBuySell(Transaction.BuySell.SELL);
        t2.setQuantity(50);
        t2.setSecurityCode("AAPL");

        when(transactionRepository.findAll()).thenReturn(List.of(t1, t2));

        Map<String, Integer> positions = positionRepository.getPositions();

        assertEquals(1, positions.size());
        assertEquals(50, positions.get("AAPL"));
    }

    @Test
    void getPositionsSkipsCancelledTransactions() {
        Transaction t1 = new Transaction();
        t1.setTradeId(1L);
        t1.setVersion(1);
        t1.setAction(Transaction.Action.INSERT);
        t1.setBuySell(Transaction.BuySell.BUY);
        t1.setQuantity(100);
        t1.setSecurityCode("AAPL");

        Transaction t2 = new Transaction();
        t2.setTradeId(1L);
        t2.setVersion(2);
        t2.setAction(Transaction.Action.CANCEL);
        t2.setBuySell(Transaction.BuySell.BUY);
        t2.setQuantity(100);
        t2.setSecurityCode("AAPL");

        when(transactionRepository.findAll()).thenReturn(List.of(t1, t2));

        Map<String, Integer> positions = positionRepository.getPositions();

        assertFalse(positions.isEmpty());
    }
}