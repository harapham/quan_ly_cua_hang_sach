package com.library.service.impl;


import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.dto.UserDto;
import com.library.model.User;
import com.library.repository.UserRepository;
import com.library.repository.RoleRepository;
import com.library.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private RoleRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDto save(UserDto userDto) {

        User user = new User();
        user.setName(userDto.getName());
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setRoles(Arrays.asList(repository.findByName("USER")));

        User userSave = userRepository.save(user);
        return mapperDTO(userSave);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User saveInfor(User user) {
        User user1 = userRepository.findByUsername(user.getUsername());
        user1.setAddress(user.getAddress());
        user1.setPhoneNumber(user.getPhoneNumber());
        return userRepository.save(user1);
    }

    private UserDto mapperDTO(User user){
        UserDto userDto = new UserDto();
        userDto.setName(user.getName());
        userDto.setPassword(user.getPassword());
        userDto.setUsername(user.getUsername());
        return userDto;
    }

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public User findById(Long id) {
		return userRepository.findById(id).get();
	}
    
	@Override
	public User update(User user) {
		// TODO Auto-generated method stub
		User userUpdate = null;
		try {
			userUpdate = userRepository.findById(user.getId()).get();
			userUpdate.setName(user.getName());
			userUpdate.setAddress(user.getAddress());
			userUpdate.setPhoneNumber(user.getPhoneNumber());
			userUpdate.setUsername(user.getUsername());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return userRepository.save(userUpdate);
	}
	
}