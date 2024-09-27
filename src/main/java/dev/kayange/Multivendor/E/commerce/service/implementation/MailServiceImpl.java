package dev.kayange.Multivendor.E.commerce.service.implementation;

import dev.kayange.Multivendor.E.commerce.entity.notification.Notification;
import dev.kayange.Multivendor.E.commerce.enumeration.EmailTemplate;
import dev.kayange.Multivendor.E.commerce.exception.ApiException;
import dev.kayange.Multivendor.E.commerce.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${application.mail.dev}")
    private String emailAddress;

    @Override
    @Async
    public void sendMail(String to, String username, EmailTemplate emailTemplate, Map<?,?> parameters){
        String templateName;
        if(emailTemplate == null) {
            templateName = "activate_account";
        }else {
            templateName = emailTemplate.getTemplateName();
        }

        var confirmationUrl = parameters.get("confirmationUrl");
        String subject = (String) parameters.get("subject");
        var activationCode = parameters.get("activationCode");

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED, StandardCharsets.UTF_8.name());
            Map<String, Object> properties = Map.of("username",username, "confirmationUrl", confirmationUrl, "activationCode", activationCode);
            Context context = new Context();
            context.setVariables(properties);

            var template = templateEngine.process(templateName, context);
            helper.setFrom(emailAddress);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(template, true);
            mailSender.send(mimeMessage);
        }catch (MessagingException e){
            log.error(e.getMessage());
            throw new ApiException("Failed to Send Email");
        }catch (Exception e){
            throw new ApiException("Something went wrong");
        }
    }

    @Override
    public void sendNotification(Notification notification) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(String.format("%s Email from Shoppers",notification.getType().name()));
        message.setTo(notification.getRecipient().getEmail());
        message.setFrom(emailAddress);
        message.setText(notification.getMessage());

        mailSender.send(message);


        // TODO: 03/09/2024 This approach is to be used!

        /*Context context = new Context();
        context.setVariable("body", notification.getMessage());

        String to = notification.getRecipient().getEmail();


        String templateName;
        switch (notification.getType()){
            case ORDER -> templateName = "order_template";
            case ALERTS -> templateName = "alerts_template";
            case PAYMENT -> templateName = "payment_template";
            case SHIPMENT -> templateName = "shipping_template";
            case PROMOTIONAL -> templateName = "promotional_template";
            default -> templateName = "";
        }

        String content = templateEngine.process(templateName, context);

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setText(content, true);
            helper.setTo(to);
            helper.setSubject(notification.getMessage());
            helper.setFrom(emailAddress);

            mailSender.send(mimeMessage);

        }catch (MessagingException e) {
            throw new ApiException("Failed to process email");
        }catch (Exception e) {
            throw new ApiException("Failed to send Email. Something went wrong");
        }*/
    }
}
