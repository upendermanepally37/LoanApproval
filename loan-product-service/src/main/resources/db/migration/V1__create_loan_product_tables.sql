-- Create loan_products table
CREATE TABLE IF NOT EXISTS loan_products (
    id BIGSERIAL PRIMARY KEY,
    product_code VARCHAR(50) UNIQUE NOT NULL,
    product_name VARCHAR(100) NOT NULL,
    loan_type VARCHAR(20) NOT NULL,
    description VARCHAR(1000),
    interest_rate DECIMAL(5,2) NOT NULL,
    processing_fee DECIMAL(5,2) NOT NULL,
    processing_fee_percentage DECIMAL(5,2) NOT NULL,
    min_tenure_months INTEGER NOT NULL,
    max_tenure_months INTEGER NOT NULL,
    min_loan_amount DECIMAL(19,2) NOT NULL,
    max_loan_amount DECIMAL(19,2) NOT NULL,
    min_monthly_salary DECIMAL(19,2) NOT NULL,
    min_credit_score INTEGER NOT NULL,
    min_age INTEGER NOT NULL,
    max_age INTEGER NOT NULL,
    prepayment_allowed BOOLEAN NOT NULL DEFAULT TRUE,
    prepayment_fee DECIMAL(5,2),
    part_payment_allowed BOOLEAN NOT NULL DEFAULT FALSE,
    debt_to_income_ratio DECIMAL(5,2) NOT NULL,
    repayment_frequency VARCHAR(20),
    eligibility_criteria VARCHAR(1000),
    required_documents VARCHAR(1000),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    effective_from TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    effective_to TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    version BIGINT
);

-- Create indexes for loan_products table
CREATE INDEX IF NOT EXISTS idx_loan_product_type ON loan_products(loan_type);
CREATE INDEX IF NOT EXISTS idx_loan_product_active ON loan_products(active);
CREATE INDEX IF NOT EXISTS idx_loan_product_effective ON loan_products(effective_from, effective_to);

-- Create eligibility_rules table
CREATE TABLE IF NOT EXISTS eligibility_rules (
    id BIGSERIAL PRIMARY KEY,
    loan_product_id BIGINT NOT NULL REFERENCES loan_products(id) ON DELETE CASCADE,
    rule_name VARCHAR(100) NOT NULL,
    rule_description VARCHAR(500),
    rule_type VARCHAR(50) NOT NULL,
    rule_expression VARCHAR(500),
    min_value DECIMAL(19,2),
    max_value DECIMAL(19,2),
    string_value VARCHAR(100),
    priority INTEGER NOT NULL DEFAULT 0,
    mandatory BOOLEAN NOT NULL DEFAULT TRUE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

-- Create indexes for eligibility_rules table
CREATE INDEX IF NOT EXISTS idx_eligibility_rule_product ON eligibility_rules(loan_product_id);
CREATE INDEX IF NOT EXISTS idx_eligibility_rule_type ON eligibility_rules(rule_type);
CREATE INDEX IF NOT EXISTS idx_eligibility_rule_priority ON eligibility_rules(priority);
