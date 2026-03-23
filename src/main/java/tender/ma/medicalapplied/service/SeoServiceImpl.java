package tender.ma.medicalapplied.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeoServiceImpl implements SeoService {
    private final MedicalService medicalService;

    @Override
    public String getContent() {
        return fillString(medicalService.getAllNames());
    }

    private String fillString(List<String> meta) {
        StringBuilder builder = new StringBuilder("лекарства, поиск лекарств, медицинская информация, препараты, здоровье, ");
        for(String name : meta) {
            builder.append(String.format("%s, ", name));
        }
        return builder.toString();
    }
}
