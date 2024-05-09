package com.tao.product.productscreen.requestbean;

import java.math.BigDecimal;

import jakarta.validation.constraints.Max;
import lombok.Data;

@Data
public class ProductBean {
	private String name;
	@Max(10000)
	private BigDecimal price;
	private String status;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
