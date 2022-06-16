package com.mygdx.game;

public class ServerResponse {
    public int statusCode;
    public String message;

    public ServerResponse() {
        message = new String();
    }

    public ServerResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
