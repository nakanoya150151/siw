package co.jp.simplex.siw.bitcoinj;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class BtcTransaction extends BtcMessage {
    // These are bitcoin serialized.
    private long version;
    private ArrayList<BtcTxInput> inputs;
    private ArrayList<BtcTxOutput> outputs;
    private long lockTime;

    // Transactions can be encoded in a way that will use more bytes than is optimal
    // (due to VarInts having multiple encodings)
    // MAX_BLOCK_SIZE must be compared to the optimal encoding, not the actual encoding, so when parsing, we keep track
    // of the size of the ideal encoding in addition to the actual message size (which Message needs) so that Blocks
    // can properly keep track of optimal encoded size
    private int optimalEncodingMessageSize;
}
