CREATE TABLE "transaction" (
       transaction_id SERIAL PRIMARY KEY,
       email VARCHAR(255) NOT NULL,
       product_id BIGINT NOT NULL,
       quantity INT ,
       total_price NUMERIC NOT NULL,
       status VARCHAR(50),
       payment_method VARCHAR(50),
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       created_by VARCHAR(255) DEFAULT 'system',
       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       updated_by VARCHAR(255) DEFAULT 'system'
);
