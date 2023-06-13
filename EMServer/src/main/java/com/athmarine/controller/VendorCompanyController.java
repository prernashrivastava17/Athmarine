package com.athmarine.controller;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.athmarine.exception.ResourceNotFoundException;
import com.athmarine.exception.ServiceRequestNotCompletedException;
import com.athmarine.exception.VendorCompanyNotFoundException;
import com.athmarine.repository.VendorCompanyRepository;
import com.athmarine.request.UserVendorModel;
import com.athmarine.request.VendorCompanyModel;
import com.athmarine.resources.AppConstant;
import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.VendorCompanyService;

@RestController
@RequestMapping(RestMappingConstants.VendorCompanyInterfaceUri.VENDOR_COMPANY_BASE_URI)
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class VendorCompanyController {

	@Autowired
	VendorCompanyService vendorCompanyService;

	@Autowired
	VendorCompanyRepository vendorCompanyRepository;

	// *** API To Create Vendor Company
	@PostMapping(RestMappingConstants.VendorCompanyInterfaceUri.VENDOR_COMPANY_ADD_URI)
	public ResponseEntity<?> createVendor(@RequestBody UserVendorModel userVendorModel) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorCompanyService.createVendorComapny(userVendorModel));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	// *** API To Update Vendor Company
	@PutMapping(RestMappingConstants.VendorCompanyInterfaceUri.VENDOR_COMPANY_UPDATE_BUISNESS_INFORMATION__URI)
	public ResponseEntity<?> updateVendorCompanyBuisnessInfo(@RequestBody VendorCompanyModel vendorCompanyModel) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorCompanyService.updateVendorCompany(vendorCompanyModel));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@PutMapping(RestMappingConstants.VendorCompanyInterfaceUri.VENDOR_COMPANY_UPDATE__URI)
	public ResponseEntity<?> updateVendorCompany(@RequestBody UserVendorModel userVendorModel) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorCompanyService.updateVendorCompany(userVendorModel));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	// *** Get Vendor Company
	@GetMapping(RestMappingConstants.VendorCompanyInterfaceUri.GET_VENDOR_COMPANY_URI + "/{id}")
	public ResponseEntity<?> getVendorCompanyById(@PathVariable("id") int id) throws VendorCompanyNotFoundException {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorCompanyService.getVendorCompanyResponse(id));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	// *** Get Vendor Company Buisness Information
	@GetMapping(RestMappingConstants.VendorCompanyInterfaceUri.GET_VENDOR_COMPANY_URI_COUNTRY + "/{id}")
	public ResponseEntity<?> getCompanyDataByCountryId(@PathVariable("id") int id)
			throws VendorCompanyNotFoundException {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorCompanyService.getCompanyDataByCountryId(id));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@GetMapping(RestMappingConstants.VendorCompanyInterfaceUri.GET_VENDOR_COMPANY_FINANCE + "/{id}")
	public ResponseEntity<?> getFinanceEmailAndPhoneNumber(@PathVariable("id") int id)
			throws VendorCompanyNotFoundException {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorCompanyService.getAllFinaneEmailAndPhoneNumberByCompanyId(id));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	
	@GetMapping(RestMappingConstants.VendorCompanyInterfaceUri.GET_VENDOR_COMPANY_BUISNESS_INFORMATION_URI + "/{id}")
	public ResponseEntity<?> getVendorCompanyBuisnessInfoById(@PathVariable("id") int id)
			throws VendorCompanyNotFoundException {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorCompanyService.getVendorCompanyData(id));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	// ***Delete API TO Delete VendorCompany

	@DeleteMapping(RestMappingConstants.VendorCompanyInterfaceUri.VENDOR_COMPANY_DELETE_URI + "/{id}")
	public ResponseEntity<?> deleteVendorCompanyById(@PathVariable @NotBlank int id) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorCompanyService.deleteVendorCompany(id));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@GetMapping(RestMappingConstants.VendorCompanyInterfaceUri.GENERATE_REFERRAL_CODE_URI + "/{id}")
	public ResponseEntity<?> generateReferralCode(@PathVariable("id") int id)
			throws ServiceRequestNotCompletedException {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorCompanyService.generateReferralCode(id));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@GetMapping(RestMappingConstants.VendorCompanyInterfaceUri.VALIDATE_REFERRAL_CODE_URI + "/{referralCode}")
	public ResponseEntity<?> validateReferralCode(@PathVariable("referralCode") String referralCode)
			throws ServiceRequestNotCompletedException {

		BaseApiResponse baseApiResponse = null;

		if (vendorCompanyRepository.existsByReferralCode(referralCode)) {
			baseApiResponse = ResponseBuilder.getSuccessResponse();
		} else {
			throw new ResourceNotFoundException(AppConstant.ErrorTypes.REFERRAL_CODE_INCORRECT_ERROR,
					AppConstant.ErrorCodes.WRONG_REFERRAL_CODE_ERROR_CODE,
					AppConstant.ErrorMessages.WRONG_REFERRAL_CODE_MESSAGE);
		}

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}


}
