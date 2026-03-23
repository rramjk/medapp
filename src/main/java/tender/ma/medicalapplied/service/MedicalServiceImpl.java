package tender.ma.medicalapplied.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tender.ma.medicalapplied.model.Medical;
import tender.ma.medicalapplied.model.MedicalFilter;
import tender.ma.medicalapplied.repository.MedicalRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MedicalServiceImpl implements MedicalService {
    private final MedicalRepository repository;

    @Override
    public List<Medical> getAll() {
        return repository.findAll();
    }

    @Override
    public Medical getById(UUID id) {
        Optional<Medical> medical = repository.findById(id);
        return medical.orElseGet(() -> Medical.builder().build());
    }

    @Override
    public List<Medical> getAllByFilter(MedicalFilter filter) {
        return repository.findAllByCountryRuAndType(filter.getCountryRu(), filter.getCategory());
    }

    @Override
    public List<String> getAllCountries() {
        return repository.findAll().stream()
                .map(Medical::getCountryRu)
                .distinct().toList();
    }

    @Override
    public List<String> getAllTypes() {
        return repository.findAll().stream()
                .map(Medical::getType)
                .distinct().toList();
    }

    @Override
    public List<String> getAllNames() {
        return repository.findAll()
                .stream()
                .map(Medical::getName)
                .distinct()
                .toList();
    }

}
