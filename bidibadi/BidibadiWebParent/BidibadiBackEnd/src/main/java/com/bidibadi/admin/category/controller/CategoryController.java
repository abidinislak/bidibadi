package com.bidibadi.admin.category.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.bidibadi.admin.category.CategoryNotFoundException;
import com.bidibadi.common.entity.Category;

@Controller
public class CategoryController {

	@Autowired

	private CategoryService service;

	@GetMapping("/categories")

	public String listPage(@Param("sortDir") String sortDir, Model model) {
		if (sortDir == null || sortDir.isEmpty()) {

			sortDir = "asc";

		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("categories", service.listAll(sortDir));
		model.addAttribute("reverseSortDir", reverseSortDir);

		return "categories/categories";

	}

	@GetMapping("/category/new")
	public String newCategory(Model model) {

		List<Category> listCatgeories = service.listCategoriesUsedInForm();

		model.addAttribute("category", new Category());
		model.addAttribute("listCatgeories", listCatgeories);
		model.addAttribute("pageTitle", "Create new catgeroy");

		return "categories/category_form";
	}

	@PostMapping("/categories/save")
	public String saveCategory(Category category, @RequestParam("fileImage") MultipartFile multipartFile,
			RedirectAttributes redirectAttributes) throws IOException {

		if (!multipartFile.isEmpty()) {

			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			category.setImage(fileName);
			Category savedCategory = service.save(category);
			String uploadDir = "../category-images/" + savedCategory.getId();
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

		}

		else {

			service.save(category);

		}

		redirectAttributes.addFlashAttribute("message", "category has been set succesfuly..");

		return "redirect:/categories";
	}

	@GetMapping("/category/edit/{id}")

	public String updateCategory(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {

		Category updateCategory;
		try {
			updateCategory = service.get(id);
			model.addAttribute("category", updateCategory);
			model.addAttribute("listCatgeories", service.listCategoriesUsedInForm());
			model.addAttribute("pageTitle", "Update Category" + id);
			return "categories/category_form";
		} catch (CategoryNotFoundException e) {

			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "categories/category_form";
		}

	}

	@GetMapping("/categories/{id}/enabled/{status}")
	public String updateEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled,
			RedirectAttributes resirectedattributes) {
		service.updateEnabledStatus(id, enabled);

		String status = enabled ? "enabled" : "dsiabled";

		String message = "The Catgeroy ID " + id + "has benn " + status;
		resirectedattributes.addFlashAttribute("message", message);
		return "redirect:/categories";

	}

	@GetMapping("/categories/delete/{id}")
	public String deleteCategory(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {

		try {

			service.delete(id);
			String categoryDir = "../category-images/" + id;

			FileUploadUtil.removeDir(categoryDir);

			redirectAttributes.addFlashAttribute("message", "the category id " + id + " has been deleted succecfully");

		} catch (CategoryNotFoundException e) {

			redirectAttributes.addFlashAttribute("message", e.getMessage());

		}

		return "redirect:/categories";
	}

}
