package br.com.wepdev.core.email;

import br.com.wepdev.domain.service.EnvioEmailService;
import br.com.wepdev.infrastructure.service.email.FakeEnvioEmailService;
import br.com.wepdev.infrastructure.service.email.SandboxEnvioEmailService;
import br.com.wepdev.infrastructure.service.email.SmtpEnvioEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailConfig {


    @Autowired
    private EmailProperties emailProperties;



    @Bean
    public EnvioEmailService envioEmailService() {
        // Acho melhor usar switch aqui do que if/else if
        switch (emailProperties.getImpl()) {
            case FAKE:
                return new FakeEnvioEmailService();
            case SMTP:
                return new SmtpEnvioEmailService();
            case SANDBOX:
                return new SandboxEnvioEmailService();
            default:
                return null;
        }
    }
}
