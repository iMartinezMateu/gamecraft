package com.gamecraft.service.dto;

public class Email {
    private String toEmailAddress;
    private String subject;
    private String body;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getToEmailAddress() {
        return toEmailAddress;
    }

    public void setToEmailAddress(String toEmailAddress) {
        this.toEmailAddress = toEmailAddress;
    }

    public Email() {

    }

    @Override
    public String toString() {
        return "EmailAccountCriteria{" +
            (subject != null ? "subject=" + subject + ", " : "") +
            (body != null ? "body=" + body + ", " : "") +
            (toEmailAddress != null ? "toEmailAddress=" + toEmailAddress + ", " : "") +
            "}";
    }
}
