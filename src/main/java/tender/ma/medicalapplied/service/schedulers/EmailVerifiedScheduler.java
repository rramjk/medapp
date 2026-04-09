package tender.ma.medicalapplied.service.schedulers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tender.ma.medicalapplied.model.profile.EmailVerification;
import tender.ma.medicalapplied.model.profile.EmailVerificationStatus;
import tender.ma.medicalapplied.repository.EmailVerificationRepository;
import tender.ma.medicalapplied.service.MailService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailVerifiedScheduler {
    private final EmailVerificationRepository repository;
    private final MailService mailService;

    @Scheduled(fixedDelayString = "${service.delay.processEmailsVerifiedDelay}")
    public void processEmailsVerified() {
        LocalDateTime now = LocalDateTime.now();
        repository.deleteExpiredRequestsOrEqualsStatus(now, EmailVerificationStatus.VERIFIED);

        List<EmailVerification> requestsForSend = repository.getEmailVerificationByStatus(now, EmailVerificationStatus.CREATED);
        processEmailVerificationRequests(requestsForSend, now);
    }

    private void processEmailVerificationRequests(List<EmailVerification> emailVerifications, LocalDateTime now) {
        emailVerifications
                .forEach(request -> {
                    try {
                        int updated = repository.updateStatusIfCurrent(
                                request.getId(),
                                EmailVerificationStatus.CREATED,
                                EmailVerificationStatus.PROCESSING,
                                now
                        );

                        if (updated == 1) {
                            processEmailVerificationRequest(request);
                        }
                    } catch (Exception e) {
                        log.error("processEmailVerificationRequests: failed to send verification email for user with id '{}'", request.getUser().getId(), e);
                        request.setStatus(EmailVerificationStatus.FAILED);
                        repository.save(request);
                    }
                });
    }

    private EmailVerification processEmailVerificationRequest(EmailVerification request) {
        mailService.sendSimpleMessageToEmailVerified(request.getUser(), request.getVerificationToken());
        request.setStatus(EmailVerificationStatus.SENT);
        return repository.save(request);
    }
}
