CREATE TABLE mc1_messages (
        id INT PRIMARY KEY AUTO_INCREMENT,
        session_id INT NOT NULL,
        MC1_timestamp TIMESTAMP,
        MC2_timestamp TIMESTAMP,
        MC3_timestamp TIMESTAMP,
        end_timestamp TIMESTAMP
);
