package co.jp.simplex.siw.utils;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.RegTestParams;
import org.bitcoinj.params.TestNet3Params;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppUtils {

    @Value("${app.bitcoin.network}")
    private String bitcoinEnv;

    public NetworkParameters getNetWorkParam() {
        NetworkParameters params = null;
        if (bitcoinEnv.equals("Testnet")) {
            params = TestNet3Params.get();
        } else if (bitcoinEnv.equals("Regtest")) {
            params = RegTestParams.get();
        } else if (bitcoinEnv.equals("Mainnet")) {
            params = MainNetParams.get();
        }
        return params;
    }
}
