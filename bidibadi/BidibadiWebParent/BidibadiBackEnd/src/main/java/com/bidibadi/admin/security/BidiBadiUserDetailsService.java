package com.bidibadi.admin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.bidibadi.admin.user.UserRepository;
import com.bidibadi.common.entity.User;

public class BidiBadiUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		User user = userRepo.getUserByEmail(email);

		if (user != null) {

			return new BidiBadiUserDetails(user);
		}

		throw new UsernameNotFoundException("Böyle bir kullanıcı bulunamadi : " + email);
	}

}
