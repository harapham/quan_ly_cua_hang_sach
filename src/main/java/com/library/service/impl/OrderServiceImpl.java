package com.library.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.model.ShoppingCart;
import com.library.model.CartItem;
import com.library.model.Category;
import com.library.model.Order;
import com.library.model.OrderDetail;
import com.library.model.Product;
import com.library.repository.CartItemRepository;
import com.library.repository.CategoryRepository;
import com.library.repository.OrderDetailRepository;
import com.library.repository.OrderRepository;
import com.library.repository.ProductRepository;
import com.library.repository.ShoppingCartRepository;

import com.library.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService{

	@Autowired
	private OrderDetailRepository orderDetailRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private ShoppingCartRepository shoppingCartRepository;
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	@Override
	public void saveOrder(ShoppingCart cart) {
		// TODO Auto-generated method stub
		Order order = new Order();
		order.setOrderStatus("Pending");
		order.setOrderDate(new Date());
		order.setUser(cart.getUser());
		order.setTotalPrice(cart.getTotalPrices());
		List<OrderDetail> orderDetails = new ArrayList<>();
		for (CartItem item : cart.getCartItem()) {
			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setOrder(order);
			orderDetail.setQuantity(item.getQuantity());
			orderDetail.setProduct(item.getProduct());
			orderDetailRepository.save(orderDetail);
			orderDetails.add(orderDetail);
			cartItemRepository.delete(item);
		}order.setOrderDetailList(orderDetails);
		for(OrderDetail orderDetail: order.getOrderDetailList()) {
			Product product = orderDetail.getProduct();
			product.setSale(product.getSale()+orderDetail.getQuantity());
			productRepository.save(product);
		}
		
		cart.setCartItem(new HashSet<>());
		cart.setTotalItems(0);
		cart.setTotalPrices(0);
		shoppingCartRepository.save(cart);
		orderRepository.save(order);
	}

	@Override
	public void acceptOrderA(Long id) {
		Order order = orderRepository.findById(id).get();
		order.setDeliveryDate(new Date());
		order.setOrderStatus("Shipping");
		orderRepository.save(order);
	}
	@Override
	public void acceptOrderC(Long id) {
		Order order = orderRepository.findById(id).get();
		order.setDeliveryDate(new Date());
		order.setOrderStatus("Success");
		for(OrderDetail orderDetail: order.getOrderDetailList()) {
			Product product = orderDetail.getProduct();
			product.setSale(product.getSale()+orderDetail.getQuantity());
			productRepository.save(product);
			
			Category category= product.getCategory();
			
			categoryRepository.save(category);
		}
		orderRepository.save(order);
	}

	@Override
	public void cancelOrder(Long id) {
		// TODO Auto-generated method stub
		Order order = orderRepository.findById(id).get();
		order.setOrderStatus("Cancel");
		for(OrderDetail orderDetail: order.getOrderDetailList()) {
			Product product = orderDetail.getProduct();
			product.setSale(product.getSale()-orderDetail.getQuantity());
			productRepository.save(product);
		}
		orderRepository.save(order);
		
	}

	@Override
	public Optional<Order> findById(Long id) {
		return orderRepository.findById(id);
	}

	@Override
	public List<Order> findAll() {
		return orderRepository.findAll();
	}
	
}
