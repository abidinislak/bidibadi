package com.bidibadi.admin.user;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bidibadi.common.entity.Role;
import com.bidibadi.common.entity.User;

@Service
@Transactional
public class UserSevice {

	public static final int USERS_PER_PAGE = 4;

	@Autowired
	private UserRepository repo;
	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private PasswordEncoder passwordsEncoder;

	public User getByEmail(String email) {

		return repo.getUserByEmail(email);

	}

	public List<User> listAll() {

		return (List<User>) repo.findAll();
	}

	public Page<User> listBYPage(int pageNumber, String sortField, String sortDir, String keyword) {

		Sort sort = Sort.by(sortField);

		sort = sort.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNumber - 1, USERS_PER_PAGE, sort);

		if (keyword != null) {

			return repo.findAll(keyword, pageable);
		}

		return repo.findAll(pageable);
	}

	public List<Role> listRepo() {

		return (List<Role>) roleRepo.findAll();
	}

	public User save(User user) {

		boolean isUpdating = (user.getId() != null);

		if (isUpdating) {

			User existinguser = repo.findById(user.getId()).get();

			if (user.getPassword().isEmpty()) {

				user.setPassword(existinguser.getPassword());

			}

			else {

				encodePassword(user);

			}

		} else {

			encodePassword(user);
		}

		return repo.save(user);

	}

	public User updateAccount(User userInform) {

		User userInDb = repo.findById(userInform.getId()).get();

		if (!userInform.getPassword().isEmpty()) {

			userInDb.setPassword(userInform.getPassword());
			encodePassword(userInDb);
		}

		if (userInform.getPhotos() != null) {
			userInDb.setPhotos(userInform.getPhotos());
		}
		userInDb.setFirstname(userInform.getFirstname());
		userInDb.setLastname(userInform.getLastname());

		return repo.save(userInDb);
	}

	private void encodePassword(User user) {
		String encodedPassword = passwordsEncoder.encode(user.getPassword());

		user.setPassword(encodedPassword);
	}

	public boolean isEmailUnique(Integer id, String email) {
		User userByEmailk = repo.getUserByEmail(email);

		if (userByEmailk == null)
			return true;

		boolean isCretaing = (id == null);

		if (isCretaing) {
			if (userByEmailk != null)
				return false;

		} else {
			if (userByEmailk.getId() != id)
				return false;

		}

		return true;

	}

	public User get(Integer id) throws UserNotFoundException {

		try {
			return repo.findById(id).get();

		} catch (NoSuchElementException e) {
			throw new UserNotFoundException("Couldnotfopıunuser");
		}

	}

	public void delete(Integer id) throws UserNotFoundException {

		Long countById = repo.countById(id);

		if (countById == null || countById == 0) {

			throw new UserNotFoundException("Boyle birk kullanıcıbulunamadi id si:" + id);

		}

		repo.deleteById(id);

	}

	public void updateUserEnabledStatus(Integer id, boolean enabled)

	{

		repo.updateEnabledStatus(id, enabled);
	}
}
