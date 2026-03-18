package tender.ma.medicalapplied.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tender.ma.medicalapplied.model.EmailMessageData;
import tender.ma.medicalapplied.model.Medical;
import tender.ma.medicalapplied.model.MedicalFilter;
import tender.ma.medicalapplied.service.MailService;
import tender.ma.medicalapplied.service.MedicalService;
import tender.ma.medicalapplied.service.SeoService;

import java.util.UUID;

@Controller
@RequestMapping(value = "/v1")
@RequiredArgsConstructor
public class MedicalController {
    private final MedicalService medicalService;
    private final SeoService seoService;
    private final MailService mailService;

    @GetMapping("/medicals")
    public String getAllMedicals(Model model, @ModelAttribute("filter") MedicalFilter filter) {
        model.addAttribute("countries", medicalService.getAllCountries());
        model.addAttribute("categories", medicalService.getAllTypes());
        return "medicals/index";
    }

    @GetMapping("/medicals/{id}")
    public String getMedicalById(Model model, @PathVariable UUID id) {
        Medical medical = medicalService.getById(id);
        model.addAttribute("medProduct", medical);
        model.addAttribute("medicals", medicalService.getAllByFilter(getMedicalFilterFromMedical(medical)));
        return "medicals/card";
    }

    @GetMapping("/medicals/about")
    public String showAboutPage() {
        return "medicals/about";
    }

    @GetMapping("/medicals/privacy")
    public String shorPrivacy() {
        return "medicals/privacy";
    }

    @PostMapping("/medicals/filtered")
    public String getFilteredMedicals(Model model, @ModelAttribute("filter") MedicalFilter filter) {
        model.addAttribute("medicals", medicalService.getAllByFilter(filter));
        model.addAttribute("country", filter.getCountryRu());
        model.addAttribute("category", filter.getCategory());
        model.addAttribute("filter", new MedicalFilter());
        model.addAttribute("countries", medicalService.getAllCountries());
        model.addAttribute("categories", medicalService.getAllTypes());
        return "medicals/article";
    }

    @PostMapping("/medicals/sign")
    public String signUpToDoctor(@ModelAttribute("emailMessageData") @Valid EmailMessageData messageData,
                                 BindingResult bindingResul) {
        mailService.sendSimpleMessage(messageData);
        return "redirect:/v1/medicals";
    }

    @ModelAttribute("content")
    public void addContent(Model model) {
        model.addAttribute("content", seoService.getContent());
        model.addAttribute("emailMessageData", new EmailMessageData());
    }

    private MedicalFilter getMedicalFilterFromMedical(Medical medical) {
        MedicalFilter filter = new MedicalFilter();
        filter.setCountryRu(medical.getCountryRu());
        filter.setCategory(medical.getType());

        return filter;
    }
}
