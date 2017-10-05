package co.jp.simplex.siw.controller.exception;

import org.springframework.http.HttpStatus;

/**
 * HTTPエラーレスポンスを表すインターフェース
 * 
 * @author nakanoya
 *
 */
public interface HttpErrors {

    /** HttpStatusを取得する
     * @return HTTPステータス
     */
    HttpStatus getStatus();

    /** エラーメッセージを取得する
     * @return エラーメッセージ
     */
    String getMessage();

    /** エラー名を取得する
     * @return エラー名
     */
    String name();
}
