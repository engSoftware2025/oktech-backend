package com.oktech.boasaude.entity;

/**
 * Enum para definir os status de aprovação de produtos no sistema.
 * Controla se um produto está pendente de aprovação, aprovado ou rejeitado.
 * 
 * @author Sistema
 * @version 1.0
 */
public enum ProductStatus {
    PENDING("Pendente de Aprovação"),
    APPROVED("Aprovado"),
    REJECTED("Rejeitado");

    /**
     * Descrição legível do status.
     */
    private final String description;

    /**
     * Construtor para inicializar o status com sua descrição.
     * 
     * @param description Descrição legível do status.
     */
    ProductStatus(String description) {
        this.description = description;
    }

    /**
     * Retorna a descrição do status.
     * 
     * @return Descrição legível do status.
     */
    public String getDescription() {
        return description;
    }
}
