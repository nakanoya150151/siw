package co.jp.simplex.siw.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Transaction extends BtcMessage {
    private int id;
    private String hash;
    private List<TxIn> inputs;
    private List<TxOut> outputs;
}
