package com.tao.product.productscreen.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tao.product.productscreen.constant.ProductConstants;
import com.tao.product.productscreen.entity.beans.ApprovalQueue;
import com.tao.product.productscreen.entity.beans.Product;
import com.tao.product.productscreen.mapper.ProductMapper;
import com.tao.product.productscreen.repository.ApprovalQueueRepository;
import com.tao.product.productscreen.repository.ProductRepository;
import com.tao.product.productscreen.requestbean.ProductBean;
import com.tao.product.productscreen.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	private ProductRepository productRepository;
	private ApprovalQueueRepository approvalQueueRepository;
	private ProductMapper productMapper;

	public ProductServiceImpl(ProductRepository productRepository, ApprovalQueueRepository approvalQueueRepository,
			ProductMapper productMapper) {
		this.productRepository = productRepository;
		this.approvalQueueRepository = approvalQueueRepository;
		this.productMapper = productMapper;
	}

	@Override
	public List<Product> getActiveProducts() {
		List<Product> all = productRepository.findAllByDeleted(ProductConstants.DELETED_ZERO);
		Collections.sort(all, Comparator.comparing(Product::getCreationDate).reversed());
		return all;
		
	}

	@Override
	public List<Product> searchProducts(String name, BigDecimal minPrice, BigDecimal maxPrice, Date minDate,
			Date maxDate) {

		return productRepository.findByNameOrPriceBetweenOrCreationDateBetweenAndDeleted(name, minPrice, maxPrice, minDate,
				maxDate, ProductConstants.DELETED_ZERO);
	}

	@Transactional
	@Override
	public Product createProduct(ProductBean productBean) {
		Product product = new Product();
		productMapper.productMap(productBean, product);
		Product savedP = productRepository.save(product);
		if (savedP != null && ProductConstants.STATUS_QUEUED.equalsIgnoreCase(product.getStatus())) {
			ApprovalQueue aq = new ApprovalQueue();
			aq.setCreationDate(new Date());
			aq.setStatus(savedP.getStatus());
			aq.setProductId(savedP.getId());
			ApprovalQueue save = approvalQueueRepository.save(aq);
			if (save != null) {
				System.out.println("ApprovalQueue saved.....");
			}
		}
		return savedP;
	}

	@Override
	public boolean editProduct(ProductBean productBean, long productId) {
		Optional<Product> byId = productRepository.findById(productId);

		if (byId.isPresent()) {
			Product product = byId.get();
			productMapper.productMap(productBean, product);
			product.setId(productId);
			Product savedP = productRepository.save(product);
			if (percentage(productBean.getPrice(), product.getPrice())) {

				ApprovalQueue byProductId = approvalQueueRepository.findByProductId(savedP.getId());
				if (byProductId != null) {
					byProductId.setCreationDate(new Date());
					byProductId.setStatus(ProductConstants.STATUS_QUEUED);
					approvalQueueRepository.save(byProductId);
				} else {
					ApprovalQueue aq = new ApprovalQueue();
					aq.setCreationDate(new Date());
					aq.setStatus(ProductConstants.STATUS_QUEUED);
					aq.setProductId(savedP.getId());
					approvalQueueRepository.save(aq);
				}
			}

			if (savedP != null)
				return true;
		}
		return false;
	}

	private static boolean percentage(BigDecimal newPrice, BigDecimal base) {
		BigDecimal TWO = new BigDecimal(2);
		return newPrice.compareTo(base.divide(TWO).add(base)) >= 1;
	}

	@Override
	public boolean removeProduct(long productId) {
		Optional<Product> byId = productRepository.findById(productId);
		boolean isDeleted = false;
		if (byId.isPresent()) {
			Product product = byId.get();
			product.setDeleted(ProductConstants.DELETED_ONE);
			productRepository.save(product);
			isDeleted = true;
			ApprovalQueue byProductId = approvalQueueRepository.findByProductId(productId);
			if (byProductId == null) {
				ApprovalQueue aq = new ApprovalQueue();
				aq.setCreationDate(new Date());
				aq.setStatus(ProductConstants.STATUS_QUEUED);
				aq.setProductId(productId);
				approvalQueueRepository.save(aq);
			}
		}
		return isDeleted;
	}

	@Override
	public List<Product> getProductsFromApprovalQueue() {
		List<ApprovalQueue> all = approvalQueueRepository.findAllByDeleted(ProductConstants.DELETED_ZERO);
		List<Product> products = new ArrayList<>();
		for(ApprovalQueue aq: all) {
			Optional<Product> product = productRepository.findById(aq.getProductId());
			if(product.isPresent())
				products.add(product.get());
		}
		return products;
	}

	@Override
	public boolean approveProduct(long approvalId) {
		Optional<ApprovalQueue> byId = approvalQueueRepository.findById(approvalId);
		if(byId.isPresent()) {
			ApprovalQueue approvalQueue = byId.get();
			Optional<Product> byId2 = productRepository.findById(approvalQueue.getProductId());
			if(byId2.isPresent()) {
				Product product = byId2.get();
				product.setStatus(ProductConstants.STATUS_APPROVED);
				productRepository.save(product);
			}
			approvalQueue.setDeleted(ProductConstants.DELETED_ONE);
			approvalQueue.setStatus(ProductConstants.STATUS_APPROVED);
			approvalQueueRepository.save(approvalQueue);
			return true;
		}
		return false;
	}

	@Override
	public boolean rejectProduct(long approvalId) {
		Optional<ApprovalQueue> byId = approvalQueueRepository.findById(approvalId);
		if(byId.isPresent()) {
			ApprovalQueue approvalQueue = byId.get();
			approvalQueue.setDeleted(ProductConstants.DELETED_ONE);
			approvalQueueRepository.save(approvalQueue);
			return true;
		}
		return false;
	}

}
