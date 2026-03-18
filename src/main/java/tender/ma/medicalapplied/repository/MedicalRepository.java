package tender.ma.medicalapplied.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tender.ma.medicalapplied.model.Medical;

import java.util.UUID;

@Repository
public interface MedicalRepository extends JpaRepository<Medical, UUID> {
}
