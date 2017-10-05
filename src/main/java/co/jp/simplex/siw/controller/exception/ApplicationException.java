package co.jp.simplex.siw.controller.exception;

import java.text.MessageFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * アプリケーション例外クラス</br>
 * ControllerExceptionHandlerのハンドル対象になっているので業務上発生する可能性のある
 * 任意の例外についてはこのクラスをスローしてください。
 * 
 * @author nakanoya
 *
 */
@Getter
@Setter
@AllArgsConstructor
public class ApplicationException extends RuntimeException {

    private HttpErrors error;
    private Throwable cause;
    private Object[] args;
    
    public String getMessage() {
        if (args != null) {
            return error.name() + ":" + MessageFormat.format(error.getMessage(), args);
        }
        return error.name() + ":" + cause.getMessage();
    }
}
