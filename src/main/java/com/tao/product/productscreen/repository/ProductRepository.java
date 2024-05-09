package com.tao.product.productscreen.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tao.product.productscreen.entity.beans.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
	
	List<Product> findAllByDeleted(int delete);
	List<Product> findByStatusOrderByCreationDate(String status);
	
	//@Query("select p from Product p where (name is null or p.name=:name) and (:price is null or :price between ) and (:date is null or d.date=:date)") 
	List<Product> findByNameOrPriceBetweenOrCreationDateBetweenAndDeleted(String name, BigDecimal minPrice, BigDecimal maxPrice, Date minDate, Date maxDate, int deleted);

}
