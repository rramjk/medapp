package tender.ma.medicalapplied.rest.impl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tender.ma.medicalapplied.rest.SeoRestApi;
import tender.ma.medicalapplied.service.SeoService;

@RestController
@RequiredArgsConstructor
@Tag(name = "Контроллер работы с SEO")
@RequestMapping(SeoRestApi.BASE_URL)
public class SeoController implements SeoRestApi {
    private final SeoService seoService;

    @Operation(summary = "Получить метаданные для SEO",
            description = "Данный метод позволяет получить список контента для оптимизации SEO",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = String.class)),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "400", description = "Bad Request + Некорректный запрос",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Forbidden",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404", description = "Not found",
                            content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(schema = @Schema(hidden = true)))
            })
    @Override
    public String getContent() {
        return seoService.getContent();
    }
}
