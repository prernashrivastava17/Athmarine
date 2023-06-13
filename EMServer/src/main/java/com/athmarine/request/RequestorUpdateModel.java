package com.athmarine.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestorUpdateModel {

	private Integer id;

	private String name;

	private String email;

	private String dob;

	private String address;

	private String phoneCode;

	private String primaryPhone;

	private String password;

	private String imageUrl;

}
