package tender.ma.medicalapplied.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tender.ma.medicalapplied.dto.MedicalDto;
import tender.ma.medicalapplied.dto.UserDto;
import tender.ma.medicalapplied.model.mapping.MedicalMapper;
import tender.ma.medicalapplied.repository.MedicalViewHistoryRepository;
import tender.ma.medicalapplied.service.MedicalViewHistoryService;
import tender.ma.medicalapplied.service.UserService;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MedicalViewHistoryServiceImpl implements MedicalViewHistoryService {
    private final MedicalViewHistoryRepository repository;
    private final UserService userService;
    private final MedicalMapper mapper;

    @Override
    public List<MedicalDto> getUserMedicalViewsHistory(UUID userId) {
        UserDto userDto = userService.getUserById(userId);
        return repository.getMedicalsFromViewsHistoryByUserId(userDto.getId())
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public void deleteUserMedicalViewsHistory(UUID userId) {
        repository.deleteMedicalViewHistoryByUserId(userId);
    }


}
