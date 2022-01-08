package com.loel.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.loel.domain.User;
import com.loel.exceptions.DuplicateUsernameException;
import com.loel.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public User saveUser(User newUser) {

		try {
			newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
			// Username has to be unique (exception)
			newUser.setUsername(newUser.getUsername());
			// Make sure that password and confirmPassword match
			// We don't persist or show the confirmPassword
			newUser.setConfirmPassword("");
			return userRepository.save(newUser);

		} catch (Exception e) {
			throw new DuplicateUsernameException(
					"Someone beat you to '" + newUser.getUsername() + "' or maybe you already registered");
		}

    }

    public Iterable<User> findAll() {
        return userRepository.findAll();
    }
}