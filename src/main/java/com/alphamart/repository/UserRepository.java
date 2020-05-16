package com.alphamart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alphamart.model.User;

public interface UserRepository extends JpaRepository<User, String> {
   User findByUserId(String userName);
}