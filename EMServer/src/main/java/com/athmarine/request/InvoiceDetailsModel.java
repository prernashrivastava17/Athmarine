package com.athmarine.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDetailsModel {

	private Integer id;

	private Double amount;

	private String invoiceNumber;

	private Integer totalEngineer;

	private String currency;

	private String currencySymbol;

	private String email;

	private List<String> emailSendToFinance;

	private Integer user_id;
}
