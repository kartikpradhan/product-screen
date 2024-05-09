package com.tao.product.productscreen.mapper;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.tao.product.productscreen.constant.ProductConstants;
import com.tao.product.productscreen.entity.beans.Product;
import com.tao.product.productscreen.requestbean.ProductBean;

@Component
public class ProductMapper {
	
	public void productMap(ProductBean productBean, Product product) {
		product.setName(productBean.getName());
		product.setPrice(productBean.getPrice());
		if(productBean.getPrice().compareTo(new BigDecimal(ProductConstants.PRICE_5000)) >= 1) {
			product.setStatus(ProductConstants.STATUS_QUEUED);
		} else {
			product.setStatus(ProductConstants.STATUS_PENDING);
		}
		product.setCreationDate(new Date());
	}

}
