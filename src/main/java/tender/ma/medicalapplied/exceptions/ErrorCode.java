package tender.ma.medicalapplied.exceptions;

public enum ErrorCode {
    USER_FOR_AUTHORIZE_NOT_FOUND("Не найден пользователь с email %s")
    ;

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getErrorMessage(Object... args) {
        return String.format(message, args);
    }
}
