package tender.ma.medicalapplied.exceptions;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserAuthorizationException extends UsernameNotFoundException {
    public UserAuthorizationException(String message) {
        super(message);
    }
}
