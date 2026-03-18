package tender.ma.medicalapplied.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tender.ma.medicalapplied.model.EmailMessageData;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    @Override
    public void sendSimpleMessage(EmailMessageData emailMessageData) {
    }
}
