package com.athmarine.service;

import com.athmarine.request.CustomerCompanyModel;
import com.athmarine.request.UserModel;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCustomerModel {

	private UserModel userModel;

	private CustomerCompanyModel customerCompanyModel;

}
