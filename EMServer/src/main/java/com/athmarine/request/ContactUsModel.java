package com.athmarine.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContactUsModel {

	private Integer id;

	private String email;

	private String name;

	private String subject;

	private String message;

	private boolean privacyPolicy;
	
	private int status;

}