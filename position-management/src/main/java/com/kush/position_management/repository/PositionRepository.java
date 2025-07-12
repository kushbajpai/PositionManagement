package com.kush.position_management.repository;

import com.kush.position_management.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class PositionRepository {

    private final TransactionRepository transactionRepository;

    @Autowired
    public PositionRepository(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public synchronized void saveTransaction(Transaction transaction) {

        Optional<Transaction> latestTxnOpt = transactionRepository
                .findTopByTradeIdOrderByVersionDesc(transaction.getTradeId());

        if (latestTxnOpt.isPresent()) {
            Transaction latestTxn = latestTxnOpt.get();

            if (transaction.getVersion() < latestTxn.getVersion()) {
                return;
            }

            if (transaction.getAction() == Transaction.Action.CANCEL) {
                adjustPosition(latestTxn, true); // reverse previous
                transactionRepository.save(transaction);
                return;
            }

            adjustPosition(latestTxn, true);
            adjustPosition(transaction, false);
            transactionRepository.save(transaction);
        } else {
            // First INSERT
            if (transaction.getAction() == Transaction.Action.INSERT) {
                adjustPosition(transaction, false);
                //check if the security code already exists do not save if it does
                Optional<Transaction> existing = transactionRepository.findBySecurityCode(transaction.getSecurityCode());
                if (existing.isPresent()) {
                    return; // Already adjusted, do not save
                }
                transactionRepository.save(transaction);
            }
        }
    }

    private void adjustPosition(Transaction transaction, boolean b) {
        transactionRepository.adjustPosition(transaction.getSecurityCode(),
                transaction.getQuantity() * (transaction.getBuySell() == Transaction.BuySell.SELL ? -1 : 1) * (b ? -1 : 1));
    }

    public Map<String, Integer> getPositions() {
        List<Transaction> allTransactions = transactionRepository.findAll();

        // Find the latest transaction for each TradeID
        Map<Long, Transaction> latestTransactions = new HashMap<>();
        for (Transaction t : allTransactions) {
            Transaction current = latestTransactions.get(t.getTradeId());
            if (current == null || t.getVersion() > current.getVersion()) {
                latestTransactions.put(t.getTradeId(), t);
            }
        }

        // Aggregate positions per SecurityCode from latest non-canceled transactions
        Map<String, Integer> positions = new HashMap<>();
        for (Transaction txn : latestTransactions.values()) {
            if (txn.getAction() == Transaction.Action.CANCEL) {
                // Ensure security is present with 0 if canceled
                positions.putIfAbsent(txn.getSecurityCode(), 0);
                continue;
            }
            int qty = txn.getQuantity();
            if (txn.getBuySell() == Transaction.BuySell.SELL) {
                qty = -qty;
            }
            positions.merge(txn.getSecurityCode(), qty, Integer::sum);
        }

        return positions;
    }
}