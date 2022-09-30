package com.bidibadi.admin.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.bidibadi.common.entity.Category;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTest {

	@Autowired
	private CategoryRepository repo;

	private void printChildren(Category parent, int sublevel) {

		int newsublevel = sublevel + 1;
		Set<Category> children = parent.getChildren();

		for (Category subCategory : children) {
			for (int i = 0; i < newsublevel; i++) {

				System.err.print("---");

			}
			System.err.println(newsublevel + "nci Seviye : " + subCategory.getName());

			printChildren(subCategory, newsublevel);

		}

	}

	@Test
	public void testCreateRootCategory() {

		Category category = new Category("elektronics");

		Category saved = repo.save(category);

		assertThat(saved.getId()).isGreaterThan(0);

	}

	@Test

	public void testCreateSubCategory() {
		Category parent = new Category(8);

		Category iphone = new Category("iphone", parent);

		repo.saveAll(List.of(iphone));

	}

	@Test
	public void testGetCatgeory() {

		Category category = repo.findById(1).get();

		System.err.println(category.getName());

		Set<Category> children = category.getChildren();

		for (Category subCategory : children) {

			System.err.println(subCategory.getName());

		}
		assertThat(children.size()).isGreaterThan(0);

	}

	@Test
	public void testGetHiyerarchchalCategroies() {

		Iterable<Category> categories = repo.findAll();
		for (Category catgeory : categories) {
			if (catgeory.getParent() == null) {
				System.err.println(catgeory.getName());
				Set<Category> children = catgeory.getChildren();
				for (Category subcategory : children) {
					System.err.println("----1nci seviye : " + subcategory.getName());
					printChildren(subcategory, 1);

				}
			}
		}
	}

	@Test
	public void testListCategoryRoot() {

		List<Category> listCategoryRoot = repo.findRootCategories();
		listCategoryRoot.forEach(x -> System.out.println(x.getName()));

		assertThat(listCategoryRoot.size()).isGreaterThan(0);

	}

	@Test

	public void testFindByName() {

		Category testFindByName = repo.findByName("Computers");

		assertThat(testFindByName).isNotNull();

		assertThat(testFindByName.getId()).isGreaterThan(0);

	}

	@Test

	public void testFindByAlias() {

		Category testFindByName = repo.findByAlias("Computers");

		assertThat(testFindByName).isNotNull();
		System.err.println(testFindByName.getAlias());

		assertThat(testFindByName.getId()).isGreaterThan(0);

	}

}
