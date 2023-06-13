package com.athmarine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.athmarine.resources.RestMappingConstants;
import com.athmarine.service.VendorEngineerService;

@RestController
@RequestMapping(RestMappingConstants.VendorCompanyInterfaceUri.VENDOR_COMPANY_BASE_URI)
public class VendorEngineerController {

	@Autowired
	VendorEngineerService vendorEngineerService;

	// *** API To Create Vendor Engineer

	/*@PostMapping(RestMappingConstants.VendorEngineerInterfaceUri.VENDOR_COMPANY_ENGINEER_CREATE_URI)
	public ResponseEntity<?> createVendorEngineer(@RequestBody VendorCompanyModel vendorCompanyModel) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorEngineerService.createVendorEngineer(vendorCompanyModel));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@PutMapping(RestMappingConstants.VendorEngineerInterfaceUri.VENDOR_COMPANY_ENGINEER_UPDATE_URI)
	public ResponseEntity<?> updateVendorEngineer(@RequestBody VendorCompanyModel vendorCompanyModel) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorEngineerService.updateVendorEngineer(vendorCompanyModel));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@GetMapping(RestMappingConstants.VendorEngineerInterfaceUri.VENDOR_COMPANY_ENGINEER_GET_URI + "/{id}")
	public ResponseEntity<?> getVendorEngineer(@PathVariable("id") int id) throws VendorEngineerNotFoundException {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorEngineerService.findVendorEngineerById(id));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@DeleteMapping(RestMappingConstants.VendorEngineerInterfaceUri.VENDOR_COMPANY_ENGINEER_DELETE_URI + "/{id}")
	public ResponseEntity<?> deleteVendorEngineer(@PathVariable("id") int id) throws VendorEngineerNotFoundException {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(vendorEngineerService.deleteVendorEngineer(id));

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}*/
}
