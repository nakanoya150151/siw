package co.jp.simplex.siw.state;

import co.jp.simplex.siw.socket.zmq.BitcondZmqClient;
import co.jp.simplex.siw.state.FullNodeStateMachine.Events;
import co.jp.simplex.siw.state.FullNodeStateMachine.NodeStates;
import co.jp.simplex.siw.state.FullNodeStateMachine.VerifyStates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;

/**
 * フルノードの状態を保持するコンテナのマネージャー</br>
 * フルノードの状態管理はこのクラスを経由して行います
 */
@Component
public class FullNodeStateManager {

    @Autowired
    private FullNodeStateMachine fullNodeStateMachine;
    
    @Value("${app.fullnode.node-numbers}")
    private int nodeNumbers;

    private ConcurrentHashMap<String, FullNodeStateContainer> statemap = new ConcurrentHashMap<>();
    
    @PostConstruct
    public void init(){
        try {
            for (int i = 1; i <= nodeNumbers; i++) {
                StateMachine<NodeStates, Events> nodeSM = fullNodeStateMachine.buildNodeStateMachine();
                nodeSM.start();
                
                StateMachine<VerifyStates, Events> verifySM = fullNodeStateMachine.buildVerifyStateMachine();
                verifySM.start();
                // fllnodeのスレッド名とキーを合わせる
                statemap.put(BitcondZmqClient.THREAD_PREFIX + i, new FullNodeStateContainer(nodeSM, verifySM));
            }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void invalidateNode(String nodeKey) {
        FullNodeStateContainer container = statemap.get(nodeKey);
        container.getVerifySM().sendEvent(Events.OFF);
    }

    public void validateNode(String nodeKey) {
        FullNodeStateContainer container = statemap.get(nodeKey);
        container.getVerifySM().sendEvent(Events.ON);
    }
}
