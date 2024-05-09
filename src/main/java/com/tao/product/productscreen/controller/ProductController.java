package com.tao.product.productscreen.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tao.product.productscreen.entity.beans.Product;
import com.tao.product.productscreen.exception.ProductNotFoundException;
import com.tao.product.productscreen.requestbean.ProductBean;
import com.tao.product.productscreen.responsebean.ProductResponse;
import com.tao.product.productscreen.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class ProductController {

	private ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping("/products")
	public ResponseEntity<ProductResponse<List<Product>>> getProducts() {
		List<Product> activeProducts = productService.getActiveProducts();
		if (activeProducts.size() > 0) {
			ProductResponse<List<Product>> productResponse = new ProductResponse<>();
			productResponse.setMessage("Product fetched results successfully");
			productResponse.setData(activeProducts);
			return new ResponseEntity<>(productResponse, HttpStatus.OK);
		} else {
			throw new ProductNotFoundException("Active products are not availabel. Please add new products");
		}
	}
	
	@GetMapping("/products/search")
	public ResponseEntity<ProductResponse<List<Product>>> searchProducts(@RequestParam(required = false) String name,
			@RequestParam(required = false) BigDecimal minPrice, 
			@RequestParam(required = false) BigDecimal maxPrice,
			@RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date minDate,
			@RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date maxDate) {
		List<Product> activeProducts = productService.searchProducts(name, minPrice, maxPrice, minDate, maxDate);
		if (activeProducts.size() > 0) {
			ProductResponse<List<Product>> productResponse = new ProductResponse<>();
			productResponse.setMessage("Product searched results successfully");
			productResponse.setData(activeProducts);
			return new ResponseEntity<>(productResponse, HttpStatus.OK);
		} else {
			throw new ProductNotFoundException("Active products are not availabel for the given search criteria.");
		}
	}
	
	@PostMapping("/products")
	public ResponseEntity<ProductResponse<Product>> createProduct(@RequestBody @Valid ProductBean productBean) {
		Product product = productService.createProduct(productBean);
		
		if(product != null) {
			ProductResponse<Product> productResponse = new ProductResponse<>();
			productResponse.setMessage("Product created successfully");
			productResponse.setData(product);
			return new ResponseEntity<>(productResponse, HttpStatus.CREATED);
		}
		else
			throw new ProductNotFoundException("Product creation failed...");
	}
	
	@PutMapping("/products/{productId}")
	public ResponseEntity<ProductResponse<Boolean>> editProduct(@RequestBody @Valid ProductBean productBean, @PathVariable long productId) {
		boolean isEdited = productService.editProduct(productBean, productId);
		if(isEdited) {
			ProductResponse<Boolean> productResponse = new ProductResponse<>();
			productResponse.setMessage("Product Edited successfully");
			productResponse.setData(isEdited);
			return new ResponseEntity<>(productResponse, HttpStatus.OK);
		}
		else
			throw new ProductNotFoundException("Product could not edit...");
	}
	
	@DeleteMapping("/products/{productId}")
	public ResponseEntity<ProductResponse<Boolean>> removeProduct(@PathVariable long productId) {
		boolean isDeleted = productService.removeProduct(productId);
		if(isDeleted) {
			ProductResponse<Boolean> productResponse = new ProductResponse<>();
			productResponse.setMessage("Product Deleted successfully");
			productResponse.setData(isDeleted);
			return new ResponseEntity<>(productResponse, HttpStatus.OK);
		}
		else
			throw new ProductNotFoundException("Product could not edit...");
	}
	
	
	@GetMapping("/products/approval-queue")
	public ResponseEntity<ProductResponse<List<Product>>> getProductsFromApprovalQueue() {
		List<Product> activeProducts = productService.getProductsFromApprovalQueue();
		if (activeProducts.size() > 0) {
			ProductResponse<List<Product>> productResponse = new ProductResponse<>();
			productResponse.setMessage("Product fetched results successfully from Approval Queue");
			productResponse.setData(activeProducts);
			return new ResponseEntity<>(productResponse, HttpStatus.OK);
		} else {
			throw new ProductNotFoundException("Active products are not availabel. Please add new products");
		}
	}
	
	@PutMapping("/products/approval-queue/{approvalId}/approve")
	public ResponseEntity<ProductResponse<Boolean>> approveProduct(@PathVariable long approvalId) {
		boolean isApproved = productService.approveProduct(approvalId);
		if(isApproved) {
			ProductResponse<Boolean> productResponse = new ProductResponse<>();
			productResponse.setMessage("Product approved and moved out of Approval Queue successfully");
			productResponse.setData(isApproved);
			return new ResponseEntity<>(productResponse, HttpStatus.OK);
		}
		else
			throw new ProductNotFoundException("Product could not approve...");
	}
	
	@PutMapping("/products/approval-queue/{approvalId}/reject")
	public ResponseEntity<ProductResponse<Boolean>> rejectProduct(@PathVariable long approvalId) {
		boolean isRejected = productService.rejectProduct(approvalId);
		if(isRejected) {
			ProductResponse<Boolean> productResponse = new ProductResponse<>();
			productResponse.setMessage("Product rejected and moved out of Approval Queue successfully");
			productResponse.setData(isRejected);
			return new ResponseEntity<>(productResponse, HttpStatus.OK);
		}
		else
			throw new ProductNotFoundException("Product could not approve...");
	}
}
