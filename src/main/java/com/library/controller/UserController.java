package com.library.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.library.model.Category;
import com.library.model.Order;
import com.library.model.Product;
import com.library.model.ShoppingCart;
import com.library.model.User;
import com.library.model.Rate;
import com.library.service.CategoryService;
import com.library.service.OrderService;
import com.library.service.ProductService;
import com.library.service.RateService;
import com.library.service.ShoppingCartService;
import com.library.service.UserService;

@Controller
public class UserController {
	@Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ShoppingCartService cartService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private RateService rateService;
//books
    @GetMapping(value = {"/user/books","/user"})
    public String products(Model model, Principal principal, HttpSession session){
    	
    	if(principal != null){
            session.setAttribute("username", principal.getName());
            User user = userService.findByUsername(principal.getName());
            ShoppingCart cart = user.getShoppingCart();
            if (cart == null) {
            	cart = new ShoppingCart();
            }
            session.setAttribute("totalItems", cart.getTotalItems());
        }else{
            session.removeAttribute("username");
        }
        List<Category> categories = categoryService.findAll();
        List<Product> products = productService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("products", products);
        return "user/products";
    }

    @GetMapping(value = "/user/category/{id}")
    public String cate(Model model, Principal principal, HttpSession session
    		,@PathVariable("id") Long id){
    	
    	if(principal != null){
            session.setAttribute("username", principal.getName());
            User user = userService.findByUsername(principal.getName());
            ShoppingCart cart = user.getShoppingCart();
            if (cart == null) {
            	cart = new ShoppingCart();
            }
            session.setAttribute("totalItems", cart.getTotalItems());
        }else{
            session.removeAttribute("username");
        }
        List<Category> categories = categoryService.findAll();
        List<Product> products = productService.getProductsInCategory(id);
        model.addAttribute("categories", categories);
        model.addAttribute("products", products);
        return "user/products-in-cate";
    }
    @GetMapping("/user/books/{id}")
    public String findProductById(@PathVariable("id") Long id, Model model, 
    		Principal principal, HttpSession session){
    	
    	if(principal != null){
            session.setAttribute("username", principal.getName());
            User user = userService.findByUsername(principal.getName());
            ShoppingCart cart = user.getShoppingCart();
            if (cart == null) {
            	cart = new ShoppingCart();
            }
            
            session.setAttribute("totalItems", cart.getTotalItems());
        }else{
            session.removeAttribute("username");
        }
        Product product = productService.getProductById(id);
        Long categoryId = product.getCategory().getId();
        
        List<Rate> rates = rateService.findByProduct(id);
        
        for (Rate i :rates) {
        	System.out.print(i.getStar());
        }
        model.addAttribute("rates", rates);
        model.addAttribute("product", product);
        model.addAttribute("rate", new Rate());
        return "user/product-detail";
    }

    
    @PostMapping("/user/books/{id}")
    public String Rate(@PathVariable("id") Long id, Model model, 
    		Principal principal, HttpSession session, @ModelAttribute("rate") Rate rate){
    	
    	Rate rate2 = new Rate();
    	rate2.setComment(rate.getComment());
    	rate2.setStar(rate.getStar());
    	rate2.setProduct(productService.getProductById(id));
    	rate2.setUser(userService.findByUsername(principal.getName()));
    	
    	Rate rateS = rateService.save(rate2);
        return "redirect:/user/books/{id}";
    }
//account    

    @GetMapping("/user/account")
    public String accountHome(Model model , Principal principal){
        if(principal == null){
            return "redirect:/login";
        }
        String username = principal.getName();
        User user = userService.findByUsername(username);
        model.addAttribute("user", user);

        return "user/account";
    }

    @PostMapping(value = "/user/account/update-infor")
    public String updateUser(
            @ModelAttribute("user") User user,
            Model model,
            RedirectAttributes redirectAttributes,
            Principal principal){
        if(principal == null){
            return "redirect:/login";
        }
        User userSaved = userService.saveInfor(user);

        redirectAttributes.addFlashAttribute("user", userSaved);

        return "redirect:/user/account";
    }
    
// cart
    @GetMapping("/user/cart")
    public String cart(Model model, Principal principal, HttpSession session){
        if(principal == null){
            return "redirect:/login";
        }
        String username = principal.getName();
        User user = userService.findByUsername(username);
        ShoppingCart shoppingCart = user.getShoppingCart();
        if(shoppingCart == null){
            model.addAttribute("check", "Giỏ hàng trống!");
        }
        session.setAttribute("totalItems", shoppingCart.getTotalItems());
        model.addAttribute("subTotal", shoppingCart.getTotalPrices());
        model.addAttribute("shoppingCart", shoppingCart);
        return "user/cart";
    }


