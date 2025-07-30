CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    auth_provider VARCHAR(20) NOT NULL, -- LOCAL, GOOGLE, FACEBOOK
    provider_id VARCHAR(255),           -- ID do provedor externo (pode ser nulo)
    role VARCHAR(20) NOT NULL,          -- USER, ADMIN, PRODUCTOR
    phone VARCHAR(20),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
