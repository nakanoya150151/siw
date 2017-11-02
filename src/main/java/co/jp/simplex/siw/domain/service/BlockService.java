package co.jp.simplex.siw.domain.service;

import co.jp.simplex.siw.domain.model.BlockHeader;
import co.jp.simplex.siw.socket.websocket.FullNodeWebSocketHandler;
import co.jp.simplex.siw.state.BlockManager;
import co.jp.simplex.siw.state.FullNodeStateManager;
import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import java.util.Map;

@Service
public class BlockService extends BaseService {

    @Value("${app.fullnode.node-numbers}")
    private int nodeNumbers;

    @Autowired
    private BlockManager blockManager;

    @Autowired
    private FullNodeWebSocketHandler handler;

    @Autowired
    private FullNodeStateManager fullNodeStateManager;

    @Synchronized
    public void receiveBlock(BlockHeader bh, String nodeKey) {
        Map<String, BlockHeader> blocks = blockManager.putBlock(bh, nodeKey);        
        int threshold = nodeNumbers / 2;
        if (blocks.size() <= threshold) {
            return;
        }

        BlockHeader candidate = getMajorityBlock(blocks);
        if (isMajority(blocks, candidate)) {
            updateVerifyStatus(blocks, candidate);
            TextMessage txt = new TextMessage(bh.toString());
            // TODO nakanoya 初回だけ送信するのに適当なSetを利用、溜まり続けるのでメンテナンスできるようにする
            boolean isSend = blockManager.getSendHashSet().contains(candidate.getHash());
            if (!isSend) {
                blockManager.getSendHashSet().add(candidate.getHash());
                handler.sendMessage(txt);
            }
        }
    }

    private BlockHeader getMajorityBlock(Map<String, BlockHeader> blocks) {
        int count = 1;
        BlockHeader majorBlock = null;
        // Boyer–Moore majority vote algorithm
        for (BlockHeader block : blocks.values()) {
            if (majorBlock == null) {
                majorBlock = block;
                continue;
            }
            if (majorBlock.getHash().equals(block.getHash())) {
                count++;
            } else {
                count--;
            }
            if (count == 0){
                majorBlock = block;
                count = 1;
            }
        }
        return majorBlock;
    }

    private boolean isMajority(Map<String, BlockHeader> blocks, BlockHeader candidate) {
        int size = nodeNumbers;
        int count = 0;
        for (BlockHeader block : blocks.values()) {
            if (block.getHash().equals(candidate.getHash())) {
                count++;
            }
        }
        if (count > size / 2) { 
            return true;
        } else {
            return false;
        }
    }

    private void updateVerifyStatus(Map<String, BlockHeader> blocks, BlockHeader majority) {
        for (Map.Entry<String, BlockHeader> entry : blocks.entrySet()) {
            if (entry.getValue().getHash().equals(majority.getHash())) {
                // VALID node
                fullNodeStateManager.validateNode(entry.getKey());
            } else {
                // INVAID node
                fullNodeStateManager.invalidateNode(entry.getKey());
            }
        }
    }
}
