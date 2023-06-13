package com.athmarine.request;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordRequest {

	@NotNull(message = "Email Cannot Be Blank/Null")
	private String email;

	@NotNull(message = "Password Cannot Be Blank/Null")
	private String newPassword;

}
