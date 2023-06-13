package com.athmarine.request;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class MSTPromotionModel {

	private Integer id;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date startedDate;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date endDate;

	private String payMoney;

	private String voucher;
	
	private int status;

}
