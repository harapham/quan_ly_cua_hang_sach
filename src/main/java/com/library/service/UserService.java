package com.library.service;

import java.util.List;

import com.library.dto.UserDto;
import com.library.model.User;

public interface UserService {

    UserDto save(UserDto userDto);

    User findByUsername(String username);

    User saveInfor(User user);

	List<User> findAll();

	User findById(Long id);

	User update(User user);

}
