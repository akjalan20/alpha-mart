package com.alphamart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alphamart.exception.NotFoundException;
import com.alphamart.model.User;
import com.alphamart.repository.UserRepository;

@Service
public class UserService {
	
	 @Autowired
	    UserRepository userRepository;
	
	public User addUser(User user) {
		return userRepository.save(user);
	}
	
	public User getUser(String userId)  {
        return userRepository.findByUserId(userId);
        		

    }

}
