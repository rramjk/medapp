package tender.ma.medicalapplied.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tender.ma.medicalapplied.config.security.utils.AuthUserService;
import tender.ma.medicalapplied.dto.MedicalDto;
import tender.ma.medicalapplied.exceptions.ErrorCode;
import tender.ma.medicalapplied.exceptions.NotFoundException;
import tender.ma.medicalapplied.model.mapping.MedicalMapper;
import tender.ma.medicalapplied.model.mapping.MedicalViewHistoryMapper;
import tender.ma.medicalapplied.model.medical.Medical;
import tender.ma.medicalapplied.model.user.User;
import tender.ma.medicalapplied.repository.MedicalViewHistoryRepository;
import tender.ma.medicalapplied.repository.medical.MedicalRepository;
import tender.ma.medicalapplied.repository.medical.MedicalSpecification;
import tender.ma.medicalapplied.service.MedicalService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MedicalServiceImpl implements MedicalService {
    private final MedicalRepository repository;
    private final MedicalMapper mapper;
    private final MedicalViewHistoryRepository medicalViewHistoryRepository;
    private final MedicalViewHistoryMapper mvhMapper;
    private final AuthUserService authUserService;

    @Override
    public List<MedicalDto> getMedicals(String countryEn, String category, String name) {
        return repository.findAll(MedicalSpecification.byFilters(countryEn, category, name))
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public MedicalDto getMedicalById(UUID id) {
        Medical medical = getByIdOrThrowExc(id);
        try {
            saveMedicalToUserViewHistory(medical);
        } catch (Exception e) {
            log.warn("getMedicalById: saveMedicalToUserViewHistory throws exception ", e);
        }
        return mapper.toDto(medical);
    }

    private void saveMedicalToUserViewHistory(Medical medical) {
        Optional<User> userOpt = authUserService.getCurrentUser();
        if (userOpt.isPresent()) {
            medicalViewHistoryRepository.save(mvhMapper.toMedicalViewHistory(userOpt.get(), medical));
        } else {
            throw new NotFoundException(ErrorCode.USER_EMPTY_IN_AUTHORIZE_CONTEXT);
        }
    }

    @Override
    public List<String> getMedicalCategories() {
        return repository.getAllTypes();
    }

    @Override
    public List<String> getMedicalCountries(boolean translateCountryName) {
        return translateCountryName
                ? repository.getAllCountriesEn()
                : repository.getAllCountriesRu();
    }

    @Override
    public List<String> getMedicalNames() {
        return repository.getAllNames();
    }

    public Medical getByIdOrThrowExc(UUID id) {
        Optional<Medical> medical = repository.findById(id);
        return medical.orElseThrow(() -> new NotFoundException(ErrorCode.MEDICAL_NOT_FOUND.getErrorMessage(id)));
    }
}
