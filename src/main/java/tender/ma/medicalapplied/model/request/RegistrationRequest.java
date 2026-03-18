package tender.ma.medicalapplied.model.request;

import lombok.Data;
import tender.ma.medicalapplied.model.user.GenderTypes;

import java.time.LocalDate;

@Data
public class RegistrationRequest {

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    private GenderTypes gender;

    private String email;

    private String password;
}
