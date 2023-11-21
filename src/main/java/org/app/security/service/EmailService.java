package org.app.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine template;
    
    @Value("${spring.mail.username}")
    String emisor;

    public void enviarPedidoDeConfirmacaoDeCadastro(String destino, String codigo) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");

        Context context = new Context();
        context.setVariable("titulo", "Bienvenido a DOCTORESA");
        context.setVariable("texto", "Necesitamos que confirme su registro, haciendo click en el enlace de abajo");
        context.setVariable("linkConfirmacao", "http://localhost:8080/u/confirmacao/cadastro?codigo=" + codigo);

        String html = template.process("email/confirmacao", context);
        helper.setTo(destino);
        helper.setText(html, true);
        helper.setSubject("Confirmación de Registro");
        helper.setFrom(emisor);

        helper.addInline("logo", new ClassPathResource("/static/image/spring-security.png"));

        mailSender.send(message);
    }

    public void enviarPedidoRedefinicaoSenha(String destino, String verificador) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                "UTF-8");

        Context context = new Context();
        context.setVariable("titulo", "Redefinición de Clave");
        context.setVariable("texto", "Para redefinir su clave user el codigo de verificación solicitado en el formulario.");
        context.setVariable("verificador", verificador);

        String html = template.process("email/confirmacao", context);
        helper.setTo(destino);
        helper.setText(html, true);
        helper.setSubject("Redefinición de Clave");
        helper.setFrom(emisor);

        helper.addInline("logo", new ClassPathResource("/static/image/spring-security.png"));

        mailSender.send(message);
    }
}
