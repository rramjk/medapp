package tender.ma.medicalapplied.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tender.ma.medicalapplied.service.MedicalService;
import tender.ma.medicalapplied.service.SeoService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeoServiceImpl implements SeoService {
    private final MedicalService medicalService;
    private final static String SEO_STATIC_VALUES = "лекарства, поиск лекарств, медицинская информация, препараты, здоровье, лекарства от, препарат от";

    @Override
    public String getContent() {
        return fillString(medicalService.getMedicalNames());
    }

    private String fillString(List<String> meta) {
        StringBuilder builder = new StringBuilder(SEO_STATIC_VALUES);
        for (String name : meta) {
            builder.append(String.format(", %s", name));
        }
        return builder.toString();
    }
}
