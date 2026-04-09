package tender.ma.medicalapplied.rest;

import org.springframework.web.bind.annotation.GetMapping;

public interface SeoRestApi {
    String BASE_URL = "/api/seo";

    @GetMapping("/content")
    String getContent();
}
