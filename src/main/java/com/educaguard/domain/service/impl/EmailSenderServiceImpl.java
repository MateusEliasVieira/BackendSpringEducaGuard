package com.educaguard.domain.service.impl;

import com.educaguard.domain.domainException.BusinessRulesException;
import com.educaguard.domain.model.User;
import com.educaguard.domain.repository.UserRepository;
import com.educaguard.domain.service.EmailSenderService;
import com.educaguard.security.jwt.JwtToken;
import com.educaguard.utils.Feedback;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {

    @Value("${spring.mail.username}")
    private String MY_GMAIL;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UserRepository repository;

    @Override
    public void sendEmail(String to, String token) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

            // Define o destinatário, assunto e o conteúdo HTML do e-mail
            helper.setTo(to);
            helper.setSubject("Confirmação de conta");

            // Codificando a string para Base64
            byte[] encodedBytes = Base64.getEncoder().encode(token.getBytes());

            // Convertendo os bytes codificados de volta para uma string
            String tokenBase64String = new String(encodedBytes);

            // Use HTML para criar um link estilizado
            String htmlContent = "<h1>Por favor, clique no link abaixo para confirmar sua conta!</h1>" +
                    "<a href=\"https://educaguard.up.railway.app/api/email/confirmation/" + tokenBase64String + "\" style=\"color: white; text-decoration: none; font-size:25px; padding:10px; background-color:dodgerblue; border-radius:10px; display:block; text-align:center;\">Confirmar!</a>";
            helper.setText(htmlContent, true);

            javaMailSender.send(mimeMessage);
        } catch (MailException | MessagingException e) {
            throw new BusinessRulesException(Feedback.ERROR_SEND_CONF_EMAIL + to);
        }

    }

    @Override
    public void recoverAccount(String to) {
        try {
            User user = repository.findUserByEmail(to);

            if (user != null) {
                // exist user

                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

                // Define o destinatário, assunto e o conteúdo HTML do e-mail
                helper.setTo(to);
                helper.setSubject("Educaguard");

                String newToken = JwtToken.generateTokenJWT(user); // gerar novo token
                user.setToken(newToken); // atualizar token do usuario
                User userUpdateWithNewToken = repository.save(user); // salvar mudanças
                String token = userUpdateWithNewToken.getToken(); // obter o token atualizado

                // Use HTML para criar um link estilizado
                String htmlContent = "<h4>Olá usuário, para recuperar sua conta, basta clicar no botão abaixo. Att equipe educaguard.</h4>" +
                        "<a href=\"https://educaguard.vercel.app/?token=" + token + "\" style=\"color: white; text-decoration: none; font-size:25px; padding:10px; background-color:dodgerblue; border-radius:10px; display:block; text-align:center;\">Recuperar minha conta!</a>";
                helper.setText(htmlContent, true);

                javaMailSender.send(mimeMessage);

            } else {
                // not exist user
                throw new BusinessRulesException(Feedback.EMPTY_USER);
            }
        } catch (MailException | MessagingException e) {
            throw new BusinessRulesException(Feedback.ERROR_ACCOUNT_RECOVER + to);
        }

    }


}
