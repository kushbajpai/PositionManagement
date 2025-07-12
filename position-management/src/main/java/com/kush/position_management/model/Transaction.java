package com.kush.position_management.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@Entity
@Table(name = "\"TRANSACTION\"")
@Data
public class Transaction {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    private Long tradeId;
    private int version;
    private String securityCode;
    private int quantity;

    @Enumerated(EnumType.STRING)
    private Action action;

    @Enumerated(EnumType.STRING)
    @Column(name = "buy_sell")
    private BuySell buySell;


    public enum Action { INSERT, UPDATE, CANCEL}
    public enum BuySell { BUY, SELL}
}
