package com.dp.entity;

/**
 * description:
 * Author	Date	Changes
 *  Aymin 11:34 2019/1/22 Created
 *
 * @author Aymin
 */
public class Order {

    private String name;

    private Integer status;

    private String event;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
