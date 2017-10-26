package co.jp.simplex.siw.state;

import co.jp.simplex.siw.domain.model.Block;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BlockManager {
    private Map<String, Map<String, Block>> blockMap = new ConcurrentHashMap<>();

    public Map<String, Block> getNodeBlocks(String hash) {
        return blockMap.get(hash);
    }

    public void putBlock(Block block, String nodeName) {
        Map<String, Block> blocks = blockMap.get(block.getHash());
        if (blocks == null) {
            Map<String, Block> newBlocks = new ConcurrentHashMap<>();
            newBlocks.put(nodeName, block);
            blockMap.put(block.getHash(), newBlocks);
        } else {
            blocks.put(nodeName, block);
        }
    }
}
