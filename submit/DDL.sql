-- miniDooray AccountAPI DDL
-- MySQL 8.0 / MariaDB 10.5+

-- ============================================================
-- account
-- ============================================================
CREATE TABLE IF NOT EXISTS account
(
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    user_id       VARCHAR(30)  NOT NULL,
    user_password VARCHAR(100) NOT NULL,
    user_email    VARCHAR(50)  NOT NULL,
    user_name     VARCHAR(50)  NOT NULL,
    status        VARCHAR(10)  NOT NULL DEFAULT 'ACTIVE',
    created_at    DATETIME,

    PRIMARY KEY (id),
    UNIQUE KEY uk_account_user_id (user_id),
    CONSTRAINT chk_account_status CHECK (status IN ('ACTIVE', 'DORMANT'))
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ============================================================
-- deleted_accounts  (탈퇴 계정 이력)
-- ============================================================
CREATE TABLE IF NOT EXISTS deleted_accounts
(
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    user_id       VARCHAR(30)  NOT NULL,
    user_password VARCHAR(100) NOT NULL,
    user_email    VARCHAR(50)  NOT NULL,
    user_name     VARCHAR(50)  NOT NULL,
    deleted_at    DATETIME,

    PRIMARY KEY (id),
    UNIQUE KEY uk_deleted_accounts_user_id (user_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;