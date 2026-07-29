package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.util.exception.ErrorInfo;
import ru.javawebinar.topjava.util.exception.ErrorType;
import ru.javawebinar.topjava.util.exception.IllegalRequestDataException;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static ru.javawebinar.topjava.util.exception.ErrorType.*;

@RestControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class ExceptionInfoHandler {

    private static final Logger log = LoggerFactory.getLogger(ExceptionInfoHandler.class);

    private final MessageSource messageSource;

    public ExceptionInfoHandler(ObjectProvider<MessageSource> messageSourceProvider) {
        this.messageSource = messageSourceProvider.getIfAvailable();
    }

    private static String getValidationDetail(Exception e) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        String message = rootCause.getMessage();
        return message != null ? message : rootCause.toString();
    }

    private static ErrorInfo logAndGetErrorInfo(
            HttpServletRequest req,
            Exception e,
            boolean logException,
            ErrorType errorType
    ) {
        return logAndGetErrorInfo(
                req,
                e,
                logException,
                errorType,
                ValidationUtil.getRootCause(e).toString(),
                null
        );
    }

    private static ErrorInfo logAndGetErrorInfo(
            HttpServletRequest req,
            Exception e,
            boolean logException,
            ErrorType errorType,
            String detail
    ) {
        return logAndGetErrorInfo(req, e, logException, errorType, detail, null);
    }

    private static ErrorInfo logAndGetErrorInfo(
            HttpServletRequest req,
            Exception e,
            boolean logException,
            ErrorType errorType,
            String detail,
            List<ErrorInfo.FieldErrorInfo> fieldErrors
    ) {
        Throwable rootCause = ValidationUtil.getRootCause(e);

        if (logException) {
            log.error(errorType + " at request " + req.getRequestURL(), rootCause);
        } else {
            log.warn("{} at request {}: {}", errorType, req.getRequestURL(), detail);
        }

        return new ErrorInfo(req.getRequestURL(), errorType, detail, fieldErrors);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(NotFoundException.class)
    public ErrorInfo notFoundError(HttpServletRequest req, NotFoundException e) {
        return logAndGetErrorInfo(req, e, false, DATA_NOT_FOUND, e.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorInfo conflict(HttpServletRequest req, DataIntegrityViolationException e) {
        String rootMsg = ValidationUtil.getRootCause(e).getMessage();

        if (rootMsg != null && rootMsg.toLowerCase().contains("meal_unique_user_datetime_idx")) {
            return logAndGetErrorInfo(
                    req,
                    e,
                    false,
                    VALIDATION_ERROR,
                    getMessage(req, "error.duplicateDateTime")
            );
        }

        return logAndGetErrorInfo(req, e, true, DATA_ERROR);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({
            IllegalRequestDataException.class,
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class
    })
    public ErrorInfo validationError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, false, VALIDATION_ERROR, getValidationDetail(e));
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(BindException.class)
    public ErrorInfo bindValidationError(HttpServletRequest req, BindException e) {
        List<ErrorInfo.FieldErrorInfo> fieldErrors = e.getAllErrors().stream()
                .map(error -> new ErrorInfo.FieldErrorInfo(
                        error instanceof FieldError ? ((FieldError) error).getField() : "global",
                        error.getDefaultMessage()
                ))
                .toList();

        return logAndGetErrorInfo(
                req,
                e,
                false,
                VALIDATION_ERROR,
                getMessage(req, "error.validation"),
                fieldErrors
        );
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorInfo internalError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true, APP_ERROR);
    }

    private String getMessage(HttpServletRequest req, String code) {
        if (messageSource == null) {
            return code;
        }
        return messageSource.getMessage(code, null, code, req.getLocale());
    }
}