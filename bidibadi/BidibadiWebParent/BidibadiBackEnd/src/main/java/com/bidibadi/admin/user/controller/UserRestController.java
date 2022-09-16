package com.bidibadi.admin.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bidibadi.admin.user.UserSevice;

@RestController
public class UserRestController {
	@Autowired

	private UserSevice service;

	@PostMapping("/users/check_email")
	public String checkDuplicateEmail(@Param("id") Integer id, @Param("email") String email) {

		return service.isEmailUnique(id, email) ? "OK" : "Duplicated";

	}

}
