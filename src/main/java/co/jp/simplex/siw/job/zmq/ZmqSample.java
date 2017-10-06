package co.jp.simplex.siw.job.zmq;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;
import org.zeromq.ZMQ;

@Component
public class ZmqSample {
    private static final String url = "tcp://127.0.0.1:28332";

    public ZmqSample(){
      
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
            socket.subscribe("hashblock");
            System.out.println("connect to " + url );
    
            while(!Thread.currentThread().isInterrupted()) {
                byte[] msg = socket.recv(0);
                System.out.println("client - requestAddress:" + msg);
            }
            socket.close();
            context.term();
        }
      };

}