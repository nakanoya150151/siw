package co.jp.simplex.siw.domain.service.bitcoind;

import co.jp.simplex.siw.controller.bitcoind.EstimateSmartFeeResult;
import co.jp.simplex.siw.domain.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BitcoindService extends BaseService {

    @Autowired
    private BitcoindClientAPI client;

    public EstimateSmartFeeResult estimateSmartFee(int confTarget, String estimateMode) {
        // TODO.nakanoya update args for bitcoind v0.15.0
        return client.estimateSmartFee(confTarget, estimateMode);
    }
}