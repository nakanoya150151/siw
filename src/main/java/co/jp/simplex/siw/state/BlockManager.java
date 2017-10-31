package co.jp.simplex.siw.state;

import co.jp.simplex.siw.domain.model.BlockHeader;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BlockManager {
    private Map<String, Map<String, BlockHeader>> blockMap = new ConcurrentHashMap<>();

    public Map<String, BlockHeader> getNodeBlocks(String hash) {
        return blockMap.get(hash);
    }

    public void putBlock(BlockHeader bh, String nodeName) {
        Map<String, BlockHeader> blocks = blockMap.get(bh.getHash());
        if (blocks == null) {
            Map<String, BlockHeader> newBlocks = new ConcurrentHashMap<>();
            newBlocks.put(nodeName, bh);
            blockMap.put(bh.getHash(), newBlocks);
        } else {
            blocks.put(nodeName, bh);
        }
    }
}
