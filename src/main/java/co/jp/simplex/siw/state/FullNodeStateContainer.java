package co.jp.simplex.siw.state;

import co.jp.simplex.siw.state.FullNodeStateMachine.Events;
import co.jp.simplex.siw.state.FullNodeStateMachine.LBStates;
import co.jp.simplex.siw.state.FullNodeStateMachine.NodeStates;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import org.springframework.statemachine.StateMachine;

@Getter
public class FullNodeStateContainer {
    private StateMachine<NodeStates, Events> nodeSM;
    private StateMachine<LBStates, Events> lbSM;
    private ConcurrentHashMap<String, ?> blockMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, ?> txMap = new ConcurrentHashMap<>();
    
    FullNodeStateContainer(StateMachine<NodeStates, Events> nodeSM, StateMachine<LBStates, Events> lbSM){
        this.nodeSM = nodeSM;
        this.lbSM = lbSM;
    }
}