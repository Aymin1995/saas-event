package com.dp;

import com.dp.entity.Order;
import com.dp.enums.OrderStatus;
import com.dp.enums.OrderStatusChangeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.StateMachinePersister;

@SpringBootApplication
public class SaasEventApplication implements CommandLineRunner {

    @Autowired
    private StateMachineFactory<OrderStatus, OrderStatusChangeEvent> stateMachineFactory;
    @Autowired
    private StateMachinePersister<OrderStatus, OrderStatusChangeEvent, Order> persister;

    public static void main(String[] args) {
        SpringApplication.run(SaasEventApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Order order1 = new Order();
        order1.setName("test1");
        Order order2 = new Order();
        order2.setName("test2");
        Order order3 = new Order();
        order3.setName("test3");

        StateMachine<OrderStatus, OrderStatusChangeEvent> stateMachine1 = stateMachineFactory.getStateMachine("stateMachineFactory");
        StateMachine<OrderStatus, OrderStatusChangeEvent> stateMachine2 = stateMachineFactory.getStateMachine("stateMachineFactory");
        System.out.println("状态机1 ID:" + stateMachine1.getUuid());
        System.out.println("状态机2 ID:" + stateMachine2.getUuid());
        Message<OrderStatusChangeEvent> msg1 = MessageBuilder.withPayload(OrderStatusChangeEvent.DELIVERY).setHeader("order", order1).build();
        Message<OrderStatusChangeEvent> msg2 = MessageBuilder.withPayload(OrderStatusChangeEvent.PAYED).setHeader("order", order2).build();
        Message<OrderStatusChangeEvent> msg3 = MessageBuilder.withPayload(OrderStatusChangeEvent.PAYED).setHeader("order", order3).build();


        stateMachine1.sendEvent(msg1);
        //msg1 并没有生效 msg1 并不是初始化事件
        stateMachine1.sendEvent(msg2);
        stateMachine2.sendEvent(msg3);
        //persister.persist(stateMachine1, order1);
    }
}

