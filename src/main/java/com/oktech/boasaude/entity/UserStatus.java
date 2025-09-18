package com.oktech.boasaude.entity;

public enum UserStatus {
    ACTIVE, // Usuário normal e ativo
    SUSPENDED, // Usuário suspenso pelo admin, não pode logar
    DELETED, // Usuário deletado (soft delete)
    INACTIVE
}
