package tender.ma.medicalapplied.service;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import tender.ma.medicalapplied.dto.UserHealthProfileDto;
import tender.ma.medicalapplied.dto.UserHealthProfileRequestDto;

import java.util.UUID;

@Validated
public interface UserHealthProfileService {

    UserHealthProfileDto createUserHealthProfile(UUID id, @Valid UserHealthProfileRequestDto userRequestDto);

    UserHealthProfileDto updateUserHealthProfile(UUID id, UUID healthId, @Valid UserHealthProfileRequestDto userRequestDto);

    UserHealthProfileDto getUserHealthProfile(UUID id, UUID healthId);

    UserHealthProfileDto deleteUserHealthProfile(UUID id, UUID healthId);
}
