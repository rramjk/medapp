package tender.ma.medicalapplied.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import tender.ma.medicalapplied.dto.MedicalDto;

import java.util.List;
import java.util.UUID;

public interface MedicalRestApi {
    String BASE_URL = "/api/medicals";

    @GetMapping
    List<MedicalDto> getMedicals(@RequestParam(required = false) String countryEn,
                                 @RequestParam(required = false) String category,
                                 @RequestParam(required = false) String name);

    @GetMapping("/{id}")
    MedicalDto getMedicalById(@PathVariable UUID id);

    @GetMapping("/categories")
    List<String> getMedicalCategories();

    @GetMapping("/countries")
    List<String> getMedicalCountries(@RequestParam(defaultValue = "false") boolean translateCountry);

    @GetMapping("/names")
    List<String> getMedicalNames();
}
