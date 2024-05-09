package com.tao.product.productscreen.exception;

import lombok.Data;

@Data
public class ExceptionResponseBean {
	private String message;
	public ExceptionResponseBean(String message) {
		super();
		this.message = message;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "ExceptionResponseBean [message=" + message + "]";
	}
    
	
}
