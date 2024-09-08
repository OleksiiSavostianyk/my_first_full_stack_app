package com.alex.spring_myfirstpetproject.exception.handler;

public class WrappedException {

    private String info;

    public WrappedException(){}


    public WrappedException(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
