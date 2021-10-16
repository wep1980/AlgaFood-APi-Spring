package br.com.wepdev.infrastructure.service.email;

import br.com.wepdev.core.email.EmailProperties;
import br.com.wepdev.domain.service.EnvioEmailService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.internet.MimeMessage;

/**
 * Classe que implementa o envio de email.
 */
@Service
public class SmtpEnvioEmailService implements EnvioEmailService {


    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private EmailProperties emailProperties;

    @Autowired
    private Configuration freemarkerConfig;


    @Override
    public void enviar(Mensagem mensagem) {
        try {
            String corpo = processarTemplate(mensagem); // processa a mensagem do corpo

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8"); // Instancia de ajuda a configurar o MimeMessage, e tb e colocado o encoding
            helper.setTo(mensagem.getDestinatarios().toArray(new String[0])); // Convertendo o Set em um array[0] de Strings
            helper.setFrom(emailProperties.getRemetente());
            helper.setSubject(mensagem.getAssunto());
            helper.setText(corpo, true); // texto enviado em HTML, pois permite enviar em negrito, imagens, etc....

            javaMailSender.send(mimeMessage);
        }catch (Exception e){
            throw new EmailException("Não foi possível enviar e-mail", e);

        }
    }

    /**
     * Metodo que ultiliza a biblioteca apache freemarker para unir o template html do corpo do email e o objeto java em um unico arquivo html de saida
     * @param mensagem
     * @return
     */
    private String processarTemplate(Mensagem mensagem){
        try {
            Template templete = freemarkerConfig.getTemplate(mensagem.getCorpo());

            /**
             * Passa nos parametros do metodo o template, e o objeto java que vai ser usado para gerar o html dinamicamente, o freemarker vai unir os 2 parametros
             * e gerar um unico html, que sera o corpo do email
             */
            return FreeMarkerTemplateUtils.processTemplateIntoString(templete, mensagem.getVariaveis());

        }catch (Exception e){
            throw new EmailException("Não foi possível montar o template do e-mail", e);

        }



    }









}
