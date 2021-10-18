package br.com.wepdev.infrastructure.service.email;

import lombok.extern.slf4j.Slf4j;

/**
 * Classe de email Fake que printa no console o email que seria enviado.(Classe de teste, ambiente de desenvolvimento)
 */
@Slf4j // Anotacão do lombok para passar a informação no console
public class FakeEnvioEmailService extends SmtpEnvioEmailService {


    @Override
    public void enviar(Mensagem mensagem) {
        // Foi necessário alterar o modificador de acesso do método processarTemplate
        // da classe pai para "protected", para poder chamar aqui
        String corpo = processarTemplate(mensagem);

        log.info("[FAKE E-MAIL] Para: {}\n{}", mensagem.getDestinatarios(), corpo);
    }

}

