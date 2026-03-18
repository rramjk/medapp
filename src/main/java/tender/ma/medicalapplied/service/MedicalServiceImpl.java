package tender.ma.medicalapplied.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tender.ma.medicalapplied.model.Medical;
import tender.ma.medicalapplied.model.MedicalFilter;
import tender.ma.medicalapplied.repository.MedicalRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MedicalServiceImpl implements MedicalService {
    private final MedicalRepository repository;

    @Override
    public List<Medical> getAll() {
        return null;
    }

    @Override
    public Medical getById(UUID id) {
        return null;
    }

    @Override
    public List<Medical> getAllByFilter(MedicalFilter filter) {
        return null;
    }

    @Override
    public List<String> getAllCountries() {
        return null;
    }

    @Override
    public List<String> getAllTypes() {
        return null;
    }

    @Override
    public List<String> getAllNames() {
        return null;
    }

}
