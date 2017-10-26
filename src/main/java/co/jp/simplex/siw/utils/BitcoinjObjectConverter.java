package co.jp.simplex.siw.utils;

import co.jp.simplex.siw.domain.model.Block;
import co.jp.simplex.siw.domain.model.Transaction;
import co.jp.simplex.siw.domain.model.TxIn;
import co.jp.simplex.siw.domain.model.TxOut;
import org.bitcoinj.core.TransactionInput;
import org.bitcoinj.core.TransactionOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BitcoinjObjectConverter {

    @Autowired
    private AppUtils appUtils;

    public Block convertBlockOnly(byte[] blockBody) {
        org.bitcoinj.core.Block jBlock = new org.bitcoinj.core.Block(appUtils.getNetWorkParam(), blockBody);
        Block block = new Block();
        block.setPayload(blockBody);
        block.setHash(jBlock.getHashAsString());
        block.setPrevHash(jBlock.getPrevBlockHash().toString());
        return block;
    }

    public Block convertBlock(byte[] blockBody) {
        org.bitcoinj.core.Block jBlock = new org.bitcoinj.core.Block(appUtils.getNetWorkParam(), blockBody);
        Block block = new Block();
        block.setPayload(blockBody);
        block.setHash(jBlock.getHashAsString());
        block.setPrevHash(jBlock.getPrevBlockHash().toString());
        block.setTransactions(convertTransactions(jBlock.getTransactions()));
        return block;
    }

    private List<Transaction> convertTransactions(List<org.bitcoinj.core.Transaction> jTransactions) {
        List<Transaction> txs = new ArrayList<>();
        for (org.bitcoinj.core.Transaction jTx : jTransactions) {
            Transaction tx = new Transaction();
            tx.setHash(jTx.getHashAsString());
            tx.setInputs(convertTxIns(jTx.getInputs()));
            tx.setOutputs(convertTxOuts(jTx.getOutputs()));
            txs.add(tx);
        }
        return txs;
    }

    private List<TxIn> convertTxIns(List<TransactionInput> jTxIns) {
        List<TxIn> txIns = new ArrayList<>();
        for (TransactionInput jTxIn : jTxIns) {
            TxIn txIn = new TxIn();
            txIn.setSignatureScript(jTxIn.getScriptBytes());
            txIns.add(txIn);
        }
        return txIns;
    }

    private List<TxOut> convertTxOuts(List<TransactionOutput> jTxOuts) {
        List<TxOut> txOuts = new ArrayList<>();
        for (TransactionOutput jTxOut : jTxOuts) {
            TxOut txOut = new TxOut();
            txOut.setPubkeyScript(jTxOut.getScriptPubKey().getPubKey());
            txOuts.add(txOut);
        }
        return txOuts;
    }
}
