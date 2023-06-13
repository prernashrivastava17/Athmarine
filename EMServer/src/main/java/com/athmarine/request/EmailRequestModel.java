package com.athmarine.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequestModel {

	private String email;
	private boolean emailVerified;
	private String primaryPhone;
	private String phoneCode;

}
