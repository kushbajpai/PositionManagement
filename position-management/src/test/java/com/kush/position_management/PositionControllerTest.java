package com.kush.position_management;

import com.kush.position_management.controller.PositionController;
import com.kush.position_management.model.Transaction;
import com.kush.position_management.service.PositionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PositionControllerTest {

    @Mock
    private PositionService positionService;

    @InjectMocks
    private PositionController positionController;

    @Nested
    @DisplayName("processTransaction")
    class ProcessTransaction {

        @org.junit.jupiter.api.Test
        void processesValidTransactionSuccessfully() {
            Transaction transaction = new Transaction();
            positionController.processTransaction(transaction);
            verify(positionService, times(1)).processTransaction(transaction);
        }

        @org.junit.jupiter.api.Test
        void handlesNullTransactionGracefully() {
            assertDoesNotThrow(() -> positionController.processTransaction(null));
            verify(positionService, times(1)).processTransaction(null);
        }
    }

    @Nested
    @DisplayName("getPositions")
    class GetPositions {

        @org.junit.jupiter.api.Test
        void returnsPositionsWhenAvailable() {
            Map<String, Integer> expected = new HashMap<>();
            expected.put("AAPL", 10);
            when(positionService.getPositions()).thenReturn(expected);

            Map<String, Integer> result = positionController.getPositions();

            assertEquals(expected, result);
        }

        @org.junit.jupiter.api.Test
        void returnsEmptyMapWhenNoPositions() {
            when(positionService.getPositions()).thenReturn(Collections.emptyMap());

            Map<String, Integer> result = positionController.getPositions();

            assertTrue(result.isEmpty());
        }
    }
}