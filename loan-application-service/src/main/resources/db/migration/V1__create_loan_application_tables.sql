-- Create loan_applications table
CREATE TABLE IF NOT EXISTS loan_applications (
    id BIGSERIAL PRIMARY KEY,
    application_number VARCHAR(50) UNIQUE NOT NULL,
    customer_id BIGINT NOT NULL,
    loan_product_id BIGINT NOT NULL,
    requested_amount DECIMAL(19,2) NOT NULL,
    approved_amount DECIMAL(19,2),
    requested_tenure_months INTEGER NOT NULL,
    approved_tenure_months INTEGER,
    interest_rate DECIMAL(5,2),
    processing_fee DECIMAL(5,2),
    emi DECIMAL(19,2),
    repayment_start_date DATE,
    repayment_end_date DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    eligibility_status VARCHAR(20),
    rejection_reason VARCHAR(1000),
    remarks VARCHAR(1000),
    fraud_flag BOOLEAN NOT NULL DEFAULT FALSE,
    fraud_remarks VARCHAR(500),
    submitted_at TIMESTAMP,
    approved_at TIMESTAMP,
    rejected_at TIMESTAMP,
    disbursed_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    version BIGINT
);

-- Create indexes for loan_applications table
CREATE INDEX IF NOT EXISTS idx_loan_app_customer ON loan_applications(customer_id);
CREATE INDEX IF NOT EXISTS idx_loan_app_product ON loan_applications(loan_product_id);
CREATE INDEX IF NOT EXISTS idx_loan_app_status ON loan_applications(status);
CREATE INDEX IF NOT EXISTS idx_loan_app_number ON loan_applications(application_number);

-- Create application_documents table
CREATE TABLE IF NOT EXISTS application_documents (
    id BIGSERIAL PRIMARY KEY,
    loan_application_id BIGINT NOT NULL REFERENCES loan_applications(id) ON DELETE CASCADE,
    document_type VARCHAR(50) NOT NULL,
    document_name VARCHAR(255) NOT NULL,
    document_path VARCHAR(500),
    document_number VARCHAR(50),
    document_format VARCHAR(20),
    file_size BIGINT,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    verification_remarks VARCHAR(500),
    verified_at TIMESTAMP,
    verified_by VARCHAR(100),
    uploaded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    uploaded_by VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

-- Create indexes for application_documents table
CREATE INDEX IF NOT EXISTS idx_app_doc_application ON application_documents(loan_application_id);
CREATE INDEX IF NOT EXISTS idx_app_doc_type ON application_documents(document_type);
CREATE INDEX IF NOT EXISTS idx_app_doc_status ON application_documents(status);

-- Create eligibility_checks table
CREATE TABLE IF NOT EXISTS eligibility_checks (
    id BIGSERIAL PRIMARY KEY,
    loan_application_id BIGINT NOT NULL REFERENCES loan_applications(id) ON DELETE CASCADE,
    check_type VARCHAR(50) NOT NULL,
    check_name VARCHAR(100) NOT NULL,
    check_description VARCHAR(500),
    actual_value DECIMAL(19,2),
    required_value DECIMAL(19,2),
    string_value VARCHAR(100),
    status VARCHAR(20) NOT NULL,
    remarks VARCHAR(500),
    passed BOOLEAN NOT NULL DEFAULT FALSE,
    checked_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    checked_by VARCHAR(100)
);

-- Create indexes for eligibility_checks table
CREATE INDEX IF NOT EXISTS idx_eligibility_check_application ON eligibility_checks(loan_application_id);
CREATE INDEX IF NOT EXISTS idx_eligibility_check_type ON eligibility_checks(check_type);

-- Create application_status_history table
CREATE TABLE IF NOT EXISTS application_status_history (
    id BIGSERIAL PRIMARY KEY,
    loan_application_id BIGINT NOT NULL REFERENCES loan_applications(id) ON DELETE CASCADE,
    from_status VARCHAR(20) NOT NULL,
    to_status VARCHAR(20) NOT NULL,
    remarks VARCHAR(500),
    changed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    changed_by VARCHAR(100)
);

-- Create indexes for application_status_history table
CREATE INDEX IF NOT EXISTS idx_status_history_application ON application_status_history(loan_application_id);
CREATE INDEX IF NOT EXISTS idx_status_history_changed_at ON application_status_history(changed_at);
