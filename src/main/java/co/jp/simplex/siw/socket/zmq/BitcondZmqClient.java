package co.jp.simplex.siw.socket.zmq;

import co.jp.simplex.siw.domain.model.Block;
import co.jp.simplex.siw.domain.service.BlockService;
import co.jp.simplex.siw.utils.AppUtils;
import co.jp.simplex.siw.utils.BitcoinjObjectConverter;
import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.core.Context;
import org.bitcoinj.core.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zeromq.ZMQ;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
public class BitcondZmqClient {
    private static final String url = "tcp://127.0.0.1:28332";

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private BitcoinjObjectConverter bitcoinjObjectConverter;

    @Autowired
    private BlockService blockService;

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
        log.info("connect to " + url);

        while (!Thread.currentThread().isInterrupted()) {
            String nodeName = Thread.currentThread().getName();
            String topic = socket.recvStr();
            log.info("client - topic:" + topic);
            if (socket.hasReceiveMore()) {
                byte[] body = socket.recv();
                try {
                    switch (topic) {
                        case "rawblock":
                            Block block = bitcoinjObjectConverter.convertBlockOnly(body);
                            log.info("client - body:" + block.toString());
                            blockService.receiveBlock(block, nodeName);
                            break;
                        case "rawtx":
                            Transaction tx = new Transaction(appUtils.getNetWorkParam(), body);
                            log.info("client - body:" + tx.toString());
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