package com.tao.product.productscreen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tao.product.productscreen.entity.beans.ApprovalQueue;

public interface ApprovalQueueRepository extends JpaRepository<ApprovalQueue, Long>{

	ApprovalQueue findByProductId(long productId);
	
	List<ApprovalQueue> findAllByDeleted(int delete);
	
}
