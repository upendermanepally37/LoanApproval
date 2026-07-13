INSERT INTO customers (
    customer_number, first_name, last_name, email, phone_number,
    date_of_birth, gender, marital_status,
    registration_date, kyc_status, active, deleted,
    created_at, updated_at, version
) VALUES (
    'CUST0001001', 'John', 'Doe', 'john.doe@example.com', '9876543210',
    '1990-05-15', 'MALE', 'S',
    CURRENT_TIMESTAMP, 'NOT_STARTED', TRUE, FALSE,
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0
);
