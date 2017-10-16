package co.jp.simplex.siw.socket.zmq;

import co.jp.simplex.siw.socket.websocket.FullNodeWebSocketHandler;
import co.jp.simplex.siw.utils.AppUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bitcoinj.core.Block;
import org.bitcoinj.core.Context;
import org.bitcoinj.core.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.zeromq.ZMQ;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class BitcondZmqClient {
    private static final String url = "tcp://127.0.0.1:28332";

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private FullNodeWebSocketHandler handler;

    @PostConstruct
    private void init() {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        Runnable runner = this::connectZmqServer;
        executor.execute(runner);
    }

    private void connectZmqServer() {
        // TODO nakanoya.bitcoinjのcontextの使用方法確認
        Context.propagate(new Context(appUtils.getNetWorkParam()));

        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket socket = context.socket(ZMQ.SUB);

        socket.connect(url);
        socket.subscribe("rawblock");
        // socket.subscribe("hashblock");
        socket.subscribe("rawtx");
        // socket.subscribe("hashtx");
        System.out.println("connect to " + url);

        while (!Thread.currentThread().isInterrupted()) {
            String topic = socket.recvStr();
            System.out.println("client - topic:" + topic);
            if (socket.hasReceiveMore()) {
                byte[] body = socket.recv();
                try {
                    switch (topic) {
                        case "rawblock":
                            Block block = new Block(appUtils.getNetWorkParam(), body);
                            System.out.println("client - body:" + block.toString());

                            TextMessage txt = new TextMessage(block.toString());
                            handler.sendMessage(txt);
                            break;
                        case "rawtx":
                            Transaction tx = new Transaction(appUtils.getNetWorkParam(), body);
                            System.out.println("client - body:" + tx.toString());
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (socket.hasReceiveMore()) {
                String sequence = socket.recvStr();
            }
        }
        socket.close();
        context.term();
    }

}