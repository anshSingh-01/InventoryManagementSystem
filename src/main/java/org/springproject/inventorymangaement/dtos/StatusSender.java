package org.springproject.inventorymangaement.dtos;

import org.springproject.inventorymangaement.enums.StatusCode;

public class StatusSender {

    private StatusCode serverStatus;
    private String message;
    private Object obj;

    public StatusSender(){

    }

    public StatusCode getStatusCode(){
        return serverStatus;
    }

    public String getMessage() {
        return message;
    }

    public Object getObj() {
        return obj;
    }

    public StatusSender(StatusCode serverStatus, String message, Object obj) {
        this.serverStatus = serverStatus;
        this.message = message;
        this.obj = obj;
    }

    @Override
    public String toString() {
        return "StatusSender{" +
                "serverStatus=" + serverStatus +
                ", message='" + message + '\'' +
                ", obj=" + obj +
                '}';
    }
}
