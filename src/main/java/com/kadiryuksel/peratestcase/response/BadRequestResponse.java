package com.kadiryuksel.peratestcase.response;

import org.springframework.http.HttpStatus;

public class BadRequestResponse extends Response{
    public BadRequestResponse(String message) {
        super(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, message);
    }
}
