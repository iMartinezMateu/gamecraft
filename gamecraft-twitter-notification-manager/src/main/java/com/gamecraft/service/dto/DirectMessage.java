package com.gamecraft.service.dto;

public class DirectMessage {
    private String message;
    private String receiver;

    public DirectMessage() {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    @Override
    public String toString() {
        return "DirectMessage{" +
            (message != null ? "message=" + message + ", " : "") +
            (receiver != null ? "receiver=" + receiver + ", " : "") +
            "}";
    }
}
