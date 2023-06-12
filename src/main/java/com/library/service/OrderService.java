package com.library.service;

import java.util.List;
import java.util.Optional;

import com.library.model.Order;
import com.library.model.ShoppingCart;


public interface OrderService {

	void saveOrder(ShoppingCart cart);
	

	
	void cancelOrder(Long id);

	Optional<Order> findById(Long id);

	List<Order> findAll();

	void acceptOrderC(Long id);

	void acceptOrderA(Long id);

	

}
