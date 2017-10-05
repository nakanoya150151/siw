package co.jp.simplex.siw.job.zmq;

import org.springframework.stereotype.Component;
import org.zeromq.ZMQ;

@Component
public class ZmqSample {
    private static final String url = "tcp://127.0.0.1:28332";

    public ZmqSample(){

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
}