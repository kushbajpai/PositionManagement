-- Table to store trades
DROP TABLE IF EXISTS transaction;

CREATE TABLE transaction (
    transaction_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    trade_id BIGINT NOT NULL,
    version INT NOT NULL,
    security_code VARCHAR(20) NOT NULL,
    quantity INT NOT NULL,
    action VARCHAR(10) NOT NULL,
    buy_sell VARCHAR(10) NOT NULL
);

-- View to calculate the net position for each security
CREATE VIEW position AS
SELECT security_code,
       SUM(CASE WHEN side = 'BUY' AND action <> 'CANCEL' THEN quantity
                WHEN side = 'SELL' AND action <> 'CANCEL' THEN -quantity
                ELSE 0 END) AS net_quantity
FROM transaction
GROUP BY security_code;