package com.kush.position_management.service;

import com.kush.position_management.model.Transaction;
import com.kush.position_management.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class PositionService {

    private final PositionRepository positionRepository;

    @Autowired
    public PositionService(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    public synchronized void processTransaction(Transaction transaction) {
        positionRepository.saveTransaction(transaction);
    }

    public Map<String, Integer> getPositions() {
        return positionRepository.getPositions();
    }
}