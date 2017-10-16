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
        NetworkParameters params;
        switch (bitcoinEnv) {
            case "Testnet":
                params = TestNet3Params.get();
                break;
            case "Regtest":
                params = RegTestParams.get();
                break;
            case "Mainnet":
                params = MainNetParams.get();
                break;
            default:
                params = RegTestParams.get();
                break;
        }
        return params;
    }
}
