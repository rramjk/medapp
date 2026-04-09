package tender.ma.medicalapplied.service;

import jakarta.validation.Valid;
import tender.ma.medicalapplied.dto.JwtAuthenticationResponse;
import tender.ma.medicalapplied.dto.LoginRequestDto;

public interface AuthService {

    JwtAuthenticationResponse login(@Valid LoginRequestDto loginRequestDto);

    void logout();
}
