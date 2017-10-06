package co.jp.simplex.siw.controller.exception;

import java.text.MessageFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * アプリケーション例外クラス</br>
 * 業務上発生する可能性のある任意の例外についてはこのクラスをスローしてください。
 * スローした例外はControllerExceptionHandlerで処理されます。
 * 
 * @author nakanoya
 *
 */
@Getter
@Setter
@AllArgsConstructor
@SuppressWarnings("serial")
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
