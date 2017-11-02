package co.jp.simplex.siw.state;

import co.jp.simplex.siw.domain.model.BlockHeader;
import lombok.Getter;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class BlockManager {
    private Map<String, Map<String, BlockHeader>> blockMap = new ConcurrentHashMap<>();

    @Getter
    private CopyOnWriteArraySet<String> sendHashSet = new CopyOnWriteArraySet<String>();

    public Map<String, BlockHeader> getBlocks(String hash) {
        return blockMap.get(hash);
    }

    public Map<String, BlockHeader> putBlock(BlockHeader bh, String nodeName) {
        Map<String, BlockHeader> blocks = blockMap.get(bh.getHash());
        if (blocks == null) {
            blocks = new ConcurrentHashMap<>();
            blocks.put(nodeName, bh);
            blockMap.put(bh.getHash(), blocks);
            // TODO nakanoya add resize logic
        } else {
            blocks.put(nodeName, bh);
        }
        return blocks;
    }
}
