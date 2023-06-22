package com.project.cuchosmarket.services;

import com.project.cuchosmarket.enums.OrderStatus;
import com.project.cuchosmarket.enums.OrderType;
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


    public void sendEmailUpdateOrderStatus(User user, Order order) throws MessagingException {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name()
        );

        String subject = setSubjectForUpdateOrderStatus(order.getStatus(), order.getType());

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

    private String setSubjectForUpdateOrderStatus(OrderStatus orderStatus, OrderType orderType) {
        switch (orderStatus) {
            case PENDING:
                return "Tu pedido ha sido realizado con éxito";
            case PREPARING:
                return "Tu pedido está siendo preparado";
            case CANCELLED:
                return "Tu pedido ha sido cancelado";
            case DELIVERED:
                if (orderType.equals(OrderType.PICK_UP))
                    return "Tu pedido está pronto para ser retirado";
                else if (orderType.equals(OrderType.DELIVERY))
                    return "Tu pedido ya está en camino";
            default:
                return "Este mensaje es enviado por Cucho's Market";
        }
    }

    public void sendResetTokenEmail(String token, User user) throws MessagingException {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name()
        );

        String resetPasswordUrl = "http://localhost:8080/doc/swagger-ui/index.html?token=" + token;  //Modificar Url para redirigir a la pag

        helper.setTo(user.getEmail());
        helper.setFrom(setFromEmail);
        helper.setSubject("Solicitud para restablecer contraseña");

        Map<String, Object> variables = new HashMap<>();
        variables.put("resetPasswordUrl", resetPasswordUrl);
        variables.put("first_name", user.getFirstName());
        helper.setText(thymeleafService.createContent("reset-password-email-template.html", variables), true);
        sender.send(message);
    }

}
