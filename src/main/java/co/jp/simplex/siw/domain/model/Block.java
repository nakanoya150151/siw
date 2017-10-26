package co.jp.simplex.siw.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Block extends BtcMessage {
    private int id;
    private String hash;
    private String prevHash;
    private int height;
    private boolean isBestChain;
    private List<Transaction> transactions;
}
