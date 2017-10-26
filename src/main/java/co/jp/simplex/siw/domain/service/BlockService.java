package co.jp.simplex.siw.domain.service;

import co.jp.simplex.siw.domain.model.Block;
import co.jp.simplex.siw.socket.websocket.FullNodeWebSocketHandler;
import co.jp.simplex.siw.state.BlockManager;
import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class BlockService extends BaseService {

    private static final BigDecimal ONE = new BigDecimal("1");
    private static final BigDecimal TWO = new BigDecimal("2");

    @Value("${app.fullnode.node-numbers}")
    private BigDecimal nodeNumbers;

    @Autowired
    private BlockManager manager;

    @Autowired
    private FullNodeWebSocketHandler handler;

    @Synchronized
    public void receiveBlock(Block block, String nodeKey) {
        manager.putBlock(block, nodeKey);
        Block verifiedBlock = verifyBlock(block);
        if(verifiedBlock != null) {
            TextMessage txt = new TextMessage(block.toString());
            handler.sendMessage(txt);
        }
    }

    private Block verifyBlock(Block newBlock) {
        // 閾値は ノード数/2(切り捨て)+1 とする
        int threshold = nodeNumbers.divide(TWO, 0, BigDecimal.ROUND_DOWN).add(ONE).intValue();
        Map<String, Block> blocks = manager.getNodeBlocks(newBlock.getHash());
        int count = 0;
        for (Block block : blocks.values()) {
            if (block.equals(newBlock)) {
                count++;
                continue;
            }
            // TODO nakanoya update verify logic
            if (block.getPrevHash().equals(newBlock.getPrevHash())) {
                count++;
            }
        }
        if (count >= threshold) {
            return newBlock;
        }
        return null;
    }

    private void completeBlock(Block newBlock) {
        Map<String, Block> blocks = manager.getNodeBlocks(newBlock.getHash());
        if (blocks.size() == nodeNumbers.intValue()) {
            // TODO nakanoya キャッシュから削除する
            // TODO nakanoya
        }
    }
}
