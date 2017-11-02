package co.jp.simplex.siw.socket.zmq;

import co.jp.simplex.siw.domain.model.BlockHeader;
import co.jp.simplex.siw.domain.service.BlockService;
import co.jp.simplex.siw.utils.AppUtils;
import co.jp.simplex.siw.utils.BitcoinjObjectConverter;
import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.core.Context;
import org.bitcoinj.core.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.zeromq.ZMQ;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@Slf4j
@Component
public class BitcondZmqClient {
    private static final String url = "tcp://127.0.0.1:28332";
    public static final String THREAD_PREFIX = "fullnode-";

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private BitcoinjObjectConverter bitcoinjObjectConverter;

    @Autowired
    private BlockService blockService;

    @Value("${app.fullnode.node-numbers}")
    private int nodeNumbers;
    
    @PostConstruct
    private void init() {
        for (int i = 1; i <= nodeNumbers; i++) {
            final int count = i;
            ExecutorService executor = Executors.newFixedThreadPool(1, new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, THREAD_PREFIX + count);
                }
            });
            Runnable runner = this::connectZmqServer;
            executor.execute(runner);
        }
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
                            BlockHeader bh = bitcoinjObjectConverter.convertBlockHeader(body);
                            log.info("client - body:" + bh.toString());
                            blockService.receiveBlock(bh, nodeName);
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