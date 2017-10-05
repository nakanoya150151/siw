package co.jp.simplex.siw.controller.exception;

import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.Getter;
import lombok.Setter;

/**
 * Controllerで発生した例外をハンドルするクラス
 * ExceptionHandlerアノテーションの引数に指定したExceptionをキャッチして任意のJSONに変換して返します。
 * 
 * @author nakanoya
 *
 */
@RestController
@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * ApplicationException用のハンドラーメソッド
     * @param req
     * @param e
     * @return JSONレスポンス
     */
    @ExceptionHandler({ ApplicationException.class })
    public ResponseEntity<RestError> handleError(HttpServletRequest req, ApplicationException e) {
        return handleError(req, e.getError(), e, e.getArgs());
    }
    
    private ResponseEntity<RestError> handleError(HttpServletRequest request, HttpErrors error, Exception ex,
            Object... args) {
        String message = MessageFormat.format(error.getMessage(), args);
        if (error.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR) {
            logger.error(message, ex);
        } else {
            logger.debug(message, ex);
        }
        if (error.getStatus() == HttpStatus.UNAUTHORIZED) {
            return new ResponseEntity<>(error.getStatus());
        }
        RestError restError = new RestError();
        restError.path = request.getRequestURI();
        restError.error = error.name();
        restError.status = error.getStatus().value();
        restError.message = message;
        restError.exception = ex.getClass().getName();
        return new ResponseEntity<>(restError, error.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        RestError restError = new RestError();
        if (request instanceof ServletWebRequest) {
            restError.path = ((ServletWebRequest) request).getRequest().getRequestURI();
        } else {
            restError.path = request.getContextPath();
        }
        restError.error = status.getReasonPhrase();
        restError.status = status.value();
        restError.message = ex.getMessage();
        restError.exception = ex.getClass().getName();
        return new ResponseEntity<>(restError, status);
    }

    @Getter
    @Setter
    private class RestError {
        String path;
        String error;
        int status;
        String message;
        String exception;
    }
}
