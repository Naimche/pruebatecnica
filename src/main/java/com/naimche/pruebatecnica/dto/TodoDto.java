package com.naimche.pruebatecnica.dto;

public class TodoDto {

    private Long id;
    private String title;
    private boolean completed;
    private Long userId;
    private String username;
    private String country;

    //region getters and setters

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    //endregion

    public TodoDto() {
    }

    public TodoDto(Long id, String title, boolean completed, Long userId, String username, String country) {
        this.id = id;
        this.title = title;
        this.completed = completed;
        this.userId = userId;
        this.username = username;
        this.country = country;
    }

    public TodoDto(String title, boolean completed, Long userId){
        this.title = title;
        this.completed = completed;
        this.userId = userId;
    }

}