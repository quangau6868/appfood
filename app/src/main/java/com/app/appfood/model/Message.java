package com.app.appfood.model;

public class Message {
    private String content;
    private boolean isFromUser;
    private String adminResponse; // Thêm trường phản hồi của admin

    // Constructor
    public Message(String content, boolean isFromUser) {
        this.content = content;
        this.isFromUser = isFromUser;
    }

    // Getter và Setter cho adminResponse
    public String getAdminResponse() {
        return adminResponse;
    }

    public void setAdminResponse(String adminResponse) {
        this.adminResponse = adminResponse;
    }

    public String getContent() {
        return content;
    }

    public boolean isFromUser() {
        return isFromUser;
    }
}
