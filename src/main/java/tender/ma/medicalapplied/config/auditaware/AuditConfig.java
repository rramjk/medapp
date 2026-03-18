package tender.ma.medicalapplied.config.auditaware;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

@Configuration
public class AuditConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        return new SecurityContextAuditorAware();
    }
}