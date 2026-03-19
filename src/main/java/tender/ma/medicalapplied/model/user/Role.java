package tender.ma.medicalapplied.model.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "ma_roles_db")
@NoArgsConstructor
public class Role extends AuditableEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private RoleTypes name;

    @Column(name = "description")
    private String description;
}
