package tender.ma.medicalapplied.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Table(name = "ma_medicals_db")
@Entity
public class Medical {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", columnDefinition = "UUID")
    private UUID id;

    @Column(name = "country_en")
    private String countryEn;

    @Column(name = "country_ru")
    private String countryRu;

    @Column(name = "type")
    private String type;

    @Column(name = "name")
    private String name;

    @Column(name = "active_ingredient")
    private String activeIngredient;

    @Column(name = "description")
    private String description;

    @Column(name = "indications")
    private String indications;

    @Column(name = "contraindications")
    private String contraindications;

    @Column(name = "dosing")
    private String dosing;

    @Column(name = "kidney_friendly")
    private Boolean kidneyFriendly;

    @Column(name = "pregnant_friendly")
    private Boolean pregnantFriendly;

    @Column(name = "breastfed_friendly")
    private Boolean breastfedFriendly;

    @Column(name = "liver_friendly")
    private Boolean liverFriendly;

    @Column(name = "child_friendly")
    private Boolean childFriendly;

    @Column(name = "stomach_friendly")
    private Boolean stomachFriendly;
}
