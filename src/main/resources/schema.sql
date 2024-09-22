-- Brand 테이블
CREATE TABLE brand
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE
);

-- Product 테이블
CREATE TABLE product
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    category VARCHAR(40) NOT NULL,
    brand_id BIGINT      NOT NULL,
    price    INT         NOT NULL,
    CONSTRAINT fk_brand FOREIGN KEY (brand_id) REFERENCES brand (id)
);
