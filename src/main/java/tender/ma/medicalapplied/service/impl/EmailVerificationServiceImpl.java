package tender.ma.medicalapplied.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tender.ma.medicalapplied.dto.EmailVerificationDto;
import tender.ma.medicalapplied.exceptions.BadRequestException;
import tender.ma.medicalapplied.exceptions.ConflictException;
import tender.ma.medicalapplied.exceptions.ErrorCode;
import tender.ma.medicalapplied.exceptions.NotFoundException;
import tender.ma.medicalapplied.model.mapping.EmailVerificationMapper;
import tender.ma.medicalapplied.model.mapping.UserMapper;
import tender.ma.medicalapplied.model.profile.EmailVerification;
import tender.ma.medicalapplied.model.profile.EmailVerificationStatus;
import tender.ma.medicalapplied.model.user.User;
import tender.ma.medicalapplied.repository.EmailVerificationRepository;
import tender.ma.medicalapplied.repository.UserRepository;
import tender.ma.medicalapplied.service.EmailVerificationService;
import tender.ma.medicalapplied.service.UserService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailVerificationServiceImpl implements EmailVerificationService {
    @Value("${service.expired.emailsVerifiedExpiredAt}")
    private Long expiredAt;
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EmailVerificationMapper mapper;
    private final EmailVerificationRepository repository;

    @Override
    @Transactional
    public EmailVerificationDto createVerificationUserRequest(UUID id) {
        User user = getUserById(id);
        if (user.isEmailVerified()) {
            return getEmailVerifiedStatus();
        } else {
            Optional<EmailVerification> emailVerificationOpt = repository.getEmailVerificationByUser(user);
            if (emailVerificationOpt.isPresent()) {
                if (emailVerificationOpt.get().getExpired().isBefore(LocalDateTime.now())
                        && !emailVerificationOpt.get().getStatus().equals(EmailVerificationStatus.VERIFIED)) {
                    return mapper.toDto(repository.save(refreshEmailVerificationRequest(emailVerificationOpt.get())));
                } else {
                    throw new ConflictException(ErrorCode.REQUEST_FOR_VERIFICATION_ALREADY_EXISTS);
                }
            } else {
                return mapper.toDto(createNewEmailVerificationRequest(user));
            }
        }
    }

    private EmailVerification refreshEmailVerificationRequest(EmailVerification emailVerification) {
        EmailVerification newEmailVerification = getEmptyEmailVerification(emailVerification.getUser(), EmailVerificationStatus.CREATED);
        newEmailVerification.setId(emailVerification.getId());
        return newEmailVerification;
    }

    @Override
    public EmailVerificationDto getVerificationUserStatus(UUID id) {
        User user = getUserById(id);
        if (user.isEmailVerified()) {
            return getEmailVerifiedStatus();
        } else {
            return mapper.toDto(getEmailVerificationIfUnverified(user));
        }
    }

    @Override
    @Transactional
    public EmailVerificationDto verifyUser(UUID id, UUID verificationToken) {
        User user = getUserById(id);
        if (user.isEmailVerified()) {
            return getEmailVerifiedStatus();
        } else {
            return processEmailVerificationToVerify(user, verificationToken);
        }

    }

    private EmailVerificationDto processEmailVerificationToVerify(User user, UUID verificationToken) {
        Optional<EmailVerification> emailVerificationOpt = getEmailVerificationOptByUser(user);
        if (emailVerificationOpt.isEmpty() || emailVerificationOpt.get().getExpired().isBefore(LocalDateTime.now())) {
            throw new NotFoundException(ErrorCode.REQUEST_FOR_VERIFICATION_NOT_FOUND.getErrorMessage(user.getId()));
        } else {
            EmailVerification emailVerification = emailVerificationOpt.get();
            setEmailVerifiedIfTokenCorrect(user, verificationToken, emailVerification);
            return mapper.toDto(verifyRequest(emailVerification));
        }
    }

    private void setEmailVerifiedIfTokenCorrect(User user, UUID verificationToken, EmailVerification emailVerification) {
        if (emailVerification.getVerificationToken().equals(verificationToken)) {
            user.setEmailVerified(true);
            userRepository.save(user);
        } else {
            throw new BadRequestException(ErrorCode.REQUEST_FOR_VERIFICATION_INCORRECT.getErrorMessage(user.getId()));
        }
    }

    private EmailVerificationDto getEmailVerifiedStatus() {
        EmailVerificationDto emailVerificationDto = new EmailVerificationDto();
        emailVerificationDto.setStatus(EmailVerificationStatus.VERIFIED);

        return emailVerificationDto;
    }

    private User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND.getErrorMessage(id)));
    }

    private EmailVerification createNewEmailVerificationRequest(User user) {
        return repository.save(getNewEmailVerificationRequest(user));
    }


    private EmailVerification getNewEmailVerificationRequest(User forUser) {
        return getEmptyEmailVerification(forUser, EmailVerificationStatus.CREATED);
    }

    private EmailVerification getEmailVerificationIfUnverified(User user) {
        return getEmailVerificationOptByUser(user).orElseGet(() -> getUnverifiedEmailVerification(user));
    }

    private EmailVerification getUnverifiedEmailVerification(User forUser) {
        return getEmptyEmailVerification(forUser, EmailVerificationStatus.UNVERIFIED);
    }

    private EmailVerification getEmptyEmailVerification(User forUser, EmailVerificationStatus status) {
        EmailVerification emailVerification = new EmailVerification();
        emailVerification.setUser(forUser);
        emailVerification.setStatus(status);
        emailVerification.setVerificationToken(UUID.randomUUID());
        emailVerification.setExpired(LocalDateTime.now().plusDays(expiredAt));

        return emailVerification;
    }

    private Optional<EmailVerification> getEmailVerificationOptByUser(User user) {
        return repository.getEmailVerificationByUser(user);
    }

    private EmailVerification verifyRequest(EmailVerification request) {
        request.setStatus(EmailVerificationStatus.VERIFIED);
        return repository.save(request);
    }


}
