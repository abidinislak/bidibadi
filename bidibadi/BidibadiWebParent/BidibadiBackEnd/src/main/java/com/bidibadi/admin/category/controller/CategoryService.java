package com.bidibadi.admin.category.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bidibadi.admin.category.CategoryRepository;
import com.bidibadi.common.entity.Category;

@Service
public class CategoryService
	{

		@Autowired
		private CategoryRepository repo;

		public List<Category> listAll() {

			return (List<Category>) repo.findAll();
		}

	}
