package tender.ma.medicalapplied.service;

import tender.ma.medicalapplied.dto.EmailVerificationDto;

import java.util.UUID;

public interface EmailVerificationService {
    EmailVerificationDto createVerificationUserRequest(UUID id);

    EmailVerificationDto getVerificationUserStatus(UUID id);

    EmailVerificationDto verifyUser(UUID id, UUID verificationToken);
}
