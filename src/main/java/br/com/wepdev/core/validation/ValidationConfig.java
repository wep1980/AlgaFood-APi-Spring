package br.com.wepdev.core.validation;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/*
   Classe que deixa como padrão o arquivo messages.properties do spring.
 */
@Configuration
public class ValidationConfig {


    /**
     * LocalValidatorFactoryBean -> que faz integração e configuração do BeanValidation com spring
     * Metodo que faz a configuração para mensagens de validação do BeanValidation, deixa como padrão o arquivo messages.properties do spring.
     */
    @Bean
    public LocalValidatorFactoryBean validator(MessageSource messageSource){
       LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
       bean.setValidationMessageSource(messageSource);
       return bean;
    }
}
