package co.jp.simplex.siw.utils;

import java.math.BigDecimal;

public class AppUtils {
    /** 1.0BTC = 100000000 satoshis */
    public static int BTC_TO_SATOSHIS = 100000000;

    /**
     * BTC単位のビットコインをsatoshisに変換します。
     * 
     * @param amount(BTC)
     * @return
     */
    public static long btcToSatoshis(String amount) {
        BigDecimal satoshis = new BigDecimal(amount);
        satoshis = satoshis.multiply(new BigDecimal(BTC_TO_SATOSHIS));
        return satoshis.longValue();
    }
}
