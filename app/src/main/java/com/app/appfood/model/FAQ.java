package com.app.appfood.model;

public class FAQ {

    private String question;
    private String category;

    // Constructor
    public FAQ(String question, String category) {
        this.question = question;
        this.category = category;
    }

    // Getter và Setter
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
