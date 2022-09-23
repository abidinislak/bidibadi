package com.bidibadi.admin.category.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.bidibadi.common.entity.Category;

@Controller
public class CategoryController
	{

		@Autowired

		private CategoryService service;

		@GetMapping("/categories")

		public String listPage(
		Model model) {

			model
			.addAttribute(
			"categories",
			service
			.listAll());

			return "categories/categories";

		}

		@GetMapping("/category/new")
		public String newCategory(
		Model model) {

			Category category = new Category();

			model
			.addAttribute(
			"category",
			category);

			return "categories/category_form.html";
		}

	}
