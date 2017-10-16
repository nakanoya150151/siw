package co.jp.simplex.siw.socket.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class WatchOnlyWebSocketClientSample {

    private static final String URL = "ws://localhost:8080/fullnode-ep";

    @Autowired
    private WatchOnlyWebSocketHandlerSample handler;

    @PostConstruct
    private void init() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Runnable runner = this::connect;
        executor.execute(runner);
    }

    private void connect() {
        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebSocketConnectionManager manager = new WebSocketConnectionManager(new StandardWebSocketClient(), handler, URL);
        manager.start();
        System.out.println("WatchOnlyWebSocketClientSample connecting...");

    }
}
