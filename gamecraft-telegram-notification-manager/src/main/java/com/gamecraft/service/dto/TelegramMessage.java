package com.gamecraft.service.dto;

public class TelegramMessage {
    private String message;
    private String  chatId;
    private boolean webPagePreviewDisabled;
    private boolean notificationDisabled;

    public TelegramMessage() {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String  getChatId() {
        return chatId;
    }

    public void setChatId(String  chatId) {
        this.chatId = chatId;
    }

    public boolean isWebPagePreviewDisabled() {
        return webPagePreviewDisabled;
    }

    public void setWebPagePreviewDisabled(boolean webPagePreviewDisabled) {
        this.webPagePreviewDisabled = webPagePreviewDisabled;
    }

    public boolean isNotificationDisabled() {
        return notificationDisabled;
    }

    public void setNotificationDisabled(boolean notificationDisabled) {
        this.notificationDisabled = notificationDisabled;
    }

    @Override
    public String toString() {
        return "TelegramMessage{" +
            (message != null ? "message=" + message + ", " : "") +
            (chatId != null ? "chatId=" + chatId + ", " : "") +
            ("webPagePreviewDisabled=" + webPagePreviewDisabled + ", ") +
            ("notificationDisabled=" + notificationDisabled + ", ") +
            "}";
    }
}
