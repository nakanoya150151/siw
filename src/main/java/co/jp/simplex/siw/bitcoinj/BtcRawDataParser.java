package co.jp.simplex.siw.bitcoinj;

import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class BtcRawDataParser {
    private static final int MESSAGE_LENGTH = 36;

    public BtcTransaction parseTx(byte[] payload) {
        BtcTransaction tx = new BtcTransaction();

        int offset = 0;
        int optimalEncodingMessageSize = 4;

        tx.setCursor(0);
        tx.setVersion(_readUint32(payload, tx));

        // First come the inputs.
//        long numInputs = readVarInt(0, payload, tx);
        long numInputs = 1L;
        optimalEncodingMessageSize += VarInt.sizeOf(numInputs);
        ArrayList<BtcTxInput> inputs = new ArrayList<>((int) numInputs);
        tx.setInputs(inputs);
        for (long i = 0; i < numInputs; i++) {
            BtcTxInput input = parseTxInput(payload, tx);
            inputs.add(input);
            long scriptLen = readVarInt(BtcOutpoint.MESSAGE_LENGTH, payload, tx);
            optimalEncodingMessageSize += BtcOutpoint.MESSAGE_LENGTH + VarInt.sizeOf(scriptLen) + scriptLen + 4;
            int cursor = tx.getCursor();
            cursor += scriptLen + 4;
            tx.setCursor(cursor);
        }
        // Now the outputs
//        long numOutputs = readVarInt(0, payload, tx);
        long numOutputs = 2L;
        optimalEncodingMessageSize += VarInt.sizeOf(numOutputs);
        ArrayList<BtcTxOutput> outputs = new ArrayList<>((int) numOutputs);
        tx.setOutputs(outputs);
        for (long i = 0; i < numOutputs; i++) {
            BtcTxOutput output = parseTxOutput(payload, tx);
            outputs.add(output);
            long scriptLen = readVarInt(8, payload, tx);
            optimalEncodingMessageSize += 8 + VarInt.sizeOf(scriptLen) + scriptLen;
            int cursor = tx.getCursor();
            cursor += scriptLen;
            tx.setCursor(cursor);
        }
        tx.setLockTime(Utils.readUint32(payload, tx.getCursor()));

        optimalEncodingMessageSize += 4;
        tx.setOptimalEncodingMessageSize(optimalEncodingMessageSize);
        tx.setLength(tx.getCursor() - offset);
        return tx;
    }

    private long _readUint32(byte[] payload, BtcMessage message) throws ProtocolException {
        int cursor = message.getCursor();
        try {
            long u = Utils.readUint32(payload, cursor);
            cursor += 4;
            message.setCursor(cursor);
            return u;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ProtocolException(e);
        }
    }



    private long _readInt64(byte[] payload, BtcMessage message) throws ProtocolException {
        int cursor = message.getCursor();
        try {
            long u = Utils.readInt64(payload, cursor);
            cursor += 8;
            message.setCursor(cursor);
            return u;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ProtocolException(e);
        }
    }



    private long readVarInt(int offset, byte[] payload, BtcMessage message) throws ProtocolException {
        try {
            int cursor = message.getCursor();
            VarInt varint = new VarInt(payload, cursor + offset);
            cursor += offset + varint.getOriginalSizeInBytes();
            message.setCursor(cursor);
            return varint.value;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ProtocolException(e);
        }
    }

    private BtcTxInput parseTxInput(byte[] payload, BtcMessage message) throws ProtocolException {
        int cursor = message.getCursor();
        BtcOutpoint outpoint = parseOutpoint(payload, message);
        cursor += BtcOutpoint.MESSAGE_LENGTH;
        BtcTxInput txInput = new BtcTxInput();
        txInput.setOutpoint(outpoint);
        int scriptLen = (int) readVarInt(0, payload, message);
        int offset = 0;
        int length = cursor - offset + scriptLen + 4;
        txInput.setScriptBytes(readBytes(scriptLen, payload, message));
        txInput.setSequence(_readUint32(payload, message));
        return txInput;
    }

    private BtcOutpoint parseOutpoint(byte[] payload, BtcMessage message) throws ProtocolException {
        BtcOutpoint outpoint = new BtcOutpoint();
//        length = MESSAGE_LENGTH;
        outpoint.setHash(reverseBytes(readBytes(32, payload, message)));
        outpoint.setIndex(_readUint32(payload, message));
        return outpoint;
    }

    protected BtcTxOutput parseTxOutput(byte[] payload, BtcMessage message) throws ProtocolException {
        BtcTxOutput output = new BtcTxOutput();
        int cursor = message.getCursor();
        output.setValue(Utils.readInt64(payload, cursor));
        int scriptLen = (int) readVarInt(0, payload, message);
        int offset = 0;
        int length = cursor - offset + scriptLen;
        output.setScriptBytes(readBytes(scriptLen, payload, message));
        return output;
    }

    private byte[] readBytes(int length, byte[] payload, BtcMessage message) throws ProtocolException {
        int cursor = message.getCursor();
        if (length > BtcMessage.MAX_SIZE) {
            throw new ProtocolException("Claimed value length too large: " + length);
        }
        try {
            byte[] b = new byte[length];
            System.arraycopy(payload, cursor, b, 0, length);
            cursor += length;
            message.setCursor(cursor);
            return b;
        } catch (IndexOutOfBoundsException e) {
            throw new ProtocolException(e);
        }
    }
    private static byte[] reverseBytes(byte[] bytes) {
        // We could use the XOR trick here but it's easier to understand if we don't. If we find this is really a
        // performance issue the matter can be revisited.
        byte[] buf = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++)
            buf[i] = bytes[bytes.length - 1 - i];
        return buf;
    }
}
