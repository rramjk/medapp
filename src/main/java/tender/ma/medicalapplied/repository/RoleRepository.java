package tender.ma.medicalapplied.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tender.ma.medicalapplied.model.user.Role;
import tender.ma.medicalapplied.model.user.RoleTypes;

@Repository
public interface RoleRepository extends CrudRepository<Role,Integer> {
    Role findByName(RoleTypes name);
}
