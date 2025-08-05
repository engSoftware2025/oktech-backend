package com.oktech.boasaude.service;

/**
 * Interface para serviços de validação.
 * Define métodos para validar CNPJ e email.
 * 
 * @author Lucas do Ouro
 * @version 1.0
 */

public interface ValidationService {

    boolean isValidCnpj(String cnpj);

    boolean isValidEmail(String email);

}
