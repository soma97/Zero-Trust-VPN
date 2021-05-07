package org.unibl.etf.srs.cmanager.exception;

import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.operator.OperatorCreationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CRLException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity handleNotFoundException(FileNotFoundException ex) { return notFound(); }

    @ExceptionHandler(CertificateException.class)
    public ResponseEntity handleCertificateException(CertificateException ex) {
        return notFound();
    }

    @ExceptionHandler(CertificateEncodingException.class)
    public ResponseEntity handleCertificateEncodingException(CertificateEncodingException ex) {
        return notFound();
    }

    @ExceptionHandler(CRLException.class)
    public ResponseEntity handleCRLException(CRLException ex) {
        return notFound();
    }

    @ExceptionHandler(NoSuchAlgorithmException.class)
    public ResponseEntity handleNoSuchAlgorithmException(NoSuchAlgorithmException ex) {
        return notFound();
    }

    @ExceptionHandler(CertIOException.class)
    public ResponseEntity handleCertIOException(CertIOException ex) {
        return notFound();
    }

    @ExceptionHandler(OperatorCreationException.class)
    public ResponseEntity handleOperatorCreationException(OperatorCreationException ex) {
        return notFound();
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity handleIOException(IOException ex) {
        return notFound();
    }

    private ResponseEntity notFound() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .build();
    }

}
