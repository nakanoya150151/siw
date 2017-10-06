package co.jp.simplex.siw.controller.bitcoind;

import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstimateSmartFeeResult {
    private BigDecimal feerate;
    private List<String> errors;
    private BigDecimal blocks;
}