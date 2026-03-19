package tender.ma.medicalapplied.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import tender.ma.medicalapplied.exceptions.UserAuthorizationException;
import tender.ma.medicalapplied.model.user.User;
import tender.ma.medicalapplied.repository.UserRepository;

import java.util.List;

import static tender.ma.medicalapplied.exceptions.ErrorCode.USER_FOR_AUTHORIZE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UserAuthorizationException {
        String email = username;
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserAuthorizationException(USER_FOR_AUTHORIZE_NOT_FOUND.getErrorMessage(email)));


        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPasswordHash(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName()))
        );
    }
}
