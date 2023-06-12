package com.library.controller;

import java.security.Principal;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.library.dto.UserDto;
import com.library.model.Role;
import com.library.model.User;
import com.library.service.UserService;

@Controller
public class LoginController {

	@Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "register";
    }


    @PostMapping("/do-register")
    public String processRegister(@Valid @ModelAttribute("userDto") UserDto userDto,
                                  BindingResult result,
                                  Model model) {
        try {
            if (result.hasErrors()) {
                model.addAttribute("userDto", userDto);
                return "register";
            }
            User user = userService.findByUsername(userDto.getUsername());
            if(user != null){
                model.addAttribute("username", "Email đã được sử dụng!");
                model.addAttribute("userDto",userDto);
                return "register";
            }
            if(userDto.getPassword().equals(userDto.getRepeatPassword())){
                userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
                userService.save(userDto);
                model.addAttribute("success", "Đăng ký thành công!");
                return "register";
            }else{
                model.addAttribute("password", "Mật khẩu không khớp!");
                model.addAttribute("userDto",userDto);
                return "register";
            }
        }catch (Exception e){
            model.addAttribute("error", "Lỗi hệ thống!");
            model.addAttribute("userDto",userDto);
        }
        return "register";
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String home(Model model, Principal principal, HttpSession session){
		session.setAttribute("username", principal.getName());
        User user = userService.findByUsername(principal.getName());
        for (Role role: user.getRoles()) {
        	if(role.getName().equals("ADMIN")) {
        		return "redirect:/admin";
        	}
        }
        return "redirect:/user";
    }
}
