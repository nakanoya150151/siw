package co.jp.simplex.siw.state;

import co.jp.simplex.siw.state.FullNodeStateMachine.Events;
import co.jp.simplex.siw.state.FullNodeStateMachine.NodeStates;
import co.jp.simplex.siw.state.FullNodeStateMachine.VerifyStates;

import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import org.springframework.statemachine.StateMachine;

/**
 * フルノードの状態を保持するコンテナクラス
 */
@Getter
public class FullNodeStateContainer {
    private StateMachine<NodeStates, Events> nodeSM;
    private StateMachine<VerifyStates, Events> verifySM;
    private ConcurrentHashMap<String, ?> blockMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, ?> txMap = new ConcurrentHashMap<>();
    
    FullNodeStateContainer(StateMachine<NodeStates, Events> nodeSM, StateMachine<VerifyStates, Events> verifySM){
        this.nodeSM = nodeSM;
        this.verifySM = verifySM;
    }
}