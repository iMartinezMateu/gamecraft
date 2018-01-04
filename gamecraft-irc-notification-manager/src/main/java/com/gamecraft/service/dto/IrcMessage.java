package com.gamecraft.service.dto;

public class IrcMessage {
    private String ircChannel;
    private String message;

    public IrcMessage() {

    }

    public String getIrcChannel() {
        return ircChannel;
    }

    public void setIrcChannel(String ircChannel) {
        this.ircChannel = ircChannel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "IrcMessage{" +
            (ircChannel != null ? "ircChannel=" + ircChannel + ", " : "") +
            (message != null ? "message=" + message + ", " : "") +
            "}";
    }
}
