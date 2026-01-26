CREATE TABLE mc1_messages (
                              id INT PRIMARY KEY AUTO_INCREMENT,
                              session_id INT NOT NULL,
                              MC1_timestamp DATETIME,
                              MC2_timestamp DATETIME,
                              MC3_timestamp DATETIME,
                              end_timestamp DATETIME
);
