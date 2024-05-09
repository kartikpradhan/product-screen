package com.tao.product.productscreen.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.tao.product.productscreen.entity.beans.Product;
import com.tao.product.productscreen.requestbean.ProductBean;

import jakarta.validation.Valid;

public interface ProductService {
	List<Product> getActiveProducts();
	List<Product> searchProducts(String name, BigDecimal minPrice, BigDecimal maxPrice, Date minDate, Date maxDate);
	Product createProduct(ProductBean product);
	boolean editProduct(ProductBean productBean, long productId);
	boolean removeProduct(long productId);
	List<Product> getProductsFromApprovalQueue();
	boolean approveProduct(long approvalId);
	boolean rejectProduct(long approvalId);
}
