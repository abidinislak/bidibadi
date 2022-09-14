package com.bidibadi.admin.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.bidibadi.admin.security.BidiBadiUserDetails;
import com.bidibadi.common.entity.User;

@Controller
public class AccountController {

	@Autowired
	private UserSevice service;

	@GetMapping("/account")
	public String viewDetails(@AuthenticationPrincipal BidiBadiUserDetails loggedUSer, Model model) {

		String email = loggedUSer.getUsername();
		User user = service.getByEmail(email);

		model.addAttribute("user", user);

		return "account_form";
	}

}
