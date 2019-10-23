package com.github.modsezam.nitritedemo.model;

public class ResponseWrapper <T> {
    T response;

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }
}
