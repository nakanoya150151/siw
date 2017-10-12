package co.jp.simplex.siw.bitcoinj;

public class ProtocolException extends RuntimeException {
    public ProtocolException(String msg) {
        super(msg);
    }

    public ProtocolException(Exception e) {
        super(e);
    }

    public ProtocolException(String msg, Exception e) {
        super(msg, e);
    }
}
