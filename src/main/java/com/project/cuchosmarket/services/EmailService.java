package com.project.cuchosmarket.services;

import com.project.cuchosmarket.models.Order;
import com.project.cuchosmarket.models.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender sender;

    @Autowired
    ThymeleafService thymeleafService;

    @Value("${spring.mail.username}")
    private String setFromEmail;


    public void sendEmailUpdateOrderStatus(User user, String subject, Order order) throws MessagingException {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name()
        );

        helper.setTo(user.getEmail());
        helper.setSubject(subject);

        Map<String, Object> variables = new HashMap<>();
        variables.put("subject", subject);
        variables.put("first_name", user.getFirstName());
        variables.put("order_status", order.getStatus().name());
        variables.put("order_type", order.getType());
        helper.setText(thymeleafService.createContent("order-status-update-email-template.html", variables), true);
        helper.setFrom(setFromEmail);
        sender.send(message);
    }
}
