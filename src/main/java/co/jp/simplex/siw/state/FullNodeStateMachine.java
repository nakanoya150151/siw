package co.jp.simplex.siw.state;

import java.util.EnumSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.config.StateMachineBuilder.Builder;
import org.springframework.stereotype.Component;

@Component
public class FullNodeStateMachine {

    @Autowired
    private ApplicationContext appContext;
    
    public static enum NodeStates {
        INACTIVE, ACTIVE
    }

    public static enum LBStates {
        DISABLE, ENABLE
    }
    
    public static enum Events {
        ON, OFF
    }
    
    public StateMachine<NodeStates, Events> buildNodeStateMachine() throws Exception {
        Builder<NodeStates, Events> builder = StateMachineBuilder.builder();
        builder.configureConfiguration().withConfiguration().beanFactory(appContext.getAutowireCapableBeanFactory());

        builder.configureStates()
            .withStates()
                .initial(NodeStates.ACTIVE)
                .states(EnumSet.allOf(NodeStates.class));
    
        builder.configureTransitions()
            .withExternal()
                .source(NodeStates.INACTIVE).target(NodeStates.ACTIVE)
                .event(Events.ON)
                .and()
            .withExternal()
                .source(NodeStates.ACTIVE).target(NodeStates.INACTIVE)
                .event(Events.OFF);
    
        return builder.build();
    }

    
    public StateMachine<LBStates, Events> buildLBStateMachine() throws Exception {
        Builder<LBStates, Events> builder = StateMachineBuilder.builder();
        builder.configureConfiguration().withConfiguration().beanFactory(appContext.getAutowireCapableBeanFactory());
    
        builder.configureStates()
            .withStates()
                .initial(LBStates.DISABLE)
                .states(EnumSet.allOf(LBStates.class));
    
        builder.configureTransitions()
            .withExternal()
                .source(LBStates.DISABLE).target(LBStates.ENABLE)
                .event(Events.ON)
                .and()
            .withExternal()
                .source(LBStates.ENABLE).target(LBStates.DISABLE)
                .event(Events.OFF);
    
        return builder.build();
    }
}
