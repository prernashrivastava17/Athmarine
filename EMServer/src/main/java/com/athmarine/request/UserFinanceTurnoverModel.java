package com.athmarine.request;

import java.util.List;

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
public class UserFinanceTurnoverModel {
	
	private Integer id;

	private UserModel userModel;

	private FinanceModel financeModel;

	private List<TurnoverModel> turnoverModels;

}