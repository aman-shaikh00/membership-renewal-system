package com.membership.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendExpiryReminder(
            String email,
            String memberName,
            String expiryDate
    ) {

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("Membership Expiry Reminder");

        message.setText(
                "Hello " + memberName + ",\n\n"
                        + "Your membership is going to expire on "
                        + expiryDate + ".\n\n"
                        + "Please renew your membership to continue "
                        + "enjoying our services.\n\n"
                        + "Thank you."
        );

        mailSender.send(message);
    }
}