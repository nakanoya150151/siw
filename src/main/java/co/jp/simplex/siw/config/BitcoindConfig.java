package co.jp.simplex.siw.config;

import co.jp.simplex.siw.domain.service.bitcoind.BitcoindClientAPI;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import com.googlecode.jsonrpc4j.ProxyUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class BitcoindConfig {
    private static final String endpoint = "http://localhost:18332";
    private static final String rpcuser ="siw";
    private static final String rpcpassword ="siw";
   
    public BitcoindConfig() {
        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication (rpcuser, rpcpassword.toCharArray());
            }
        });
    }

    @Bean
    public JsonRpcHttpClient jsonRpcHttpClient() {
        URL url = null;
        //You can add authentication headers etc to this map
        Map<String, String> map = new HashMap<>();
        try {
            url = new URL(endpoint);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonRpcHttpClient(url, map);
    }

    @Bean
    public BitcoindClientAPI bitcoindClientAPI(JsonRpcHttpClient jsonRpcHttpClient) {
        return ProxyUtil.createClientProxy(getClass().getClassLoader(), BitcoindClientAPI.class, jsonRpcHttpClient);
    }
}