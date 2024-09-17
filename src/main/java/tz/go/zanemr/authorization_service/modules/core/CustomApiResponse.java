package tz.go.zanemr.authorization_service.modules.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.ValidationException;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Optional;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomApiResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Object data;
    private Integer status;
    private String message;
    private int page;
    private int size;
    private Long total;
    private String[] errors;

    public static CustomApiResponse ok(Object data) {
        CustomApiResponse response = new CustomApiResponse();
        response.setStatus(HttpStatus.OK.value());
        response.setData(data);
        return response;
    }

    public static CustomApiResponse ok(String message, Object data) {
        CustomApiResponse response = new CustomApiResponse();
        response.setStatus(HttpStatus.OK.value());
        response.setData(data);
        response.setMessage(message);
        return response;
    }

    public static CustomApiResponse ok(String message, Object data, int page, int size, Long totalElements) {
        CustomApiResponse response = new CustomApiResponse();
        response.setStatus(HttpStatus.OK.value());
        response.setData(data);
        response.setMessage(message);
        response.setTotal(totalElements);
        response.setPage(page);
        response.setSize(size);
        return response;
    }

    public static CustomApiResponse created(String message, Object data) {
        CustomApiResponse response = new CustomApiResponse();
        response.setStatus(HttpStatus.CREATED.value());
        response.setData(data);
        response.setMessage(message);
        return response;
    }

    public static CustomApiResponse accepted(String message, Object data) {
        CustomApiResponse response = new CustomApiResponse();
        response.setStatus(HttpStatus.ACCEPTED.value());
        response.setData(data);
        response.setMessage(message);
        return response;
    }

    public static CustomApiResponse badRequest(String message, Object data) {
        CustomApiResponse response = new CustomApiResponse();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setData(data);
        response.setMessage(message);
        return response;
    }

    public static CustomApiResponse ok(Page page) {
        CustomApiResponse response = new CustomApiResponse();
        response.setStatus(HttpStatus.OK.value());
        response.setData(page.getContent());
        response.setPage(page.getNumber());
        response.setTotal(page.getTotalElements());
        response.setSize(page.getSize());
        return response;
    }

    public static CustomApiResponse ok(String message) {
        CustomApiResponse response = new CustomApiResponse();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage(message);
        return response;
    }

    public static CustomApiResponse noContent(String message) {
        CustomApiResponse response = new CustomApiResponse();
        response.setStatus(HttpStatus.NO_CONTENT.value());
        response.setMessage(message);
        return response;
    }

    public static CustomApiResponse accessDenied(String message) {
        CustomApiResponse response = new CustomApiResponse();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setMessage(message);
        return response;
    }

    public static CustomApiResponse notFound(String message) {
        CustomApiResponse response = new CustomApiResponse();
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setMessage(message);
        return response;
    }

    public static CustomApiResponse ok(Optional data) {
        if (data.isPresent()) {
            CustomApiResponse response = new CustomApiResponse();
            response.setStatus(HttpStatus.OK.value());
            response.setData(data.get());
            return response;
        } else {
            throw new ValidationException("Resource not found");
        }

    }

    public CustomApiResponse errors(String message) {
        this.message = message;
        return this;
    }
}
