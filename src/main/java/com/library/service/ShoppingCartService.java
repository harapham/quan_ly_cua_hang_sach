package com.library.service;

import com.library.model.User;
import com.library.model.Product;
import com.library.model.ShoppingCart;

public interface ShoppingCartService {
    ShoppingCart addItemToCart(Product product, int quantity, User user);

    ShoppingCart updateItemInCart(Product product, int quantity, User user);

    ShoppingCart deleteItemFromCart(Product product, User user);
}
