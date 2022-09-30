package com.bidibadi.admin.category.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bidibadi.admin.category.CategoryNotFoundException;
import com.bidibadi.admin.category.CategoryRepository;
import com.bidibadi.common.entity.Category;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repo;

	public List<Category> listAll() {

		List<Category> rootCategories = repo.findRootCategories();
		return listHierechicalCategories(rootCategories);
	}

	public List<Category> listCategoriesUsedInForm() {

		List<Category> CategroiesUsedInFrom = new ArrayList<>();

		Iterable<Category> categoriesIndb = repo.findAll();

		for (Category catgeory : categoriesIndb) {
			if (catgeory.getParent() == null) {
				CategroiesUsedInFrom.add(Category.copyIdAndName(catgeory));

				Set<Category> children = catgeory.getChildren();
				for (Category subcategory : children) {

					String name = "---" + subcategory.getName();

					CategroiesUsedInFrom.add(Category.copyIdAndName(subcategory.getId(), name));
					listChildren(CategroiesUsedInFrom, subcategory, 1);

				}
			}
		}

		return CategroiesUsedInFrom;
	}

	public Category get(Integer id) throws CategoryNotFoundException {

		try {

			return repo.findById(id).get();

		} catch (NoSuchElementException e) {

			throw new CategoryNotFoundException("This Catgeory can not found");
		}

	}

	private void listChildren(List<Category> categoriesUsedInForm, Category parent, int sublevel) {

		int newsublevel = sublevel + 1;
		Set<Category> children = parent.getChildren();

		for (Category subCategory : children) {

			String name = "";
			for (int i = 0; i < newsublevel; i++) {

				name += "---";

			}

			name += subCategory.getName();
			categoriesUsedInForm.add(new Category(name));

			listChildren(categoriesUsedInForm, subCategory, newsublevel);

		}

	}

	private List<Category> listHierechicalCategories(List<Category> rootCategories) {
		List<Category> herarhicalCategories = new ArrayList<>();

		for (Category rootCategory : rootCategories) {

			herarhicalCategories.add(Category.copyFull(rootCategory));
			Set<Category> children = rootCategory.getChildren();

			for (Category subCategory : children) {
				String name = "---" + subCategory.getName();
				herarhicalCategories.add(Category.copyFull(subCategory, name));

				listSubHierarchicalCategories(herarhicalCategories, subCategory, 1);

			}

		}

		return herarhicalCategories;
	}

	private void listSubHierarchicalCategories(List<Category> herarhicalCategories, Category parent, int sublevel) {
		Set<Category> children = parent.getChildren();
		int newSublevel = sublevel + 1;
		for (Category subCategory : children) {
			String name = "";
			for (int i = 0; i < newSublevel; i++) {

				name += "---";

			}

			name += subCategory.getName();
			herarhicalCategories.add(new Category(name));

			listSubHierarchicalCategories(herarhicalCategories, subCategory, newSublevel);

		}

	}

	public Category save(Category category) {
		return repo.save(category);
	}

	public String checkUnique(Integer id, String name, String alias) {
		boolean isCreatingNew = (id == null || id == 0);

		Category catgeoryByname = repo.findByName(name);

		if (isCreatingNew) {

			if (catgeoryByname != null) {

				return "DuplicatedName";

			}

			else {

				Category catgeroyByalias = repo.findByAlias(alias);

				if (catgeroyByalias != null) {
					return "DuplicateAlias";
				}

			}

		} else {

			if (catgeoryByname != null && catgeoryByname.getId() != id) {

				return "DuplicatedName";
			}

			Category categoryByAlias = repo.findByAlias(alias);
			if (categoryByAlias != null && categoryByAlias.getId() != id) {

				return "DuplicatedName";

			}

		}

		return "ok";

	}

}
