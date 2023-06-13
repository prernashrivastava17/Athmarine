package com.athmarine.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.athmarine.request.PaymentDetailsModel;
import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.PaymentTransactionService;
import com.athmarine.service.UserDetailsServiceImpl;
import com.stripe.exception.StripeException;

@RestController
@RequestMapping(RestMappingConstants.PaymentInterfaceUri.PAYMENT_BASE_URI)
public class PaymentController {

	@Autowired
	PaymentTransactionService paymentTransactionService;

	@Autowired
	UserDetailsServiceImpl userDetailsServiceImpl;

	@PostMapping(RestMappingConstants.PaymentInterfaceUri.PAYMENT_CREATE_URI)
	public ResponseEntity<?> createPayment(@RequestBody PaymentDetailsModel model) throws StripeException {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(paymentTransactionService.saveInPaymentTrasactionEntity(model));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@GetMapping(RestMappingConstants.PaymentInterfaceUri.PAYMENT_GET_URI + "/{id}")
	public ResponseEntity<?> getTotalAmount(@PathVariable("id") Integer id) {

		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(paymentTransactionService.totalAmount(id));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@GetMapping(RestMappingConstants.PaymentInterfaceUri.DOWNLOAD_INVOICE_DETAILS + "/{id}")
	public ResponseEntity<?> downloadInvoiceDetail(@PathVariable("id") Integer id, HttpServletResponse response) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(paymentTransactionService.downloadInvoiceByCompanyID(id, response));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@GetMapping(RestMappingConstants.PaymentInterfaceUri.GET_PAYMENT_DETAIL_URI + "/{id}")
	public ResponseEntity<?> getPaymentDetail(@PathVariable("id") Integer userId) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(paymentTransactionService.getPaymentDetail(userId));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@GetMapping(RestMappingConstants.PaymentInterfaceUri.GET_PAYMENT_DETAILS_URI + "/{id}")
	public ResponseEntity<?> getAllEngineerByCompanyId(@PathVariable("id") Integer companyId) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(userDetailsServiceImpl.getAllEngineerByCompanyId(companyId));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@PostMapping(RestMappingConstants.PaymentInterfaceUri.STRIPE_STATUS_BY_WEBHOOK)
	public ResponseEntity<?> StripeStatusByWebhook(@RequestBody String Json, HttpServletRequest request) {
		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(paymentTransactionService.Webhook(Json, request));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@PostMapping(RestMappingConstants.PaymentInterfaceUri.GET_TXN_DETAILS_BY_USER_ID_URI + "/{userId}")
	public ResponseEntity<?> getTrxDetailsByUserId(@PathVariable("userId") Integer userId) {
		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(paymentTransactionService.getTxnDetailsByUser(userId));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

}
