CREATE TABLE IF NOT EXISTS products (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    shop_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price INT NOT NULL, -- armazenando como inteiro (ex: em centavos)
    category VARCHAR(100),
    stock INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_product_shop FOREIGN KEY (shop_id) REFERENCES shops(id) ON DELETE CASCADE
);

-- Índice para melhorar a busca por loja
CREATE INDEX IF NOT EXISTS idx_products_shop_id ON products(shop_id);

-- Índice para busca por categoria (opcional, útil para filtragem)
CREATE INDEX IF NOT EXISTS idx_products_category ON products(category);