    @PostMapping("/user/add-to-cart")
    public String addItemToCart(
            @RequestParam("id") Long productId,
            @RequestParam(value = "quantity", required = false, defaultValue = "1") int quantity,
            Principal principal,
            HttpServletRequest request, RedirectAttributes attributes){

        if(principal == null){
            return "redirect:/login";
        }
        Product product = productService.getProductById(productId);
        String username = principal.getName();
        User user = userService.findByUsername(username);

        	ShoppingCart cart = cartService.addItemToCart(product, quantity, user);
        	attributes.addFlashAttribute("success","Thêm thành công!");

        return "redirect:" + request.getHeader("Referer");


    }


    @RequestMapping(value = "/user/update-cart", method = RequestMethod.POST, params = "action=update")
    public String updateCart(@RequestParam("quantity") int quantity,
                             @RequestParam("id") Long productId,
                             Model model,
                             Principal principal, RedirectAttributes attributes
                             ){

        if(principal == null){
            return "redirect:/login";
        }else{
            String username = principal.getName();
            User user = userService.findByUsername(username);
            Product product = productService.getProductById(productId);

            	ShoppingCart cart = cartService.updateItemInCart(product, quantity, user);
            	model.addAttribute("shoppingCart", cart);
            	attributes.addFlashAttribute("success","Cập nhật thành công!");

            return "redirect:/user/cart";
        }

    }


    @RequestMapping(value = "/user/update-cart", method = RequestMethod.POST, params = "action=delete")
    public String deleteItemFromCart(@RequestParam("id") Long productId,
                                     Model model,
                                     Principal principal){
        if(principal == null){
            return "redirect:/login";
        }else{
            String username = principal.getName();
            User  user = userService.findByUsername(username);
            Product product = productService.getProductById(productId);
            ShoppingCart cart = cartService.deleteItemFromCart(product, user);
            model.addAttribute("shoppingCart", cart);
            return "redirect:/user/cart";
        }

    }

//order
    
    @GetMapping("/user/check-out")
	public String checkout(Model model, Principal principal) {
		if (principal == null) {
			return "redirect:/login";
		}
		String username = principal.getName();
		User user = userService.findByUsername(username);
		if (user.getPhoneNumber() == null || user.getAddress() == null ) {

			model.addAttribute("user", user);
			model.addAttribute("error", "Vui lòng cập nhật đầy đủ thông tin!");
			return "user/account";
		} else {
			model.addAttribute("user", user);
			ShoppingCart cart = user.getShoppingCart();
			model.addAttribute("cart", cart);
		}
		return "user/checkout";
	}

	@GetMapping("/user/order")
	public String order(Principal principal, Model model) {

		if (principal == null) {
			return "redirect:/login";
		}

		String username = principal.getName();
		User user = userService.findByUsername(username);
		List<Order> orders = user.getOrders();
		model.addAttribute("orders", orders);
		return "user/order";
	}

	@GetMapping("/user/save-order")
	public String saveOrder(Principal principal) {
		if (principal == null) {
			return "redirect:/login";
		}

		String username = principal.getName();
		User user = userService.findByUsername(username);
		ShoppingCart cart = user.getShoppingCart();
		orderService.saveOrder(cart);
		return "redirect:/user/order";

	}

	@GetMapping("/user/accept-order")
	public String acceptOrder(@RequestParam("id") Long id) {
		orderService.acceptOrderC(id);

		return "redirect:/user/order";
	}

	@GetMapping("/user/cancel-order")
	public String cancelOrder(@RequestParam("id") Long id) {
		orderService.cancelOrder(id);

		return "redirect:/user/order";
	}

	@GetMapping("/user/view-order/{id}")
	public String viewOrder(@PathVariable("id") Long id, Model model, Principal principal) {
		if (principal == null) {
			return "redirect:/login";
		}
		String username = principal.getName();
		User user = userService.findByUsername(username);
		model.addAttribute("user", user);
		Order order = orderService.findById(id).get();
		model.addAttribute("order",order);

		return "user/view-order";
	}
    
}
