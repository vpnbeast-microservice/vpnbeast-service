package com.vpnbeast.vpnbeastservice.exception;

import com.vpnbeast.vpnbeastservice.model.enums.ExceptionMessage;
import com.vpnbeast.vpnbeastservice.model.enums.HttpFields;
import com.vpnbeast.vpnbeastservice.util.DateUtil;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.JDBCException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@ResponseBody
@ControllerAdvice
public class GenericControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    private ResponseEntity<ExceptionInfo> handleResourceNotFoundException(ResourceNotFoundException exception) {
        log.warn(exception.getExceptionInfo().getErrorMessage());
        final ExceptionInfo exceptionInfo = exception.getExceptionInfo();
        return new ResponseEntity<>(exceptionInfo, HttpStatus.valueOf(exceptionInfo.getHttpCode()));
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    private ResponseEntity<ExceptionInfo> handleResourceAlreadyExistsException(ResourceAlreadyExistsException exception) {
        log.warn(exception.getExceptionInfo().getErrorMessage());
        final ExceptionInfo exceptionInfo = exception.getExceptionInfo();
        return new ResponseEntity<>(exceptionInfo, HttpStatus.valueOf(exceptionInfo.getHttpCode()));
    }

    @ExceptionHandler(VerificationCodeExpiredException.class)
    private ResponseEntity<ExceptionInfo> handleVerificationCodeExpiredException(VerificationCodeExpiredException exception) {
        log.warn(exception.getExceptionInfo().getErrorMessage());
        final ExceptionInfo exceptionInfo = exception.getExceptionInfo();
        return new ResponseEntity<>(exceptionInfo, HttpStatus.valueOf(exceptionInfo.getHttpCode()));
    }

    @ExceptionHandler(VerificationCodeNotValidException.class)
    private ResponseEntity<ExceptionInfo> handleVerificationCodeExpiredException(VerificationCodeNotValidException exception) {
        log.warn(exception.getExceptionInfo().getErrorMessage());
        final ExceptionInfo exceptionInfo = exception.getExceptionInfo();
        return new ResponseEntity<>(exceptionInfo, HttpStatus.valueOf(exceptionInfo.getHttpCode()));
    }

    @ExceptionHandler(NonUpdatableColumnException.class)
    private ResponseEntity<ExceptionInfo> handleNonUpdatableColumnException(NonUpdatableColumnException exception) {
        log.warn(exception.getExceptionInfo().getErrorMessage());
        final ExceptionInfo exceptionInfo = exception.getExceptionInfo();
        return new ResponseEntity<>(exceptionInfo, HttpStatus.valueOf(exceptionInfo.getHttpCode()));
    }

    @ExceptionHandler(FeignException.class)
    private ResponseEntity<ExceptionInfo> handleFeignException(FeignException exception) {
        log.error("", exception);
        final ExceptionInfo exceptionInfo = ExceptionInfo.builder()
                .errorMessage(exception.getMessage())
                .httpCode(exception.status())
                .timestamp(DateUtil.getCurrentLocalDateTime())
                .build();
        return new ResponseEntity<>(exceptionInfo, HttpStatus.valueOf(exceptionInfo.getHttpCode()));
    }

    @ExceptionHandler(JDBCException.class)
    private ResponseEntity<ExceptionInfo> handleJDBCException(JDBCException exception) {
        log.error("", exception);
        final ExceptionInfo exceptionInfo = ExceptionInfo.builder()
                .errorMessage(exception.getMessage())
                .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(DateUtil.getCurrentLocalDateTime())
                .build();
        return new ResponseEntity<>(exceptionInfo, HttpStatus.valueOf(exceptionInfo.getHttpCode()));
    }

    @NonNull
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                               @NonNull HttpHeaders headers,
                                                               HttpStatus status,
                                                               @NonNull WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(HttpFields.TIMESTAMP.getField(), DateUtil.getCurrentLocalDateTime());
        body.put(HttpFields.HTTP_CODE.getField(), status.value());
        body.put(HttpFields.STATUS.getField(), false);
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        body.put(HttpFields.ERROR_MESSAGE.getField(), errors);
        return new ResponseEntity<>(body, headers, status);
    }

    @NonNull
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         @NonNull HttpHeaders headers,
                                                                         @NonNull HttpStatus status,
                                                                         @NonNull WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(" method is not supported for this request. Supported methods are ");
        Objects.requireNonNull(ex.getSupportedHttpMethods()).forEach(t -> builder.append(t).append(" "));
        body.put(HttpFields.ERRORS.getField(), builder.toString());
        body.put(HttpFields.MESSAGE.getField(), ex.getLocalizedMessage());
        body.put(HttpFields.TIMESTAMP.getField(), DateUtil.getCurrentLocalDateTime());
        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", ex.getLocalizedMessage());
        body.put("error", "unknown error occured at the backend");
        body.put("timestamp", DateUtil.getCurrentLocalDateTime());
        return new ResponseEntity<>(body, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}