package com.bidibadi.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.bidibadi.common.entity","com.bidibadi.admin.user"})
public class BidibadiBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(BidibadiBackEndApplication.class, args);
	}

}
