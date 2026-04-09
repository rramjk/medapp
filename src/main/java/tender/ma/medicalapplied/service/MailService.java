package tender.ma.medicalapplied.service;

import tender.ma.medicalapplied.model.user.User;

import java.util.UUID;

public interface MailService {

    void sendSimpleMessageToEmailVerified(User user, UUID token);
}
