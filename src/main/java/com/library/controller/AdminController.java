package com.library.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.library.dto.ProductDto;
import com.library.model.Category;
import com.library.model.Product;
import com.library.model.Role;
import com.library.model.User;
import com.library.service.CategoryService;
import com.library.service.ProductService;
import com.library.service.UserService;


@Controller
public class AdminController {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductService productService;
	
	@Autowired
	private UserService userService;

//category
	@GetMapping("/admin/categories")
	public String getCategory(Model model, Principal principal, HttpSession session) {
		if(principal != null){
           
            
            User user = userService.findByUsername(principal.getName());
            boolean checkAdmin = false;
            for(Role x : user.getRoles()) {
            	if(x.getName().equals("ADMIN")) {
            		checkAdmin= true;
            	}
            }
            if(checkAdmin==true) {
            	 session.setAttribute("username", principal.getName());
            }
            else session.removeAttribute("username");
            
		}else{
            session.removeAttribute("username");
        }
		
		List<Category> categories = categoryService.findAll();
		model.addAttribute("categories",categories);
		model.addAttribute("size", categories.size());
		
		return "admin/categories";
	}
	
	@GetMapping("/admin/categories/add")
	public String getAddCategory(Model model, Principal principal, HttpSession session) {
		if(principal != null){
			User user = userService.findByUsername(principal.getName());
            boolean checkAdmin = false;
            for(Role x : user.getRoles()) {
            	if(x.getName().equals("ADMIN")) {
            		checkAdmin= true;
            	}
            }
            if(checkAdmin==true) {
            	model.addAttribute("category", new Category());
            	return "admin/category-add"; 
            } 
        }
		session.removeAttribute("username");
		return "redirect:/login";
	}

	@PostMapping("/admin/categories/add")
	public String postAddCategory(@ModelAttribute("category") Category category, RedirectAttributes attributes) {
		try {
			categoryService.save(category);
			attributes.addFlashAttribute("success", "Thêm thể loại mới thành công!");

		} catch (DataIntegrityViolationException e) {
			attributes.addFlashAttribute("failed", "Thể loại đã tồn tại!");
			// TODO: handle exception
		} catch (Exception e) {
			// TODO: handle exception
			attributes.addFlashAttribute("failed", "Lỗi hệ thống!");
		}

		return "redirect:/admin/categories";
	}

	@GetMapping("/admin/categories/delete/{id}")
	public String deleteCategory(@PathVariable Long id, RedirectAttributes attributes, Principal principal, HttpSession session) {
		if(principal != null){
			User user = userService.findByUsername(principal.getName());
            boolean checkAdmin = false;
            for(Role x : user.getRoles()) {
            	if(x.getName().equals("ADMIN")) {
            		checkAdmin= true;
            	}
            }
            if(checkAdmin==true) {
            	try {
        			categoryService.deleteById(id);
        			attributes.addFlashAttribute("success", "Xóa thể loại thành công!");
        		} catch (Exception e) {
        			// TODO: handle exception
        			attributes.addFlashAttribute("failed", "Lỗi hệ thống!");
        		}
        		
        		return "redirect:/admin/categories";
            } 
        }
		session.removeAttribute("username");
		return "redirect:/login";
		

	}

	@GetMapping("/admin/categories/update/{id}")
	public String getUpdateCategory(@PathVariable Long id, Model model, Principal principal, HttpSession session) {
		
		if(principal != null){
			User user = userService.findByUsername(principal.getName());
            boolean checkAdmin = false;
            for(Role x : user.getRoles()) {
            	if(x.getName().equals("ADMIN")) {
            		checkAdmin= true;
            	}
            }
            if(checkAdmin==true) {
            	Category category = categoryService.findById(id);
        		model.addAttribute("category", category);
        		return "admin/category-update";
            } 
        }
		session.removeAttribute("username");
		return "redirect:/login";
		
		
	}

