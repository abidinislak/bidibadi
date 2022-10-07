package com.bidibadi.admin.category.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bidibadi.admin.category.CategoryNotFoundException;
import com.bidibadi.admin.category.CategoryRepository;
import com.bidibadi.common.entity.Category;

@Service
@Transactional
public class CategoryService {

	@Autowired
	private CategoryRepository repo;

	public List<Category> listAll(String sortDir) {

		Sort sort = Sort.by("name");

		if (sortDir.equals("asc")) {

			sort = sort.ascending();

		} else if (sortDir.equals("desc")) {

			sort = sort.descending();

		}

		List<Category> rootCategories = repo.findRootCategories(sort);
		return listHierechicalCategories(rootCategories, sortDir);
	}

	public List<Category> listCategoriesUsedInForm() {

		List<Category> CategroiesUsedInFrom = new ArrayList<>();

		Iterable<Category> categoriesIndb = repo.findRootCategories(Sort.by("name").ascending());

		for (Category catgeory : categoriesIndb) {
			if (catgeory.getParent() == null) {
				CategroiesUsedInFrom.add(Category.copyIdAndName(catgeory));

				Set<Category> children = sortSubCategoeries(catgeory.getChildren());
				for (Category subcategory : children) {

					String name = "---" + subcategory.getName();

					CategroiesUsedInFrom.add(Category.copyIdAndName(subcategory.getId(), name));
					listSubCategoriesUsedinform(CategroiesUsedInFrom, subcategory, 1);

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

//	sortSubCategoeries
	private void listSubCategoriesUsedinform(List<Category> categoriesUsedInForm, Category parent, int sublevel) {

		int newsublevel = sublevel + 1;
		Set<Category> children = sortSubCategoeries(parent.getChildren());

		for (Category subCategory : children) {

			String name = "";
			for (int i = 0; i < newsublevel; i++) {

				name += "---";

			}

			name += subCategory.getName();
			categoriesUsedInForm.add(new Category(name));

			listSubCategoriesUsedinform(categoriesUsedInForm, subCategory, newsublevel);

		}

	}

	private List<Category> listHierechicalCategories(List<Category> rootCategories, String sorDir) {
		List<Category> herarhicalCategories = new ArrayList<>();

		for (Category rootCategory : rootCategories) {

			herarhicalCategories.add(Category.copyFull(rootCategory));
			Set<Category> children = sortSubCategoeries(rootCategory.getChildren(), sorDir);

			for (Category subCategory : children) {
				String name = "---" + subCategory.getName();
				herarhicalCategories.add(Category.copyFull(subCategory, name));

				listSubHierarchicalCategories(herarhicalCategories, subCategory, 1, sorDir);

			}

		}

		return herarhicalCategories;
	}

	private void listSubHierarchicalCategories(List<Category> herarhicalCategories, Category parent, int sublevel,
			String sorDir) {
		Set<Category> children = sortSubCategoeries(parent.getChildren(), sorDir);
		int newSublevel = sublevel + 1;
		for (Category subCategory : children) {
			String name = "";
			for (int i = 0; i < newSublevel; i++) {

				name += "---";

			}

			name += subCategory.getName();
			herarhicalCategories.add(new Category(name));

			listSubHierarchicalCategories(herarhicalCategories, subCategory, newSublevel, sorDir);

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

	private SortedSet<Category> sortSubCategoeries(Set<Category> children) {

		return sortSubCategoeries(children, "asc");
	}

	private SortedSet<Category> sortSubCategoeries(Set<Category> children, String sortDir) {

		SortedSet<Category> sortedChildren = new TreeSet<>(new Comparator<Category>() {

			@Override
			public int compare(Category cat1, Category cat2) {

				if (sortDir.equals("asc")) {

					return cat1.getName().compareTo(cat2.getName());
				} else {
					return cat2.getName().compareTo(cat1.getName());

				}
			}
		});
		sortedChildren.addAll(children);
		return sortedChildren;
	}

	public void updateEnabledStatus(Integer id, boolean enabled) {

		repo.updateEnabledStatus(id, enabled);

	}

	public void delete(Integer id) throws CategoryNotFoundException {

		Long countByid = repo.countById(id);
		if (countByid == null || countByid == 0) {
			throw new CategoryNotFoundException("Catgeroy with id cant not found : " + id);

		}

		repo.deleteById(id);
	}

}
