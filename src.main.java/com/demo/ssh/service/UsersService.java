package com.demo.ssh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.ssh.dao.UsersRepository;
import com.demo.ssh.entity.Users;

@Service
public class UsersService {
	@Autowired
	private UsersRepository usersRepository;

	public List<Users> getAllUser() {
		return usersRepository.findAll();
	}

	public UsersRepository getUserResponse() {
		return usersRepository;
	}

	public void setUserDao(UsersRepository userDao) {
		this.usersRepository = userDao;
	}

}