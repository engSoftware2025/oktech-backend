package com.oktech.boasaude.service.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ValidationServiceImplTest {

    private ValidationServiceImpl validationServiceImpl;

    @BeforeEach
    public void setUp() {
        this.validationServiceImpl = new ValidationServiceImpl();
    }

    @Test
    public void testForValidCnpj() {
        assertTrue(validationServiceImpl.isValidCnpj("12.345.678/0001-95"));

    }

    @Test
    public void testForNotValidCnpj() {
        assertFalse(validationServiceImpl.isValidCnpj("12.345.678/0001-9A"));
    }

    @Test
    public void testForValidEmail() {
        assertTrue(validationServiceImpl.isValidEmail("teste@gmail.com"));
    }

    @Test
    public void testForNotValidEmail() {
        assertFalse(validationServiceImpl.isValidEmail("teste@gmail"));
    }
}
