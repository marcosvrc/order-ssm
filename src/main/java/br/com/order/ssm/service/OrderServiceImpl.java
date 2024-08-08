package br.com.order.ssm.service;

import br.com.order.ssm.enums.OrderEventsEnum;
import br.com.order.ssm.enums.OrderStatesEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class OrderServiceImpl implements OrderService {

    private static final String MESSAGE_NEW_ORDER = "New Order";
    private static final String MESSAGE_NEW_ORDER_CANCEL = "New Order Cancel";
    private static final String MESSAGE_PAY_ORDER = "Pay Order";
    private static final String MESSAGE_PAY_ORDER_CANCEL = "Pay Order Cancel";
    private static final String MESSAGE_SHIP_ORDER = "Ship Order";
    private static final String MESSAGE_COMPLETE_ORDER = "Complete Order";
    private static final String MESSAGE_VALIDATE_ORDER = "Validate Order";
    private static final String MESSAGE_VALIDATE_ORDER_CANCEL = "Validate Order Cancel";

    private StateMachine<OrderStatesEnum, OrderEventsEnum> stateMachine;
    private final StateMachineFactory<OrderStatesEnum, OrderEventsEnum> stateMachineFactory;

    @Autowired
    public OrderServiceImpl(StateMachineFactory<OrderStatesEnum, OrderEventsEnum> stateMachineFactory) {
        this.stateMachineFactory = stateMachineFactory;
    }

    public String newOrder(Boolean isValid) {
        initOrderSaga();
        String resultAction = null;
        if(isValid) {
            resultAction = MESSAGE_NEW_ORDER;
        } else {
            resultAction = MESSAGE_NEW_ORDER_CANCEL;
            cancelOrder(OrderEventsEnum.CANCEL_NEW);
        }
        return resultAction;
    }

    public String validateOrder(Boolean isValidate) {
        String resultAction = MESSAGE_VALIDATE_ORDER;
        if(isValidate){
            createEventValidateOrder();
        } else {
            resultAction = MESSAGE_VALIDATE_ORDER_CANCEL;
            cancelOrder(OrderEventsEnum.CANCEL_VALIDATE);
        }
        return resultAction;
    }

    public String payOrder(Boolean isPaid) {
        String resultAction = MESSAGE_PAY_ORDER;
        createEventPayOrder();
        if(!isPaid) {
            resultAction = MESSAGE_PAY_ORDER_CANCEL;
            cancelOrder(OrderEventsEnum.CANCEL_PAY);
       }
        return resultAction;
    }

    public String shipOrder() {
        createEventShipOrder();
        return MESSAGE_SHIP_ORDER;
    }

    public String completeOrder() {
        createEventCompleteOrder();
        stopOrderSaga();
        return MESSAGE_COMPLETE_ORDER;
    }

    private void cancelOrder(OrderEventsEnum event) {
        createEventCancelOrder(event);
        stopOrderSaga();
    }

    private void createEventCancelOrder(OrderEventsEnum event) {
        printAction("Canceling order...");
        stateMachine.sendEvent(Mono.just(
                MessageBuilder.withPayload(event)
                        .build())).subscribe(result -> System.out.println("Validation result: " + result.getResultType())
        );
        printFinalState();
    }

    private void stopOrderSaga() {
        printAction("Stopping order saga...");
        stateMachine.stopReactively().subscribe();
        printFinalState();
    }

    private void createEventValidateOrder() {
        printAction("Validating order...");
        stateMachine.sendEvent(Mono.just(
                MessageBuilder.withPayload(OrderEventsEnum.VALIDATE)
                        .build())).subscribe(result -> System.out.println("Validation result: " + result.getResultType())
        );
        printFinalState();
    }

    private void createEventPayOrder(){
        printAction("Paying order...");
        stateMachine.sendEvent(Mono.just(
                MessageBuilder.withPayload(OrderEventsEnum.PAY)
                        .setHeader("orderId", 1)
                        .build())).subscribe(result -> System.out.println("Validation result: " + result.getResultType())
        );
        printFinalState();
    }

    private void createEventShipOrder(){
        printAction("Shipping order...");
        stateMachine.sendEvent(Mono.just(
                MessageBuilder.withPayload(OrderEventsEnum.SHIP)
                        .setHeader("orderId", 1)
                        .build())).subscribe(result -> System.out.println("Validation result: " + result.getResultType())
        );
        printFinalState();
    }

    private void createEventCompleteOrder(){
        printAction("Completing order...");
        stateMachine.sendEvent(Mono.just(
                MessageBuilder.withPayload(OrderEventsEnum.COMPLETE)
                        .setHeader("orderId", 1)
                        .build())).subscribe(result -> System.out.println("Validation result: " + result.getResultType())
        );
       printFinalState();
    }

    private void initOrderSaga() {
        printAction("--> Initiating order saga...");
        stateMachine = stateMachineFactory.getStateMachine();
        stateMachine.startReactively().subscribe();
        printFinalState();
    }

    private void printFinalState() {
        System.out.println("Final State: " + stateMachine.getState().getId());
    }

    private void printAction(String message) {
        System.out.println(message);
    }

}
