package co.jp.simplex.siw.domain.service;

import co.jp.simplex.siw.domain.model.BlockHeader;
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
    public void receiveBlock(BlockHeader bh, String nodeKey) {
        manager.putBlock(bh, nodeKey);
        BlockHeader verifiedBlock = verifyBlock(bh);
        if(verifiedBlock != null) {
            TextMessage txt = new TextMessage(bh.toString());
            handler.sendMessage(txt);
        }
    }

    private BlockHeader verifyBlock(BlockHeader newBlock) {
        // 閾値は ノード数/2(切り捨て)+1 とする
        int threshold = nodeNumbers.divide(TWO, 0, BigDecimal.ROUND_DOWN).add(ONE).intValue();
        Map<String, BlockHeader> blocks = manager.getNodeBlocks(newBlock.getHash());
        int count = 0;
        for (BlockHeader block : blocks.values()) {
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

    private void completeBlock(BlockHeader newBlock) {
        Map<String, BlockHeader> blocks = manager.getNodeBlocks(newBlock.getHash());
        if (blocks.size() == nodeNumbers.intValue()) {
            // TODO nakanoya キャッシュから削除する
            // TODO nakanoya
        }
    }
}
