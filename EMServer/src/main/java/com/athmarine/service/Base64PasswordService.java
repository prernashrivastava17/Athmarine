package com.athmarine.service;

import java.util.Base64;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.athmarine.resources.AppConstant;

@Service
public class Base64PasswordService {

	public String encode(String password) {
		StringBuilder pass = new StringBuilder(password);
		Date date = new Date();
		pass.append(AppConstant.SecretKey.SECRET_KEY);
		pass.append(date.toInstant().toString());
		String encodedUrl = Base64.getUrlEncoder().encodeToString(pass.toString().getBytes());

		return encodedUrl;
	}

	// ****to decode the UserId****

	public String decode(String encodedUrl) {
		byte[] decodedBytes = Base64.getUrlDecoder().decode(encodedUrl);
		String decodedUrl = new String(decodedBytes);
		String[] password = decodedUrl.split("`'+");
		return password[0];
	}

	public boolean matches(String password, String encodedPassword) {
		String originalPassword = decode(encodedPassword);

		if (originalPassword.equals(password)) {
			return true;
		} else {
			return false;
		}
	}
}
