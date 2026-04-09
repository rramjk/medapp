package tender.ma.medicalapplied.service;

import tender.ma.medicalapplied.dto.MedicalDto;

import java.util.List;
import java.util.UUID;

public interface MedicalViewHistoryService {

    List<MedicalDto> getUserMedicalViewsHistory(UUID userId);

    void deleteUserMedicalViewsHistory(UUID userId);
}
