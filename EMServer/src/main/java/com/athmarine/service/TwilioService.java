package com.athmarine.service;

import java.util.Arrays;

import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.athmarine.resources.AppConstant;

@Service
public class TwilioService {

	@Autowired
	RestTemplate restTemplate;
	@Value("${ACCOUNT_SID}")
	private String ACCOUNT_SID;
	@Value("${AUTH_TOKEN}")
	private String AUTH_TOKEN;
	@Value("${TWILIO_MESSAGE_URL}")
	private String TWILIO_MESSAGE_URL;
	@Value("${ATHMARINE}")
	private String ATHMARINE;

	public Object sendMessage(String phoneNumber, String sendOTPPhone) {
		String message = AppConstant.TwilioSmsService.MESSAGE.replace("{var1}",sendOTPPhone);
		ResponseEntity<String> urlResponse = null;
		HttpHeaders httpHeaders = new HttpHeaders();
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("To", phoneNumber);
		map.add("From", ATHMARINE);
		map.add("Body", message);
		
		String authorizationHeader = "Basic "
				+ DatatypeConverter.printBase64Binary((ACCOUNT_SID + ":" + AUTH_TOKEN).getBytes());
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		httpHeaders.add("Authorization", authorizationHeader);
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map,
				httpHeaders);
		urlResponse = restTemplate.exchange(TWILIO_MESSAGE_URL, HttpMethod.POST, request, String.class);
		return urlResponse;
	}
}