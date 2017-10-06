package co.jp.simplex.siw.domain.service.bitcoind;

import co.jp.simplex.siw.controller.bitcoind.EstimateSmartFeeResult;
import com.googlecode.jsonrpc4j.JsonRpcMethod;
import com.googlecode.jsonrpc4j.JsonRpcParam;

public interface BitcoindClientAPI {
    @JsonRpcMethod("estimatesmartfee")
    EstimateSmartFeeResult estimateSmartFee(@JsonRpcParam(value = "conf_target") int conf, @JsonRpcParam(value = "estimate_mode") String mode);
}