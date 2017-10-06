package co.jp.simplex.siw.state;

import co.jp.simplex.siw.state.FullNodeStateMachine;
import co.jp.simplex.siw.state.FullNodeStateMachine.Events;
import co.jp.simplex.siw.state.FullNodeStateMachine.LBStates;
import co.jp.simplex.siw.state.FullNodeStateMachine.NodeStates;

import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

@Component
public class StateManager {

    @Autowired
    private FullNodeStateMachine fullNodeStateMachine;

    private ConcurrentHashMap<String, FullNodeStateContainer> statemap = new ConcurrentHashMap<>();
    
    @PostConstruct
    public void init(){
        try {
            StateMachine<NodeStates, Events> nodeSM = fullNodeStateMachine.buildNodeStateMachine();
            nodeSM.start();

            StateMachine<LBStates, Events> lbSM = fullNodeStateMachine.buildLBStateMachine();
            lbSM.start();

            statemap.put("key", new FullNodeStateContainer(nodeSM, lbSM));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
