package tender.ma.medicalapplied.service;

import tender.ma.medicalapplied.dto.MedicalDto;

import java.util.List;
import java.util.UUID;

public interface MedicalService {

    List<MedicalDto> getMedicals(String countryEn, String category, String name);

    MedicalDto getMedicalById(UUID id);

    List<String> getMedicalCategories();

    List<String> getMedicalCountries(boolean translateCountryName);

    List<String> getMedicalNames();
}
