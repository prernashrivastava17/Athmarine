package com.athmarine.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinanceModel {

	private Integer id;

	private int status;

	private Integer userId;

	private String currency;

	private List<TurnoverModel> turnover;
	
	private int registrationstatus;

}
