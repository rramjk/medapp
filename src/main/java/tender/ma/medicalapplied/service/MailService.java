package tender.ma.medicalapplied.service;

import tender.ma.medicalapplied.model.EmailMessageData;

public interface MailService {
    void sendSimpleMessage(EmailMessageData emailMessageData);
}
