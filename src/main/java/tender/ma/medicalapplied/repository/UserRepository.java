package tender.ma.medicalapplied.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tender.ma.medicalapplied.model.user.User;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User,Integer> {
    Optional<User> findByEmail(String email);
}
