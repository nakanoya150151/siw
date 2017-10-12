package co.jp.simplex.siw.bitcoinj;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BtcTxInput {
    // Allows for altering transactions after they were broadcast. Values below NO_SEQUENCE-1 mean it can be altered.
    private long sequence;
    // Data needed to connect to the output of the transaction we're gathering coins from.
    private BtcOutpoint outpoint;
    // The "script bytes" might not actually be a script. In coinbase transactions where new coins are minted there
    // is no input transaction, so instead the scriptBytes contains some extra stuff (like a rollover nonce) that we
    // don't care about much. The bytes are turned into a Script object (cached below) on demand via a getter.
    private byte[] scriptBytes;
    // The Script object obtained from parsing scriptBytes. Only filled in on demand and if the transaction is not
    // coinbase.
    // FIXME nakanoya
//    private WeakReference<Script> scriptSig;
}
