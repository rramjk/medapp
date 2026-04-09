package tender.ma.medicalapplied.service.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import tender.ma.medicalapplied.dto.JwtAuthenticationResponse;
import tender.ma.medicalapplied.dto.LoginRequestDto;
import tender.ma.medicalapplied.exceptions.ErrorCode;
import tender.ma.medicalapplied.exceptions.UserAuthorizationException;
import tender.ma.medicalapplied.model.user.User;
import tender.ma.medicalapplied.repository.UserRepository;
import tender.ma.medicalapplied.service.AuthService;
import tender.ma.medicalapplied.util.auth.JwtUtil;

import java.util.Optional;

@Service
@Slf4j
@Validated
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public JwtAuthenticationResponse login(@Valid LoginRequestDto loginRequestDto) {
        Optional<User> userOpt = userRepository.findByEmail(loginRequestDto.getEmail());
        JwtAuthenticationResponse response = new JwtAuthenticationResponse();
        if (userOpt.isPresent() && passwordEncoder.matches(loginRequestDto.getPassword(), userOpt.get().getPassword())) {
            response.setAccessToken(jwtUtil.generateToken(userOpt.get()));
        } else {
            throw new UserAuthorizationException(ErrorCode.AUTHORIZATION_FAILED);
        }
        return response;
    }

    @Override
    public void logout() {
    }

}
