package com.bidibadi.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {

	@Test
	public void testEncoderPassword() {

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

		String rawpassword = "nam2020";
		String encodePassword = passwordEncoder.encode(rawpassword);
		System.err.println(encodePassword);

		boolean match = passwordEncoder.matches(rawpassword, encodePassword);
		System.err.println(match);

		assertThat(match).isTrue();
	}
}
