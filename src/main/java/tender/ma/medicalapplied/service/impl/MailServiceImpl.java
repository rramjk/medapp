package tender.ma.medicalapplied.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import tender.ma.medicalapplied.model.user.User;
import tender.ma.medicalapplied.service.MailService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private static final String PATH_FOR_EMAIL_VERIFY = "/api/users/%s/verify?token=%s";
    private static final String EMAIL_FROM = "medicalappliedservice@gmail.com";
    private static final String EMAIL_VERIFICATION_SUBJECT = "Подтверждение почты";
    private static final String EMAIL_VERIFICATION_PATTERN_FILE_NAME = "email-verification-format-page.html";

    @Value("${server.baseUrl}")
    private String BASE_URL;
    private final JavaMailSender emailSender;
    private final ResourceLoader resourceLoader;

    @Override
    public void sendSimpleMessageToEmailVerified(User user, UUID token) {
        MimeMessage message = getMimeMessage(EMAIL_FROM,
                user.getEmail(),
                EMAIL_VERIFICATION_SUBJECT,
                getHtmlPageForVerifyEmail(user, token));
        emailSender.send(message);
    }


    private String getHtmlPageForVerifyEmail(User user, UUID token) {
        Resource page = resourceLoader.getResource("classpath:" + EMAIL_VERIFICATION_PATTERN_FILE_NAME);
        try (InputStream inputStream = page.getInputStream()) {
            return String.format(getStringPageFromFormatFileInputStream(inputStream),
                    user.getFirstName(),
                    getUrlForEmailVerify(user.getId(), token)
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getUrlForEmailVerify(UUID id, UUID token) {
        return String.format(BASE_URL + PATH_FOR_EMAIL_VERIFY, id, token);
    }

    private MimeMessage getMimeMessage(String from, String to, String subject, String text) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);

            return message;
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private String getStringPageFromFormatFileInputStream(InputStream inputStream) throws IOException {
        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }
}
