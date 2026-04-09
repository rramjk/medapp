package tender.ma.medicalapplied.service.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import tender.ma.medicalapplied.dto.ResetPasswordRequestDto;
import tender.ma.medicalapplied.dto.UserCreateRequestDto;
import tender.ma.medicalapplied.dto.UserDto;
import tender.ma.medicalapplied.dto.UserRequestDto;
import tender.ma.medicalapplied.exceptions.BadRequestException;
import tender.ma.medicalapplied.exceptions.ErrorCode;
import tender.ma.medicalapplied.exceptions.NotFoundException;
import tender.ma.medicalapplied.model.mapping.UserMapper;
import tender.ma.medicalapplied.model.user.RoleTypes;
import tender.ma.medicalapplied.model.user.User;
import tender.ma.medicalapplied.repository.RoleRepository;
import tender.ma.medicalapplied.repository.UserRepository;
import tender.ma.medicalapplied.service.UserService;

import java.util.List;
import java.util.UUID;

@Service
@Validated
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserDto> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public UserDto getUserById(UUID id) {
        return userMapper.toDto(getUserByIdOrElseThrow(id));
    }

    @Override
    public UserDto createUser(@Valid UserCreateRequestDto userRequestDto) {
        validateRegisterRequest(userRequestDto);
        return userMapper.toDto(userRepository.save(processCreateRequestToUser(userRequestDto)));
    }

    @Override
    public UserDto updateUser(UUID id, @Valid UserRequestDto userRequestDto) {
        User user = getUserByIdOrElseThrow(id);
        validateRegisterRequestIfNeed(user, userRequestDto);
        User resultUser = userMapper.toUpdatedEntity(userMapper.toEntityFromRequestDto(userRequestDto), user);
        if (!user.getEmail().equals(resultUser.getEmail())) {
            resultUser.setEmailVerified(false);
        }

        return userMapper.toDto(userRepository.save(resultUser));
    }

    @Override
    public UserDto deleteUser(UUID id) {
        User user = getUserByIdOrElseThrow(id);
        userRepository.delete(user);
        return userMapper.toDto(user);
    }

    @Override
    public void resetPassword(UUID id, ResetPasswordRequestDto resetPasswordRequestDto) {
        User user = getUserByIdOrElseThrow(id);
        if (passwordIsCorrect(user, resetPasswordRequestDto.getOldPassword())) {
            user.setPasswordHash(passwordEncoder.encode(resetPasswordRequestDto.getNewPassword()));
            userRepository.save(user);
        }
    }

    private boolean passwordIsCorrect(User user, String oldPassword) {
        if (passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
            return true;
        } else {
            throw new BadRequestException(ErrorCode.AUTHORIZATION_FAILED);
        }
    }

    private User processCreateRequestToUser(UserCreateRequestDto userRequestDto) {
        User user = userMapper.toEntityFromRequestDto(userRequestDto);
        user.setPasswordHash(passwordEncoder.encode(userRequestDto.getPassword()));
        setDefaultRole(user);
        return user;
    }

    private void validateRegisterRequestIfNeed(User user, UserRequestDto request) {
        if (!user.getEmail().equals(request.getEmail())) {
            validateRegisterRequest(request);
        }
    }

    private void validateRegisterRequest(UserRequestDto request) {
        String email = request.getEmail();
        if (!StringUtils.isBlank(email) && userRepository.findByEmail(email).isPresent()) {
            throw new BadRequestException(ErrorCode.USER_WITH_THIS_EMAIL_EXISTS.getErrorMessage(email));
        }
    }

    private void setDefaultRole(User user) {
        user.setRole(roleRepository.findByName(RoleTypes.USER));
    }

    private User getUserByIdOrElseThrow(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND.getErrorMessage(id)));
    }

}
