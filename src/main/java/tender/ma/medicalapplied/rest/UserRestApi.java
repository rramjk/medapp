package tender.ma.medicalapplied.rest;

import org.springframework.web.bind.annotation.*;
import tender.ma.medicalapplied.dto.*;

import java.util.List;
import java.util.UUID;

public interface UserRestApi {
    String BASE_URL = "/api/users";

    @GetMapping
    List<UserDto> getUsers();

    @GetMapping("/{id}")
    UserDto getUserById(@PathVariable UUID id);

    @PostMapping
    UserDto createUser(@RequestBody UserCreateRequestDto userRequestDto);

    @PostMapping("/{id}")
    UserDto updateUser(@PathVariable UUID id, @RequestBody UserRequestDto userRequestDto);

    @DeleteMapping("/{id}")
    UserDto deleteUser(@PathVariable UUID id);

    @GetMapping("/{id}/views")
    List<MedicalDto> getUserMedicalViews(@PathVariable UUID id);

    @DeleteMapping("/{id}/views")
    void deleteUserMedicalViews(@PathVariable UUID id);

    @PostMapping("/{id}/health")
    UserHealthProfileDto createUserHealthProfile(@PathVariable UUID id,
                                                 @RequestBody UserHealthProfileRequestDto userRequestDto);

    @PutMapping("/{id}/health/{healthId}")
    UserHealthProfileDto updateUserHealthProfile(@PathVariable UUID id,
                                                 @PathVariable UUID healthId,
                                                 @RequestBody UserHealthProfileRequestDto userRequestDto);

    @GetMapping("/{id}/health/{healthId}")
    UserHealthProfileDto getUserHealthProfile(@PathVariable UUID id,
                                              @PathVariable UUID healthId);

    @DeleteMapping("/{id}/health/{healthId}")
    UserHealthProfileDto deleteUserHealthProfile(@PathVariable UUID id,
                                                 @PathVariable UUID healthId);

    @PostMapping("/{id}/verify")
    EmailVerificationDto createVerificationUserRequest(@PathVariable UUID id);

    @GetMapping("/{id}/verify")
    EmailVerificationDto verifyUser(@PathVariable UUID id, @RequestParam UUID token);

    @GetMapping("/{id}/verify/status")
    EmailVerificationDto getVerificationUserStatus(@PathVariable UUID id);

    @PostMapping("/{id}/reset-password")
    void resetPassword(@PathVariable UUID id, @RequestBody ResetPasswordRequestDto resetPasswordRequestDto);

}
