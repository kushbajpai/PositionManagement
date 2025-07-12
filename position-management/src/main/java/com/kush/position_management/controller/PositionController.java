package com.kush.position_management.controller;

import com.kush.position_management.model.Transaction;
import com.kush.position_management.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Position Management API", description = "API for managing positions based on transactions")
@RestController
@RequestMapping("/api")
public class PositionController {
    private final PositionService positionService;

    @Autowired
    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @Operation(summary = "Process a transaction")
    @PostMapping("/transaction")
    public void processTransaction(Transaction transaction) {
        positionService.processTransaction(transaction);
    }


    @Operation(summary = "Get all positions")
    @GetMapping("/positions")
    public Map<String, Integer> getPositions() {
        return positionService.getPositions();
    }
}
