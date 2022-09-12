package com.bidibadi.admin.user;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bidibadi.admin.FileUploadUtil;
import com.bidibadi.common.entity.Role;
import com.bidibadi.common.entity.User;

@Controller
public class UserController {

	@Autowired
	private UserSevice service;

	@GetMapping("/users")

	public String listFirstPage(Model model) {

		return listByPage(1, model, "firstname", "asc", null);

	}

	@GetMapping("/users/page/{pageNum}")

	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword) {

		Page<User> page = service.listBYPage(pageNum, sortField, sortDir, keyword);

		List<User> listUsers = page.getContent();

		long startcount = (pageNum - 1) * service.USERS_PER_PAGE + 1;
		long endCount = startcount + service.USERS_PER_PAGE - 1;

		if (endCount > page.getTotalElements()) {

			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startcount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("userlist", listUsers);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);

		return "users";
	}

	@GetMapping("/users/new")

	public String newUser(Model model) {

		User user = new User();
		user.setEnabled(true);

		List<Role> listRoles = service.listRepo();
		model.addAttribute("user", user);
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("pageTitle", "Create New User");

		return "user_form";

	}

	@PostMapping("/users/save")

	public String saveUser(User user, RedirectAttributes rediredirecrAttributes,
			@RequestParam("image") MultipartFile multipartFile) throws IOException {

		if (!multipartFile.isEmpty()) {

			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

			user.setPhotos(fileName);

			User savedUser = service.save(user);
			String UploadDir = "user-photos/" + savedUser.getId();

			FileUploadUtil.cleanDir(UploadDir);

			FileUploadUtil.saveFile(UploadDir, fileName, multipartFile);

		}

		else {

			if (user.getPhotos().isEmpty())
				user.setPhotos(null);

			service.save(user);

		}

		rediredirecrAttributes.addFlashAttribute("message", "the kullanıcı başarılı birşekilde kaydedildi");
		return getRedirectedToURLAffectedUser(user);

	}

	private String getRedirectedToURLAffectedUser(User user) {
		String firstPartOfEmail = user.getEmail().split("@")[0];
		return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" + firstPartOfEmail;
	}

	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes rediredirecrAttributes) {

		try {
			User user = service.get(id);
			List<Role> listRoles = service.listRepo();
			model.addAttribute("user", user);
			model.addAttribute("listRoles", listRoles);

			model.addAttribute("pageTitle", "Update User : " + id);

			return "user_form";

		} catch (UserNotFoundException e) {
			// TODO Auto-generated catch block
			rediredirecrAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/users";
		}

	}

	@GetMapping("/users/delete/{id}")

	public String deleteUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes)

	{
		try {
			service.delete(id);
			redirectAttributes.addFlashAttribute("message", "Kullanıcı id: " + id + " Silinmiştir");

		} catch (UserNotFoundException e) {
			// TODO Auto-generated catch block
			redirectAttributes.addFlashAttribute("message", e.getMessage());

		}
		return "redirect:/users";
	}

	@GetMapping("/users/{id}/enabled/{status}")
	public String updateUSerEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled,
			RedirectAttributes redirectAttributes)

	{
		service.updateUserEnabledStatus(id, enabled);

		String status = enabled ? "enabled" : "disabled";

		String message = "kullanıcı " + id + " " + status + "hale getirildi";

		redirectAttributes.addFlashAttribute("message", message);

		return "redirect:/users";

	}

	@GetMapping("/users/export/csv")

	public void exportToCsv(HttpServletResponse response) throws IOException {
		List<User> listall = service.listAll();

		UserCsvExporter exporter = new UserCsvExporter();

		exporter.export(listall, response);

	}

}
