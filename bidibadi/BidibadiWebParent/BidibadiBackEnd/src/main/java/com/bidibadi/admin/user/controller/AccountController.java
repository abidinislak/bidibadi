package com.bidibadi.admin.user.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bidibadi.admin.FileUploadUtil;
import com.bidibadi.admin.security.BidiBadiUserDetails;
import com.bidibadi.admin.user.UserSevice;
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

		return "users/account_form";
	}

	@PostMapping("/account/update")

	public String saveDetails(User user, RedirectAttributes rediredirecrAttributes,
			@AuthenticationPrincipal BidiBadiUserDetails loggedUSer, @RequestParam("image") MultipartFile multipartFile)
			throws IOException {

		if (!multipartFile.isEmpty()) {

			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

			user.setPhotos(fileName);

			User savedUser = service.updateAccount(user);
			String UploadDir = "user-photos/" + savedUser.getId();

			FileUploadUtil.cleanDir(UploadDir);

			FileUploadUtil.saveFile(UploadDir, fileName, multipartFile);

		}

		else {

			if (user.getPhotos().isEmpty())
				user.setPhotos(null);

			service.updateAccount(user);

		}

		loggedUSer.setFirstName(user.getFirstname());
		loggedUSer.setLastName(user.getLastname());
		rediredirecrAttributes.addFlashAttribute("message", "hesap başarılı birşekilde güncellendi");
		return "redirect:/account";

	}

}