	@PostMapping("/admin/categories/update/{id}")
	public String postUpdateCategory(@ModelAttribute("category") Category category, 
			@PathVariable("id") int id, 
			RedirectAttributes attributes) {
		try {
			categoryService.update(category);
			attributes.addFlashAttribute("success", "Cập nhật thể loại thành công!");

		}  catch (Exception e) {
			// TODO: handle exception
			attributes.addFlashAttribute("failed", "Lỗi hệ thống!");
		}

		return "redirect:/admin/categories";
	}
//book
	@GetMapping(value = {"/admin/books","/admin"})
	public String getProduct(Model model, Principal principal, HttpSession session) {
		if(principal != null){
           
            
            User user = userService.findByUsername(principal.getName());
            boolean checkAdmin = false;
            for(Role x : user.getRoles()) {
            	if(x.getName().equals("ADMIN")) {
            		checkAdmin= true;
            	}
            }
            if(checkAdmin==true) {
            	 session.setAttribute("username", principal.getName());
            }
            else session.removeAttribute("username");
            
		}else{
            session.removeAttribute("username");
        }
		
		List<Product> products = productService.findAll();
		model.addAttribute("products", products);
		model.addAttribute("size", products.size());
		
		return "admin/products";
	}
	@GetMapping("/admin/books/{id}")
	public String getView(@PathVariable("id") Long id, Model model, Principal principal,HttpSession session) {
		if(principal != null){
			User user = userService.findByUsername(principal.getName());
            boolean checkAdmin = false;
            for(Role x : user.getRoles()) {
            	if(x.getName().equals("ADMIN")) {
            		checkAdmin= true;
            	}
            }
            if(checkAdmin==true) {
            	model.addAttribute("title", "Update products");
                List<Category> categories = categoryService.findAll();
                ProductDto productDto = productService.getById(id);
                model.addAttribute("categories", categories);
                model.addAttribute("product", productDto);
                return "admin/product-view";
            } 
        }
		session.removeAttribute("username");
		return "redirect:/login";
		
        
	}
	@GetMapping("/admin/books/add")
	public String getAddBook(Model model, Principal principal, HttpSession session) {
		if(principal != null){
			User user = userService.findByUsername(principal.getName());
            boolean checkAdmin = false;
            for(Role x : user.getRoles()) {
            	if(x.getName().equals("ADMIN")) {
            		checkAdmin= true;
            	}
            }
            if(checkAdmin==true) {
            	List<Category> categories = categoryService.findAll();
                model.addAttribute("categories", categories);
                model.addAttribute("product", new ProductDto());
                return "admin/product-add";
            } 
        }
		session.removeAttribute("username");
		return "redirect:/login";
		
        
	}

	@PostMapping("/admin/books/add")
	public String postAddBook(@ModelAttribute("product") ProductDto productDto,
			@RequestParam("productImage") MultipartFile imageFile, 
			RedirectAttributes attributes){

		try {
			productService.save(imageFile, productDto);
			attributes.addFlashAttribute("success", "Thêm sách mới thành công!");
		}catch (DataIntegrityViolationException e) {
			// TODO: handle exception
			attributes.addFlashAttribute("failed", "Sách đã tồn tại!");
		} catch (Exception e) {
			// TODO: handle exception
			attributes.addFlashAttribute("failed", "Lỗi hệ thống");
			e.printStackTrace();
		}
		return "redirect:/admin/books";
	}
	
	@GetMapping("/admin/books/delete/{id}")
	public String deleteBook(@PathVariable Long id,
			HttpSession session, RedirectAttributes attributes, Principal principal) {
		
		if(principal != null){
			User user = userService.findByUsername(principal.getName());
            boolean checkAdmin = false;
            for(Role x : user.getRoles()) {
            	if(x.getName().equals("ADMIN")) {
            		checkAdmin= true;
            	}
            }
            if(checkAdmin==true) {
            	try {
        			productService.deleteById(id);
        			attributes.addFlashAttribute("success", "Xóa sách thành công!");
        		} catch (Exception e) {
        			// TODO: handle exception
        			attributes.addFlashAttribute("failed", "Lỗi hệ thống!");
        		}
        		return "redirect:/admin/books";
            } 
        }
		session.removeAttribute("username");
		return "redirect:/login";
		
	}

	@GetMapping("/admin/books/update/{id}")
	public String getUpdateBook(@PathVariable("id") Long id, 
			HttpSession session, Model model, Principal principal){
        
		if(principal != null){
			User user = userService.findByUsername(principal.getName());
            boolean checkAdmin = false;
            for(Role x : user.getRoles()) {
            	if(x.getName().equals("ADMIN")) {
            		checkAdmin= true;
            	}
            }
            if(checkAdmin==true) {
            	 model.addAttribute("title", "Update products");
                 List<Category> categories = categoryService.findAll();
                 ProductDto productDto = productService.getById(id);
                 model.addAttribute("categories", categories);
                 model.addAttribute("product", productDto);
                 return "admin/product-update";
            } 
        }
		session.removeAttribute("username");
		return "redirect:/login";
		
       
    }

	@PostMapping("/admin/books/update/{id}")
	public String postAddBook(@PathVariable("id") Long id,
            @ModelAttribute("product") ProductDto productDto,
            @RequestParam("productImage")MultipartFile imageProduct,
            RedirectAttributes attributes
            ){

		try {
			productService.update(imageProduct, productDto);
			attributes.addFlashAttribute("success", "Cập nhật sách thành công!");
		}catch (Exception e) {
			// TODO: handle exception
			attributes.addFlashAttribute("failed", "Lỗi hệ thống");
			e.printStackTrace();
		}
		return "redirect:/admin/books";
	}
	
	
}
