
-- accounts 테이블
CREATE TABLE IF NOT EXISTS accounts (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  owner_name VARCHAR(255) NOT NULL,
  balance DECIMAL(18,2) NOT NULL,
  account_number VARCHAR(20) NOT NULL,
  deleted CHAR(1) NOT NULL,
  created_at DATETIME(6) NOT NULL,
  updated_at DATETIME(6) NULL,
  CONSTRAINT uk_accounts_account_number UNIQUE (account_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- transactions 테이블
CREATE TABLE IF NOT EXISTS transactions (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  account_id BIGINT NOT NULL,
  type VARCHAR(20) NOT NULL,
  amount DECIMAL(18,2) NOT NULL,
  fee BIGINT NOT NULL,
  created_at DATETIME(6) NOT NULL,
  related_account_id BIGINT NULL,
  CONSTRAINT fk_transactions_account
    FOREIGN KEY (account_id) REFERENCES accounts(id)
      ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 조회 성능 인덱스
CREATE INDEX idx_tx_account_created_at ON transactions(account_id, created_at DESC);
CREATE INDEX idx_tx_type_created_at ON transactions(type, created_at DESC);
