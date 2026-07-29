package ru.javawebinar.topjava.util.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public class ErrorInfo {

    private final String url;
    private final ErrorType type;
    private final String detail;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<FieldErrorInfo> fieldErrors;

    public ErrorInfo(CharSequence url, ErrorType type, String detail) {
        this(url, type, detail, null);
    }

    public ErrorInfo(CharSequence url, ErrorType type, String detail, List<FieldErrorInfo> fieldErrors) {
        this.url = url.toString();
        this.type = type;
        this.detail = detail;
        this.fieldErrors = (fieldErrors == null || fieldErrors.isEmpty()) ? null : fieldErrors;
    }

    public String getUrl() {
        return url;
    }

    public ErrorType getType() {
        return type;
    }

    public String getDetail() {
        return detail;
    }

    public List<FieldErrorInfo> getFieldErrors() {
        return fieldErrors;
    }

    public static class FieldErrorInfo {
        private final String field;
        private final String message;

        public FieldErrorInfo(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public String getMessage() {
            return message;
        }
    }
}