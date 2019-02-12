package com.lamechat.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;


@JsonAutoDetect
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum AuthStatus {
    NICK_IS_ALREADY_IN_USE("NICK_IS_ALREADY_IN_USE"),
    EMAIL_IS_ALREADY_IN_USE("EMAIL_IS_ALREADY_IN_USE"),
    WRONG_PASSWORD("WRONG_PASSWORD"),
    WRONG_EMAIL("WRONG_EMAIL"),
    AUTHORIZED("AUTHORIZED");

    private String status;

    AuthStatus(String status) {
        this.status = status;
    }

    public String getStatus(){
        return status;
    }
}
