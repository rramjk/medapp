package tender.ma.medicalapplied.service;

import jakarta.validation.Valid;
import tender.ma.medicalapplied.dto.ResetPasswordRequestDto;
import tender.ma.medicalapplied.dto.UserCreateRequestDto;
import tender.ma.medicalapplied.dto.UserDto;
import tender.ma.medicalapplied.dto.UserRequestDto;

import java.util.List;
import java.util.UUID;

public interface UserService {

    List<UserDto> getUsers();

    UserDto getUserById(UUID id);

    UserDto createUser(@Valid UserCreateRequestDto userRequestDto);

    UserDto updateUser(UUID id, @Valid UserRequestDto userRequestDto);

    UserDto deleteUser(UUID id);

    void resetPassword(UUID id, ResetPasswordRequestDto resetPasswordRequestDto);
}
