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
import tender.ma.medicalapplied.dto.*;
import tender.ma.medicalapplied.rest.UserRestApi;
import tender.ma.medicalapplied.service.EmailVerificationService;
import tender.ma.medicalapplied.service.MedicalViewHistoryService;
import tender.ma.medicalapplied.service.UserHealthProfileService;
import tender.ma.medicalapplied.service.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(UserRestApi.BASE_URL)
@Tag(name = "Контроллер работы с пользователем")
public class UserController implements UserRestApi {
    private final UserService userService;
    private final UserHealthProfileService userHealthService;
    private final MedicalViewHistoryService mvhService;
    private final EmailVerificationService emailVerificationService;

    @Operation(summary = "Получить пользователей",
            description = "Данный метод позволяет получить всех пользователей",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDto.class)),
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
    public List<UserDto> getUsers() {
        return userService.getUsers();
    }

    @Operation(summary = "Получить пользователя",
            description = "Данный метод позволяет получить пользователя по идентификатору",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = @Content(schema = @Schema(implementation = UserDto.class),
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
    public UserDto getUserById(@Parameter(name = "Идентификатор пользователя") UUID id) {
        return userService.getUserById(id);
    }

    @Operation(summary = "Создать пользователя",
            description = "Данный метод позволяет создать пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = @Content(schema = @Schema(implementation = UserDto.class),
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
    public UserDto createUser(UserCreateRequestDto userRequestDto) {
        return userService.createUser(userRequestDto);
    }

    @Operation(summary = "Обновить пользователя",
            description = "Данный метод позволяет обновить информацию о пользователе",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = @Content(schema = @Schema(implementation = UserDto.class),
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
    public UserDto updateUser(@Parameter(name = "Идентификатор пользователя") UUID id,
                              UserRequestDto userRequestDto) {
        return userService.updateUser(id, userRequestDto);
    }

    @Operation(summary = "Удалить пользователя",
            description = "Данный метод позволяет удалить пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = @Content(schema = @Schema(implementation = UserDto.class),
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
    public UserDto deleteUser(@Parameter(name = "Идентификатор пользователя") UUID id) {
        return userService.deleteUser(id);
    }

    @Operation(summary = "Получить список препаратов, просматриваемых пользователем",
            description = "Данный метод позволяет получить список препаратов, просматриваемых пользователем",
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
    public List<MedicalDto> getUserMedicalViews(@Parameter(name = "Идентификатор пользователя") UUID id) {
        return mvhService.getUserMedicalViewsHistory(id);
    }

    @Operation(summary = "Очистить список препаратов, просматриваемых пользователем",
            description = "Данный метод позволяет очистить список препаратов, просматриваемых пользователем",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
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
    public void deleteUserMedicalViews(@Parameter(name = "Идентификатор пользователя") UUID id) {
        mvhService.deleteUserMedicalViewsHistory(id);
    }

    @Operation(summary = "Создать запись о здоровье пользователя",
            description = "Данный метод позволяет создать запись о здоровье пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = @Content(schema = @Schema(implementation = UserHealthProfileDto.class),
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
    public UserHealthProfileDto createUserHealthProfile(@Parameter(description = "Идентификатор пользователя") UUID id, UserHealthProfileRequestDto userRequestDto) {
        return userHealthService.createUserHealthProfile(id, userRequestDto);
    }

    @Operation(summary = "Обновить запись о здоровье пользователя",
            description = "Данный метод позволяет обновить запись о здоровье пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = @Content(schema = @Schema(implementation = UserHealthProfileDto.class),
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
    public UserHealthProfileDto updateUserHealthProfile(@Parameter(description = "Идентификатор пользователя") UUID id,
                                                        @Parameter(description = "Идентификатор профиля здоровья") UUID healthId,
                                                        UserHealthProfileRequestDto userRequestDto) {
        return userHealthService.updateUserHealthProfile(id, healthId, userRequestDto);
    }

    @Operation(summary = "Получить информацию о здоровье пользователя",
            description = "Данный метод позволяет получить информацию о здоровье пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = @Content(schema = @Schema(implementation = UserHealthProfileDto.class),
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
    public UserHealthProfileDto getUserHealthProfile(@Parameter(description = "Идентификатор пользователя") UUID id,
                                                     @Parameter(description = "Идентификатор профиля здоровья") UUID healthId) {
        return userHealthService.getUserHealthProfile(id, healthId);
    }

    @Operation(summary = "Удалить информацию о здоровье пользователя",
            description = "Данный метод позволяет удалить информацию о здоровье пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = @Content(schema = @Schema(implementation = UserHealthProfileDto.class),
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
    public UserHealthProfileDto deleteUserHealthProfile(@Parameter(description = "Идентификатор пользователя") UUID id,
                                                        @Parameter(description = "Идентификатор профиля здоровья") UUID healthId) {
        return userHealthService.deleteUserHealthProfile(id, healthId);
    }

    @Operation(summary = "Создать запрос на верификацию пользователя",
            description = "Данный метод позволяет создать запрос на верификацию пользователя по почте",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = @Content(schema = @Schema(implementation = EmailVerificationDto.class),
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
    public EmailVerificationDto createVerificationUserRequest(@Parameter(description = "Идентификатор пользователя") UUID id) {
        return emailVerificationService.createVerificationUserRequest(id);
    }

    @Operation(summary = "Получить информацию о статусе верификации пользователя",
            description = "Данный метод позволяет получить информацию о статусе верификации пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = @Content(schema = @Schema(implementation = EmailVerificationDto.class),
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
    public EmailVerificationDto getVerificationUserStatus(@Parameter(description = "Идентификатор пользователя") UUID id) {
        return emailVerificationService.getVerificationUserStatus(id);
    }

    @Operation(summary = "Верифицировать пользователя",
            description = "Данный метод позволяет верифицировать пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = @Content(schema = @Schema(implementation = EmailVerificationDto.class),
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
    public EmailVerificationDto verifyUser(@Parameter(description = "Идентификатор пользователя") UUID id,
                                           @Parameter(description = "Уникальный токен верификации") UUID verificationToken) {
        return emailVerificationService.verifyUser(id, verificationToken);
    }

    @Operation(summary = "Сбросить пароль пользователя",
            description = "Данный метод позволяет сбросить пароль пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
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
    public void resetPassword(@Parameter(description = "Идентификатор пользователя") UUID id,
                              ResetPasswordRequestDto requestDto) {
        userService.resetPassword(id, requestDto);
    }
}
