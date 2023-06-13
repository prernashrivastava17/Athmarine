package com.athmarine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.athmarine.exception.EquipmentNotFoundException;
import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.EngineerCertificateService;

@RestController
@RequestMapping(path = RestMappingConstants.EngineerCertificateInterfaceUri.ENGINEER_CERTIFICATE_BASE_URI)
public class EngineerCertificatesController {

	@Autowired
	EngineerCertificateService engineerCertificateService;

	@GetMapping(RestMappingConstants.EngineerCertificateInterfaceUri.ENGINEER_CERTIFICATE_GET_URI + "/{id}")
	public ResponseEntity<?> getEngineerEquipments(@PathVariable("id") int id) throws EquipmentNotFoundException {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(engineerCertificateService.getEngineerCertificate(id));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

}
