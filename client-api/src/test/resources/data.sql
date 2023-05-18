-- Inserts for client table
INSERT INTO client (name, surname, phone, salary, social_credit)
VALUES ('John', 'Doe', '1234567890', 5000, 10000.00),
       ('Jane', 'Doe', '0987654321', 7000, 15000.00),
       ('Alice', 'Smith', '5551234', 3000, 5000.00),
       ('Bob', 'Johnson', '4445678', 6000, 12000.00),
       ('Charlie', 'Brown', '7771212', 4000, 8000.00);

-- Inserts for credit table
INSERT INTO credit (last_payment_date, penalty, client_id, credit_balance, credit_status)
VALUES ('2022-01-31', 50.00, 1, 5000.00, 'CREATED'),
       ('2021-12-31', 75.00, 2, 7500.00, 'APPROVED'),
       ('2022-02-28', 100.00, 3, 2500.00, 'NEED_COLLECTOR'),
       ('2022-03-31', 0.00, 4, 9000.00, 'NOT_APPROVED'),
       ('2022-04-30', 25.00, 5, 6000.00, 'NEED_HUMAN_APPROVE');

-- Inserts for payment table
INSERT INTO payment (payment, credit_id, status)
VALUES (1000, 1, 'APPROVED'),
       (500, 2, 'PENDING'),
       (200, 3, 'CANCELED'),
       (1200, 4, 'APPROVED'),
       (750, 5, 'PENDING');
