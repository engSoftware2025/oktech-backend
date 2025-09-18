-- Adiciona coluna status na tabela products para controle de aprovação
ALTER TABLE products ADD COLUMN status VARCHAR(20) DEFAULT 'PENDING';

-- Atualiza produtos existentes para status aprovado (assumindo que produtos já existentes devem ser aprovados)
UPDATE products SET status = 'APPROVED' WHERE status IS NULL;

-- Adiciona constraint para garantir que apenas valores válidos sejam aceitos
ALTER TABLE products ADD CONSTRAINT chk_product_status 
CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED'));

-- Cria índice para melhorar performance nas consultas por status
CREATE INDEX IF NOT EXISTS idx_products_status ON products(status);
