package com.tao.product.productscreen.exception;

public class ProductNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	private String message;
	public ProductNotFoundException(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "ProductNotFoundException [message=" + message + "]";
	}
}
