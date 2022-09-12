package com.bidibadi.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.bidibadi.common.entity.Role;
import com.bidibadi.common.entity.User;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositorytest {
	@Autowired
	private UserRepository repo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateUserWithOneRole() {

		Role roleAdmin = entityManager.find(Role.class, 1);

		User userName = new User("nam@codejava.net", "nam2020", "nam", "ha mingh");

		userName.addrole(roleAdmin);

		User userCaved = repo.save(userName);

		assertThat(userCaved.getId()).isGreaterThan(0);

	}

	@Test
	public void testCreateUserWithTwoRole() {
		User userName = new User("asdasda@codejava.net", "ravi2020", "ravi", "kumar");

		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(4);

		userName.addrole(roleEditor);
		userName.addrole(roleAssistant);

		User userRavi = repo.save(userName);

		assertThat(userRavi.getId()).isGreaterThan(0);

	}

	@Test
	public void testListAllUser() {

		Iterable<User> listAlluser = repo.findAll();

		listAlluser.forEach(user -> System.out.println(user));

	}

	@Test
	public void getuserById() {

		User user = repo.findById(1).get();
		System.out.println(user);
		assertThat(user).isNotNull();

	}

	@Test
	public void testUpdateUserDetails() {

		User user = repo.findById(1).get();

		user.setEnabled(true);
		user.setEmail("testupdate@gmail.com");

		repo.save(user);
		System.out.println(user);
		assertThat(user).isNotNull();

	}

	@Test
	public void testUpdateUserRoles() {

		User user = repo.findById(4).get();

		user.getRoles().remove(new Role(3));
		user.getRoles().remove(new Role(4));
		user.addrole(new Role(2));

		repo.save(user);
		System.out.println(user);
		assertThat(user).isNotNull();

	}

	@Test
	public void testDeleteUser() {

		repo.deleteById(1);

	}

	@Test
	public void testSearchUsers() {

		String keyword = "bruce";
		int pageNumber = 0;
		int pageSize = 3;

		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(keyword, pageable);

		List<User> ListUsers = page.getContent();
		ListUsers.forEach(user -> System.err.println(user));

		assertThat(ListUsers.size()).isGreaterThan(0);

	}

	@Test
	public void testGetUserByEmail() {

		String email = "aadawsd@codejava.net";
		User user = repo.getUserByEmail(email);

		assertThat(user).isNotNull();

	}

	@Test

	public void testCountById() {
		Integer id = 60;
		Long countById = repo.countById(id);

		assertThat(countById).isNotNull().isGreaterThan(0);

	}

	@Test
	public void testDisableUser() {

		Integer id = 60;
		repo.updateEnabledStatus(id, false);
	}

	@Test
	public void testEnableUser() {

		Integer id = 60;
		repo.updateEnabledStatus(id, true);
	}

	@Test
	public void testListFirstPage() {
		int pageNumber = 0;
		int pageSize = 3;

		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(pageable);

		List<User> ListUsers = page.getContent();
		ListUsers.forEach(user -> System.err.println(user));

		assertThat(ListUsers.size()).isEqualTo(pageSize);

	}

}
