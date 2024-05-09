package com.tao.product.productscreen.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = ProductNotFoundException.class)
    public ResponseEntity<ExceptionResponseBean> handleProductNotFoundException(ProductNotFoundException ex) {
        ExceptionResponseBean errorResponse = new ExceptionResponseBean(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NO_CONTENT);
    }
}
