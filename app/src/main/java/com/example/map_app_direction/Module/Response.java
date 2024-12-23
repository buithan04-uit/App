package com.example.map_app_direction.Module;

public class Response {
    private Boolean error;
    private User user;
    private String message;

    public Response(Boolean error, User user, String message) {
        this.error = error;
        this.user = user;
        this.message = message;
    }

    public Boolean getError() {
        return error;
    }

    public User getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
