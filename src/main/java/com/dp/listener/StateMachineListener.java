package com.dp.listener;

import com.dp.config.StateMachineConfig;
import com.dp.entity.Order;
import com.dp.enums.OrderStatus;
import com.dp.enums.OrderStatusChangeEvent;
import org.springframework.messaging.Message;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Component;

/**
 * description:fsm 事件总线
 * Author	Date	Changes
 * Aymin 11:59 2019/1/22 Created
 *
 * @author Aymin
 */
@Component
@WithStateMachine(id = StateMachineConfig.orderStateMachineId)
public class StateMachineListener {

    @OnTransition(source = "WAIT_PAYMENT", target = "WAIT_DELIVER")
    public boolean payTransition(Message<OrderStatusChangeEvent> message) {
        System.out.println("----------------------------");
        Order order = (Order) message.getHeaders().get("order");
        order.setStatus(OrderStatus.WAIT_DELIVER.getCode());
        System.out.println("支付 headers=" + message.getHeaders().toString() + "order name:" + order.getName() + " event=" + message.getPayload());
        System.out.println("----------------------------");
        return true;
    }

    @OnTransition(source = "WAIT_DELIVER", target = "WAIT_RECEIVE")
    public boolean deliverTransition(Message<OrderStatusChangeEvent> message) {
        System.out.println("----------------------------");
        Order order = (Order) message.getHeaders().get("order");
        order.setStatus(OrderStatus.WAIT_RECEIVE.getCode());
        System.out.println("发货 headers=" + message.getHeaders().toString() + "order name:" + order.getName() + " event=" + message.getPayload());
        System.out.println("----------------------------");
        return true;
    }

    @OnTransition(source = "WAIT_RECEIVE", target = "FINISH")
    public boolean receiveTransition(Message<OrderStatusChangeEvent> message) {
        System.out.println("----------------------------");
        Order order = (Order) message.getHeaders().get("order");
        order.setStatus(OrderStatus.FINISH.getCode());
        System.out.println("收货 headers=" + message.getHeaders().toString() + "order name:" + order.getName() + " event=" + message.getPayload());
        System.out.println("----------------------------");
        return true;
    }
}
