package tender.ma.medicalapplied.config.security.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import tender.ma.medicalapplied.model.user.User;
import tender.ma.medicalapplied.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthUserServiceImpl implements AuthUserService {
    private final UserRepository userRepository;

    @Override
    public Optional<User> getCurrentUser() {
        return getUserFromAuthorizeContext();
    }

    private Optional<User> getUserFromAuthorizeContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> userResult;

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            userResult = Optional.empty();
        } else {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails userDetails) {
                userResult = userRepository.findByEmail(userDetails.getUsername());
            } else if (principal instanceof String username) {
                userResult = userRepository.findByEmail(username);
            } else {
                userResult = Optional.empty();
            }
        }

        return userResult;
    }
}
