package br.com.wepdev.infrastructure.service.email;

import br.com.wepdev.core.email.EmailProperties;
import br.com.wepdev.domain.service.EnvioEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class SmtpEnvioEmailService implements EnvioEmailService {


    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private EmailProperties emailProperties;


    @Override
    public void enviar(Mensagem mensagem) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8"); // Instancia de ajuda a configurar o MimeMessage, e tb e colocado o encoding
            helper.setTo(mensagem.getDestinatarios().toArray(new String[0])); // Convertendo o Set em um array[0] de Strings
            helper.setFrom(emailProperties.getRemetente());
            helper.setSubject(mensagem.getAssunto());
            helper.setText(mensagem.getCorpo(), true); // texto enviado em HTML, pois permite enviar em negrito, imagens, etc....

            javaMailSender.send(mimeMessage);
        }catch (Exception e){
            throw new EmailException("Não foi possível enviar e-mail", e);

        }


    }
}
