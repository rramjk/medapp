package tender.ma.medicalapplied.rest.impl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tender.ma.medicalapplied.dto.MedicalDto;
import tender.ma.medicalapplied.rest.MedicalRestApi;
import tender.ma.medicalapplied.service.MedicalService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Контроллер медицинских препаратов")
@RequestMapping(MedicalRestApi.BASE_URL)
public class MedicalController implements MedicalRestApi {
    private final MedicalService medicalService;

    @Operation(summary = "Получить список препаратов",
            description = "Данный метод позволяет получить список всех препаратов",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = MedicalDto.class)),
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
    public List<MedicalDto> getMedicals(@Parameter(name = "Страна") String countryEn,
                                        @Parameter(name = "Категория препарата") String category,
                                        @Parameter(name = "Название препарата") String name) {
        return medicalService.getMedicals(countryEn, category, name);
    }

    @Operation(summary = "Получить препарат",
            description = "Данный метод позволяет получить препарат по его идентификатору",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = @Content(schema = @Schema(implementation = MedicalDto.class),
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
    public MedicalDto getMedicalById(@Parameter(name = "Идентификатор препарата") UUID id) {
        return medicalService.getMedicalById(id);
    }

    @Operation(summary = "Получить список категорий препаратов",
            description = "Данный метод позволяет получить список всех категорий препаратов",
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
    public List<String> getMedicalCategories() {
        return medicalService.getMedicalCategories();
    }

    @Operation(summary = "Получить список стран",
            description = "Данный метод позволяет получить список всех доступных стран. По умолчанию название страны на русском",
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
    public List<String> getMedicalCountries(@Parameter(description = "Выводить название страны на английском языке") boolean translateCountryName) {
        return medicalService.getMedicalCountries(translateCountryName);
    }

    @Operation(summary = "Получить список всех имен препаратов",
            description = "Данный метод позволяет получить список всех имен доступных препаратов",
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
    public List<String> getMedicalNames() {
        return medicalService.getMedicalNames();
    }

}
