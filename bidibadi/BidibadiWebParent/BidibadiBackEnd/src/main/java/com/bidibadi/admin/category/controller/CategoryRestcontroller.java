package com.bidibadi.admin.category.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryRestcontroller {

	@Autowired

	private CategoryService service;

	@PostMapping("/categories/check_unique")

	private String checkUnique(@Param("id") Integer id, @Param("name") String name, @Param("alias") String alias) {

		return service.checkUnique(id, name, alias);

	}

}
