package com.wika.wikachat.templates;

public class Message implements Comparable {

    // Attributes
    private Profile sender;
    private Profile receiver;
    private String message;
    private Integer order;

    // Constructors
    public Message() {

    }

    public Message(Profile sender, Profile receiver, String message, int order) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.order = order;
    }

    // Methods

    public Profile getReceiver() {
        return receiver;
    }

    public Profile getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public Integer getOrder() {
        return order;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setReceiver(Profile receiver) {
        this.receiver = receiver;
    }

    public void setSender(Profile sender) {
        this.sender = sender;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    @Override
    public int compareTo(Object o) {
        Message m = (Message) o;
        if (order < m.getOrder())
            return -1;
        else if (order == m.getOrder())
            return 0;
        else
            return 1;
    }
}
