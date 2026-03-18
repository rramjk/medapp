package tender.ma.medicalapplied.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tender.ma.medicalapplied.model.user.User;

@Repository
public interface UserRepository extends CrudRepository<User,Integer> {
}
