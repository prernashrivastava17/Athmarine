package com.athmarine.request;

import lombok.Data;

@Data
public class JwtRequest {

	private String username;
	private String password;
}
