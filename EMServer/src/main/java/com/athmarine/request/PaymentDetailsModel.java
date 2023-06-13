package com.athmarine.request;

import java.time.LocalDateTime;
import java.util.List;

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
public class PaymentDetailsModel {

	private Integer id;

	private String transactionId;

	private String system_transactionId;

	private String transactionCardDeatils;

	private Double amount;

	private String paymentMode;

	private String currency;

	private String currencySymbol;

	private String tokenStripe;

	private String email;

	private List<String> emailSendToFinance;

	private String transactionStatus;

	private Integer user_id;

	private LocalDateTime transactionLocalDate;

	private String receiptUrl;

	private String atmCardNumber;

	private String requestJson;

	private Integer masterPromotionStragyId;

}
