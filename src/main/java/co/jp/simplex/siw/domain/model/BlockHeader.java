package co.jp.simplex.siw.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlockHeader extends BtcMessage {
    private String hash;
    private String prevHash;
}
