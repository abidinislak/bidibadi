package com.bidibadi.admin.category.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

	public String listPage(Model model) {

		model.addAttribute("categories", service.listAll());

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

	public String updateCategory(@PathVariable(name = "id") Integer id, Model model) {

		Category updateCategory;
		try {
			updateCategory = service.get(id);
			model.addAttribute("category", updateCategory);
			model.addAttribute("listCatgeories", service.listCategoriesUsedInForm());
			model.addAttribute("pageTitle", "Update Category" + id);
		} catch (CategoryNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "categories/category_form";

	}

}
