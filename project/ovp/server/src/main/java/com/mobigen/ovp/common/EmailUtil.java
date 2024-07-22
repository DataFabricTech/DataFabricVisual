package com.mobigen.ovp.common;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailUtil {
    public static final String PATTERN_EMAIL = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
    private final TemplateEngine templateEngine;
    private final JavaMailSender mailSender;

    /**
     * 메일 유효성 검증
     *
     * @param emailAddress
     * @return
     */
    public static boolean isValidEmail(String emailAddress) {
        return emailAddress != null && !emailAddress.trim().isEmpty() && Pattern.matches(PATTERN_EMAIL, emailAddress);
    }

    public boolean sendHTMLMail(String to, String subject, Context context, String resource) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

            String html = templateEngine.process(resource, context);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);

            mailSender.send(message);
        } catch(MessagingException me) {
            log.error(me.toString());
            return false;
        }
        return true;
    }
}
