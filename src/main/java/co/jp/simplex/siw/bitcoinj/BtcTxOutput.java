package co.jp.simplex.siw.bitcoinj;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BtcTxOutput {
    // The output's value is kept as a native type in order to save class instances.
    private long value;

    // A transaction output has a script used for authenticating that the redeemer is allowed to spend
    // this output.
    private byte[] scriptBytes;

    // The script bytes are parsed and turned into a Script on demand.
    // FIXME nakanoya
//    private Script scriptPubKey;
}
