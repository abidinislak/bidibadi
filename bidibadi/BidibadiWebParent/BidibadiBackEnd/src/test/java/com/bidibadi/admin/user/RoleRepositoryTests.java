package com.bidibadi.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.bidibadi.common.entity.Role;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RoleRepositoryTests {

	@Autowired
	private RoleRepository repo;
	
	@Test
	public void testCreateFirstRole() {
	
		Role roleAdmin = new Role ("admin","manage everyhing");
		Role savedrole=repo.save(roleAdmin);
		
		
		assertThat(savedrole.getId()).isGreaterThan(0);
		
		
		
	}
	@Test
	public void testCreateRestRole() {
		
		Role roleSalesPerson = new Role ("SalesPerson","manage product prize,custoemrs,shipping,orders and sales repost");
		Role roleEditor = new Role ("Editor","manage categories ,bramds,porducts,articles  and menus");
		Role roleshipper = new Role ("shipper","vieww poruduvst, view orders and update order status");
		Role roleAssistant = new Role ("Assistant","manage questions and reviews");
		
		
		
		
		
		
		repo.saveAll( List.of(   roleSalesPerson,roleEditor,roleshipper,roleAssistant));
		
		
	//	assertThat(savedrole.getId()).isGreaterThan(0);
		
		
		
	}
	
	}
