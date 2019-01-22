package com.dp.config;

import com.alibaba.fastjson.JSONObject;
import com.dp.entity.Order;
import com.dp.enums.OrderStatus;
import com.dp.enums.OrderStatusChangeEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.statemachine.support.DefaultStateMachineContext;

import java.util.EnumSet;

/**
 * description: fsm配置
 * Author	Date	Changes
 * Aymin 11:14 2019/1/22 Created
 *
 * @author Aymin
 */
@Configuration
@EnableStateMachineFactory(name = "stateMachineFactory")
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<OrderStatus, OrderStatusChangeEvent> {
    /**
     * 订单状态机ID
     */
    public static final String orderStateMachineId = "stateMachineFactory";

    /**
     * 监听配置
     *
     * @param config
     * @throws Exception
     */
    @Override
    public void configure(StateMachineConfigurationConfigurer<OrderStatus, OrderStatusChangeEvent> config)
            throws Exception {
        config.withConfiguration().autoStartup(true);
    }

    /**
     * 配置状态
     * 没有特殊的配置方法来将状态集合标记为正交状态的一部分。简而言之，当相同的分层状态机具有多组状态时，每个都具有初始状态，
     * 从而创建正交状态。因为单个状态机只能有一个初始状态，所以多个初始状态必须意味着特定状态必须具有多个独立区域。
     * @param states
     * @throws Exception
     */
    @Override
    public void configure(StateMachineStateConfigurer<OrderStatus, OrderStatusChangeEvent> states) throws Exception {
        states.withStates().initial(OrderStatus.WAIT_PAYMENT).states(EnumSet.allOf(OrderStatus.class));
    }

    /**
     * 配置状态转换事件关系
     *
     * @param transitions
     * @throws Exception
     */
    @Override
    public void configure(StateMachineTransitionConfigurer<OrderStatus, OrderStatusChangeEvent> transitions) throws Exception {
        transitions
                .withExternal().source(OrderStatus.WAIT_PAYMENT).target(OrderStatus.WAIT_DELIVER).event(OrderStatusChangeEvent.PAYED)
                .and()
                .withExternal().source(OrderStatus.WAIT_DELIVER).target(OrderStatus.WAIT_RECEIVE).event(OrderStatusChangeEvent.DELIVERY)
                .and()
                .withExternal().source(OrderStatus.WAIT_RECEIVE).target(OrderStatus.FINISH).event(OrderStatusChangeEvent.RECEIVED);
    }

    /**
     * 持久化配置
     * 实际使用中，可以配合redis等，进行持久化操作
     *
     * @return
     */
    @Bean
    public StateMachinePersister<OrderStatus, OrderStatusChangeEvent, Order> persister() {
        return new DefaultStateMachinePersister<>(new StateMachinePersist<OrderStatus, OrderStatusChangeEvent, Order>() {
            @Override
            public void write(StateMachineContext<OrderStatus, OrderStatusChangeEvent> context, Order order) throws Exception {
                //TODO 持久化操作
                order.setStatus(context.getState().getCode());
                System.out.println(JSONObject.toJSONString(order));
            }

            @Override
            public StateMachineContext<OrderStatus, OrderStatusChangeEvent> read(Order order) throws Exception {
                //从上下文获取
                StateMachineContext<OrderStatus, OrderStatusChangeEvent> result = new DefaultStateMachineContext<>(OrderStatus.getOrderStatuByCode(order.getStatus()), null, null, null, null, orderStateMachineId);
                System.out.println(JSONObject.toJSONString(result));
                return result;
            }
        });
    }
}
