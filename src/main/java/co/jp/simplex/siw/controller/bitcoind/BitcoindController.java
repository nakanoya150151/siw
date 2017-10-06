package co.jp.simplex.siw.controller.bitcoind;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.jp.simplex.siw.controller.BaseController;
import co.jp.simplex.siw.domain.service.bitcoind.BitcoindService;

@RestController
@RequestMapping("bitcoind")
public class BitcoindController extends BaseController {

    @Autowired
    private BitcoindService bitcoindService;

    @RequestMapping(value = "estimatesmartfee", method = RequestMethod.GET)
    public EstimateSmartFeeResult estimateSmartFee(@RequestParam("conf_target") int conf, @RequestParam("estimate_mode") String mode) {
        return bitcoindService.estimateSmartFee(conf, mode);
    }
}