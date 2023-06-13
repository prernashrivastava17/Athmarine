
package com.athmarine.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.athmarine.exception.FailedToSendOTPException;
import com.athmarine.resources.AppConstant;

@Service
public class SMSService {

	private HttpHeaders headers;
	private RestTemplate rest;

	public void sendVerificationSMS(String mobileNumber, String otp) {
		String message = AppConstant.TwilioSmsService.MESSAGE.replace("{var1}", otp);
		this.headers = new HttpHeaders();
		this.rest = new RestTemplate();

		headers.add("Content-Type", "application/x-www-form-urlencoded");
		headers.add("Accept", "*/*");
		headers.add("Authorization", "Basic "
				+ "QUNiNzhlMDc0ZDM3MjU4OWRjZWRhNTNkZGFhMTRmYzY4ODo3M2I2YWNjOGRiMjA4ZTMwNmZiMjFkMjQyOWViZmFjNw==");

		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<String, String>();
		requestBody.add("Body", message);
		requestBody.add("From", "+13192507683");
		requestBody.add("To", mobileNumber);

		HttpEntity<Object> requestEntity = new HttpEntity<>(requestBody, headers);
		try {
			rest.exchange("https://api.twilio.com/2010-04-01/Accounts/ACb78e074d372589dceda53ddaa14fc688/Messages.json",
					HttpMethod.POST, requestEntity, String.class);

		} catch (Exception exception) {
			throw new FailedToSendOTPException(AppConstant.ErrorTypes.FAILED_TO_SEND_OTP_ON_PHONE_ERROR,
					AppConstant.ErrorCodes.FAILED_TO_SEND_OTP_ON_PHONE_ERROR_CODE,
					AppConstant.ErrorMessages.FAILED_TO_SEND_OTP_ON_PHONE_ERROR);

		}

	}

}
