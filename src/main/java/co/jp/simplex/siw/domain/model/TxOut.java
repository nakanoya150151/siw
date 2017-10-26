package co.jp.simplex.siw.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TxOut extends BtcMessage {
    private int id;
    private String hash;
    private int amount;
    private byte[] pubkeyScript;
}
