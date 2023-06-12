package com.library.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.library.model.User;
import com.library.model.Product;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    

}