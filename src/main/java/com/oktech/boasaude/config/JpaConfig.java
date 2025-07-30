package com.oktech.boasaude.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Configuração JPA para a aplicação.
 * Habilita o suporte a auditoria JPA.
 * 
 * @author Arlindo Neto
 */
@EnableJpaAuditing
@Configuration
public class JpaConfig {
    
}
