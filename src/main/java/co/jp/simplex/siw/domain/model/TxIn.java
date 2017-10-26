package co.jp.simplex.siw.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TxIn extends BtcMessage {
    private int id;
    private String hash;
    private String prevHash;
    private int outputIndex;
    private byte[] signatureScript;
    private String witness;
}
