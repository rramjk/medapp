package tender.ma.medicalapplied.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import tender.ma.medicalapplied.dto.JwtAuthenticationResponse;
import tender.ma.medicalapplied.dto.LoginRequestDto;

public interface AuthRestApi {
    String BASE_URL = "/api";

    @PostMapping("/login")
    JwtAuthenticationResponse login(@RequestBody LoginRequestDto loginRequestDto);

    @PostMapping("/logout")
    void logout();
}
