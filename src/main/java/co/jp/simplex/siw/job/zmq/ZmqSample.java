package co.jp.simplex.siw.job.zmq;

import co.jp.simplex.siw.bitcoinj.BtcRawDataParser;
import co.jp.simplex.siw.utils.AppUtils;
import org.bitcoinj.core.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zeromq.ZMQ;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class ZmqSample {
    private static final String url = "tcp://127.0.0.1:28332";

    @Autowired
    private BtcRawDataParser btcRawDataParser;

    @Autowired
    private AppUtils appUtils;

    @PostConstruct
    private void init(){
      
        ExecutorService executor = Executors.newFixedThreadPool(1);
        try {
          executor.execute(command);
        } finally {
          executor.shutdown();
          try {
			executor.awaitTermination(1, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        }

    }

    private Runnable command = new Runnable() {
        public void run() {
            ZMQ.Context context = ZMQ.context(1);
            ZMQ.Socket socket = context.socket(ZMQ.SUB);
            
    
            socket.connect(url);
            socket.subscribe("rawblock");
            // socket.subscribe("hashblock");
            socket.subscribe("rawtx");
            // socket.subscribe("hashtx");
            System.out.println("connect to " + url );
    
            while(!Thread.currentThread().isInterrupted()) {
                String topic = socket.recvStr();
                System.out.println("client - topic:" + topic);
                if (socket.hasReceiveMore()) {
                    byte[] body = socket.recv();
                    try {
                        Transaction tx = new Transaction(appUtils.getNetWorkParam(), body);
//                        BtcTransaction tx = btcRawDataParser.parseTx(msg2);
//                    String strmsg2 = BinAscii.hexlify(msg2);
                        // String strmsg2 = BinAscii.hexlify(Arrays.copyOfRange(msg2, 0, 80));
                        System.out.println("client - body:" + tx.toString());
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
    };

}