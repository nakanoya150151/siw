package co.jp.simplex.siw.controller.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * HTTPエラーを表すEnumクラス
 * 発生するHTTPエラーはすべてこのクラスに列挙してください。
 * 
 * @author nakanoya
 *
 */
@AllArgsConstructor
public enum ApplicationHttpErrors implements HttpErrors {
    INSUFFICIENT_MONEY(HttpStatus.BAD_REQUEST, "振替金額に対してウォレット残高が不足しています。: id={0} balance={1} amount={2} (satoshis)"),
    UNEXPECTED(HttpStatus.INTERNAL_SERVER_ERROR, "想定外のエラーが発生しました。 : {0}");
    
    @Getter(onMethod=@__({@Override}))
    private HttpStatus status;
    @Getter(onMethod=@__({@Override}))
    private String message;

}
