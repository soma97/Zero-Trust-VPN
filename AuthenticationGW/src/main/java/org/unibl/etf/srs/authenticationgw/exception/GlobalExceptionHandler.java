package org.unibl.etf.srs.authenticationgw.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnAuthorizedUserException.class)
    public ModelAndView handleUnauthorizedException(UnAuthorizedUserException ex) {
        return new ModelAndView("redirect:/");
    }

}
