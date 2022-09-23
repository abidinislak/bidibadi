package com.bidibadi.admin.category;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.bidibadi.common.entity.Category;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer>
	{

	}
