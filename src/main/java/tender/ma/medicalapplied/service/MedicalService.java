package tender.ma.medicalapplied.service;

import tender.ma.medicalapplied.model.Medical;
import tender.ma.medicalapplied.model.MedicalFilter;

import java.util.List;
import java.util.UUID;

public interface MedicalService {

    List<Medical> getAll();

    Medical getById(UUID id);

    List<Medical> getAllByFilter(MedicalFilter medical);

    List<String> getAllCountries();

    List<String> getAllTypes();

    List<String> getAllNames();
}
