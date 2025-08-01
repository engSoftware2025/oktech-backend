package com.oktech.boasaude.service.impl;

import com.oktech.boasaude.service.ValidationService;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

/** * Implementação do serviço de validação.
 * Fornece métodos para validar CNPJ e email.
 * @author Lucas do Ouro
 * @version 1.0
 */

@Service
public class ValidationServiceImpl implements ValidationService {
    
    private static final String CNPJ_REGEX = "^(\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}|\\d{14})$"; // Formato XX.XXX.XXX/XXXX-XX ou XXXXXXXXXXXXXXX
    private static final Pattern CNPJ_PATTERN = Pattern.compile(CNPJ_REGEX); // Regex para validar CNPJ

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"; // Regex para validar email
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    // Construtor padrão
    @Override
    public boolean isValidCnpj(String cnpj) {
        if (cnpj == null || cnpj.isBlank()) {
            return false;
        }
        return CNPJ_PATTERN.matcher(cnpj).matches();
    }   

    // Construtor padrão
    @Override
    public boolean isValidEmail(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
  
}
