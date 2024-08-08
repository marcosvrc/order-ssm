package br.com.order.ssm.config;

import br.com.order.ssm.enums.OrderEventsEnum;
import br.com.order.ssm.enums.OrderStatesEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.transition.Transition;

import java.util.EnumSet;
import java.util.Objects;

@Configuration
@EnableStateMachineFactory
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<OrderStatesEnum, OrderEventsEnum> {

    @Override
    public void configure(StateMachineStateConfigurer<OrderStatesEnum, OrderEventsEnum> states) throws Exception {
        states
                .withStates()
                .initial(OrderStatesEnum.NEW)
                .states(EnumSet.allOf(OrderStatesEnum.class))
                .end(OrderStatesEnum.COMPLETED)
                .end(OrderStatesEnum.CANCELLED);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<OrderStatesEnum, OrderEventsEnum> transitions) throws Exception {
        transitions
                .withExternal().source(OrderStatesEnum.NEW).target(OrderStatesEnum.VALIDATED).event(OrderEventsEnum.VALIDATE)
                .action(validateOrderAction())
                .and()
                .withExternal().source(OrderStatesEnum.VALIDATED).target(OrderStatesEnum.PAID).event(OrderEventsEnum.PAY)
                .action(payOrderAction())
                .and()
                .withExternal().source(OrderStatesEnum.PAID).target(OrderStatesEnum.SHIPPED).event(OrderEventsEnum.SHIP)
                .action(shipOrderAction())
                .and()
                .withExternal().source(OrderStatesEnum.SHIPPED).target(OrderStatesEnum.COMPLETED).event(OrderEventsEnum.COMPLETE)
                .action(completeOrderAction())
                .and()
                .withExternal().source(OrderStatesEnum.VALIDATED).target(OrderStatesEnum.CANCELLED).event(OrderEventsEnum.CANCEL_VALIDATE)
                .action(cancelValidateAction())
                .and()
                .withExternal().source(OrderStatesEnum.PAID).target(OrderStatesEnum.CANCELLED).event(OrderEventsEnum.CANCEL_PAY)
                .action(cancelPayAction())
                .and()
                .withExternal().source(OrderStatesEnum.NEW).target(OrderStatesEnum.CANCELLED).event(OrderEventsEnum.CANCEL_NEW)
                .action(cancelNewAction());
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<OrderStatesEnum, OrderEventsEnum> config) throws Exception {
        config.withConfiguration().listener(stateMachineListener());
    }

    @Bean
    public StateMachineListener<OrderStatesEnum, OrderEventsEnum> stateMachineListener() {
        return new StateMachineListenerAdapter<>() {

            @Override
            public void transition(Transition<OrderStatesEnum, OrderEventsEnum> transition) {
                if(Objects.nonNull(transition.getTarget())) {
                    System.out.println("--------------------------------------");
                    System.out.println("Transitioning from " +
                            (Objects.nonNull(transition.getSource()) ? transition.getSource().getId() : "NONE") +
                            " to " +
                            transition.getTarget().getId());
                }
            }
        };
    }

    @Bean
    public Action<OrderStatesEnum, OrderEventsEnum> shipOrderAction() {
        return context -> System.out.println("Action -> Shipping Order");
    }

    @Bean
    public Action<OrderStatesEnum, OrderEventsEnum> payOrderAction() {
        return context -> System.out.println("Action -> Paying Order");
    }

    @Bean
    public Action<OrderStatesEnum, OrderEventsEnum> validateOrderAction() {
        return context -> System.out.println("Action -> Validating Order");
    }

    @Bean
    public Action<OrderStatesEnum, OrderEventsEnum> cancelNewAction() {
        return context -> System.out.println("Action -> Cancel New Order");
    }

    @Bean
    public Action<OrderStatesEnum, OrderEventsEnum> cancelPayAction() {
        return context -> System.out.println("Action -> Cancel Pay Order");
    }

    @Bean
    public Action<OrderStatesEnum, OrderEventsEnum> cancelValidateAction() {
        return context -> System.out.println("Action -> Cancel Validate Order");
    }

    @Bean
    public Action<OrderStatesEnum, OrderEventsEnum> completeOrderAction() {
        return context -> System.out.println("Action -> Complete Order");
    }
}
