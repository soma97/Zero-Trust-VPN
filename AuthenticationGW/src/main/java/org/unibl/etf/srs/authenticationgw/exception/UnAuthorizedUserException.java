package org.unibl.etf.srs.authenticationgw.exception;

import lombok.Getter;

@Getter
public class UnAuthorizedUserException extends RuntimeException {

    private String message;

    public UnAuthorizedUserException(String message) {
        this.message = message;
    }
}
