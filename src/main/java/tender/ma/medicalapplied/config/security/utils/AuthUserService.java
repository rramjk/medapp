package tender.ma.medicalapplied.config.security.utils;

import tender.ma.medicalapplied.model.user.User;

import java.util.Optional;

public interface AuthUserService {
    Optional<User> getCurrentUser();
}
