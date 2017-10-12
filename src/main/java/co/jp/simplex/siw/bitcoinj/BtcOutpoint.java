package co.jp.simplex.siw.bitcoinj;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BtcOutpoint {
    public static final int MESSAGE_LENGTH = 36;
    /** Hash of the transaction to which we refer. */
    private byte[] hash;
    /** Which output of that transaction we are talking about. */
    private long index;
}
