package com.gamecraft.service.dto;

public class Tweet {
    private String message;

    public Tweet() {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Tweet{" +
            (message != null ? "message=" + message + ", " : "") +
            "}";
    }
}
