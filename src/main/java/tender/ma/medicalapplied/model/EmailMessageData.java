package tender.ma.medicalapplied.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Component
public class EmailMessageData {
    @Email
    private String email;
    @NotBlank
    private String doctor;
    @NotNull
    private LocalDateTime date;
}
