package tender.ma.medicalapplied.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import tender.ma.medicalapplied.model.EmailMessageData;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private final JavaMailSender emailSender;

    public void sendSimpleMessage(EmailMessageData emailMessageData) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("medicalappliedservice@gmail.com");
            helper.setTo(emailMessageData.getEmail());
            helper.setSubject("Запись к врачу");
            helper.setText(getHtmlPage(emailMessageData), true);

            emailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    private String getHtmlPage(EmailMessageData data) {
        return String.format("""
        <div style="max-width:500px;margin:30px auto;padding:30px 30px 20px 30px;
                    border-radius:16px;background:#f7fafd;border:1px solid #dde5ed;
                    font-family:Arial,sans-serif;box-shadow:0 6px 32px rgba(60,90,130,0.08);">
            <h1 style="font-size:26px;margin-bottom:16px;color:#0a4c80;text-align:center;">
                Запись к врачу
            </h1>
            <h2 style="font-size:18px;margin-bottom:12px;color:#1984b7;text-align:center;font-weight:400;">
                Уважаемый %s!
            </h2>
            <div style="padding:16px 0 0 0;font-size:17px;color:#202124;">
                <p style="margin-bottom:8px;">
                    <span style="color:#888;">Доктор:</span>
                    <b style="color:#114476;">%s</b>
                </p>
                <p style="margin-bottom:16px;">
                    <span style="color:#888;">Дата:</span>
                    <b style="color:#114476;">%s</b>
                </p>
            </div>
            <div style="font-size:15px;color:#888;text-align:center;padding-top:12px;">
                Если вы не совершали запись, проигнорируйте это письмо.<br>
                С уважением,<br>
                Команда Medical Applied<br>
                Контакты: medicalappliedservice@gmail.com
            </div>
        </div>
        """, data.getEmail(), data.getDoctor(), data.getDate().toString());
    }
}
