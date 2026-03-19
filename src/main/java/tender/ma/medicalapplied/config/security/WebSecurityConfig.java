package tender.ma.medicalapplied.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    SecurityFilterChain springSecurityFilterChain(HttpSecurity http) {
        http
                .authorizeHttpRequests((request) -> request
                                .requestMatchers("/v1/home", "/v1/login", "/v1/register").permitAll()
                                .anyRequest().authenticated()
                        )
                .formLogin((form) -> form
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .loginProcessingUrl("/v1/login")
                        .loginPage("/v1/login")
                        .permitAll()
                )
                .logout(LogoutConfigurer::permitAll);

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
