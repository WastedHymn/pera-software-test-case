package com.kadiryuksel.peratestcase.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Data
public class Response {
    private final int code;
    private final HttpStatus status;
    private final String message;
}
