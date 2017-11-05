package com.gamecraft.service.dto;

import java.util.Arrays;

public class SlackMessage {
    private String[] channels;
    private String[] users;
    private String message;

    public SlackMessage() {

    }

    public String[] getChannels() {
        return channels;
    }

    public void setChannels(String[] channels) {
        this.channels = channels;
    }

    public String[] getUsers() {
        return users;
    }

    public void setUsers(String[] users) {
        this.users = users;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "SlackMessage{" +
            (channels != null ? "channels=" + Arrays.toString(channels) + ", " : "") +
            (users != null ? "users=" + Arrays.toString(users) + ", " : "") +
            (message != null ? "message=" + message + ", " : "") +
            "}";
    }
}
