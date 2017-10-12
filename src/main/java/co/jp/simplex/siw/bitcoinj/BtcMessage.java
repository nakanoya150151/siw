package co.jp.simplex.siw.bitcoinj;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BtcMessage {
    public static final int MAX_SIZE = 0x02000000; // 32MB
    // The offset is how many bytes into the provided byte array this message payload starts at.
    protected int offset;
    // The cursor keeps track of where we are in the byte array as we parse it.
    // Note that it's relative to the start of the array NOT the start of the message payload.
    protected int cursor;
    protected int length = 0;
}
