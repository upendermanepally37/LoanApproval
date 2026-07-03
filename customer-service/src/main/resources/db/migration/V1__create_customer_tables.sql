-- Create customers table
CREATE TABLE IF NOT EXISTS customers (
    id BIGSERIAL PRIMARY KEY,
    customer_number VARCHAR(50) UNIQUE NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    middle_name VARCHAR(100),
    email VARCHAR(100) UNIQUE NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    alternate_phone_number VARCHAR(20),
    date_of_birth DATE NOT NULL,
    gender VARCHAR(10) NOT NULL,
    marital_status VARCHAR(1) NOT NULL,
    nationality VARCHAR(100),
    pan_number VARCHAR(50),
    aadhaar_number VARCHAR(50),
    registration_date TIMESTAMP NOT NULL,
    kyc_status VARCHAR(20) NOT NULL DEFAULT 'NOT_STARTED',
    kyc_remarks VARCHAR(500),
    risk_profile VARCHAR(20),
    credit_score VARCHAR(10),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    version BIGINT
);

-- Create indexes for customers table
CREATE INDEX IF NOT EXISTS idx_customer_email ON customers(email);
CREATE INDEX IF NOT EXISTS idx_customer_phone ON customers(phone_number);
CREATE INDEX IF NOT EXISTS idx_customer_kyc_status ON customers(kyc_status);
CREATE INDEX IF NOT EXISTS idx_customer_active ON customers(active);

-- Create addresses table
CREATE TABLE IF NOT EXISTS addresses (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL REFERENCES customers(id) ON DELETE CASCADE,
    address_type VARCHAR(20) NOT NULL,
    address_line1 VARCHAR(500),
    address_line2 VARCHAR(500),
    city VARCHAR(100),
    state VARCHAR(100),
    postal_code VARCHAR(20),
    country VARCHAR(100),
    is_verified BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

-- Create employment_details table
CREATE TABLE IF NOT EXISTS employment_details (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL REFERENCES customers(id) ON DELETE CASCADE,
    employment_type VARCHAR(20) NOT NULL,
    employer_name VARCHAR(200),
    employer_address VARCHAR(500),
    designation VARCHAR(20),
    department VARCHAR(20),
    monthly_income DECIMAL(19,2),
    annual_income DECIMAL(19,2),
    employment_id VARCHAR(20),
    employment_start_date DATE,
    employment_end_date DATE,
    work_location VARCHAR(500),
    industry VARCHAR(20),
    current_employment BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

-- Create documents table
CREATE TABLE IF NOT EXISTS documents (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL REFERENCES customers(id) ON DELETE CASCADE,
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

-- Create indexes for documents table
CREATE INDEX IF NOT EXISTS idx_document_customer ON documents(customer_id);
CREATE INDEX IF NOT EXISTS idx_document_type ON documents(document_type);
CREATE INDEX IF NOT EXISTS idx_document_status ON documents(status);
