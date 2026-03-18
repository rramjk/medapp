package tender.ma.medicalapplied.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tender.ma.medicalapplied.model.request.RegistrationRequest;
import tender.ma.medicalapplied.model.user.RoleTypes;
import tender.ma.medicalapplied.model.user.User;
import tender.ma.medicalapplied.repository.RoleRepository;
import tender.ma.medicalapplied.repository.UserRepository;

@Controller
@RequestMapping("/v1/register")
@RequiredArgsConstructor
public class RegistrationController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @GetMapping
    public String showRegistrationForm() {
        return "/medicals/register";
    }

    @PostMapping
    public String showRegistrationForm(@Valid RegistrationRequest request, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "/medicals/register";
        }

        User user = mapToUser(request);
        setDefaultRole(user);
        userRepository.save(user);

        return "redirect:/v1/login";
    }

    @ModelAttribute(name = "registrationRequest")
    public RegistrationRequest getUser() {
        return new RegistrationRequest();
    }

    private User mapToUser(RegistrationRequest registrationRequest) {
        User user = new User();
        user.setFirstName(registrationRequest.getFirstName());
        user.setLastName(registrationRequest.getLastName());
        user.setGender(registrationRequest.getGender());
        user.setBirthDate(registrationRequest.getBirthDate());
        user.setEmail(registrationRequest.getEmail());
        user.setPasswordHash(registrationRequest.getPassword());
        return user;
    }

    private void setDefaultRole(User user) {
        user.setRole(roleRepository.findByName(RoleTypes.USER));
    }
}
