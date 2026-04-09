package tender.ma.medicalapplied.service.schedulers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tender.ma.medicalapplied.repository.MedicalViewHistoryRepository;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ViewsHistoryScheduler {
    private final MedicalViewHistoryRepository repository;

    @Value("${service.expired.viewsHistoryExpiredAt}")
    private Long expiredAt;

    @Scheduled(fixedDelayString = "${service.delay.clearViewsHistoryDelay}")
    public void clearExpiredViewsHistory() {
        LocalDateTime expiredDate = LocalDateTime.now().minusDays(expiredAt);
        repository.deleteExpiredViews(expiredDate);
    }
}
