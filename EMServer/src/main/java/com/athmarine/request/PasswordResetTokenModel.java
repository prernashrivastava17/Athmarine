package com.athmarine.request;



import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PasswordResetTokenModel {
	
	Long id;

	private String token;

	private UserModel user;
	
	private Date expiryDate;

}
