package com.athmarine.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.athmarine.request.DownloadFileResponse;
import com.athmarine.request.UploadFileResponse;
import com.athmarine.resources.AppConstant;
import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.FileServiceImpl;

@RestController
@RequestMapping(RestMappingConstants.FileInterfaceUri.FILE_BASE_URI)
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FileController {

	@Autowired
	FileServiceImpl fileService;

	/* Download File */
	/**************************************************************************************************/
	@PostMapping(path = RestMappingConstants.FileInterfaceUri.FILE_DOWNLOAD_URI)
	public ResponseEntity<BaseApiResponse> downloadFile(@RequestParam(required = true) String documentRequest,
			HttpServletRequest request)

	{
		DownloadFileResponse downloadFileResponse = fileService.downloadFile(documentRequest);
		BaseApiResponse baseApiResponse = ResponseBuilder.getSuccessResponse(downloadFileResponse);
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	/* Upload File */
	/**************************************************************************************************/
	/*
	 * @PostMapping(path = RestMappingConstants.FileInterfaceUri.FILE_UPLOAD_URI)
	 * public ResponseEntity<BaseApiResponse> uploadFile(@RequestParam(required =
	 * false) Integer customerCompanyId,
	 * 
	 * @RequestParam(required = false) Integer enginnerCertificateId,
	 * 
	 * @RequestParam(required = false) Integer turnOverId, @RequestParam(required =
	 * false) Integer vendorCompanyId,
	 * 
	 * @RequestPart(AppConstant.Commons.REQUEST_PART_FILE) @Valid @NotBlank(message
	 * = "Please attach file") @NotNull(message = "Please attach file")
	 * MultipartFile file,
	 * 
	 * HttpServletRequest request) throws IOException { DocumentRequest
	 * documentRequest = new DocumentRequest();
	 * 
	 * documentRequest.setCustomerCompanyId(customerCompanyId);
	 * documentRequest.setEnginnerCertificateId(enginnerCertificateId);
	 * documentRequest.setTurnOverId(turnOverId);
	 * documentRequest.setVendorCompanyId(vendorCompanyId);
	 * 
	 * BaseApiResponse baseApiResponse = null; UploadFileResponse uploadFileResponse
	 * = null; uploadFileResponse = fileService.uploadFile(documentRequest, file);
	 * baseApiResponse = ResponseBuilder.getSuccessResponse(uploadFileResponse);
	 * 
	 * return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK); }
	 */

	@PostMapping(path = RestMappingConstants.FileInterfaceUri.FILE_UPLOAD_URI)
	public ResponseEntity<BaseApiResponse> uploadFile(@RequestParam(required = false) String fileKey,
			@RequestParam(required = false) String token,
			@RequestPart(AppConstant.Commons.REQUEST_PART_FILE) @Valid @NotBlank(message = "Please attach file") @NotNull(message = "Please attach file") MultipartFile file,

			HttpServletRequest request) throws IOException {
		/*
		 * DocumentRequest documentRequest = new DocumentRequest();
		 * 
		 * documentRequest.setCustomerCompanyId(customerCompanyId);
		 * documentRequest.setEnginnerCertificateId(enginnerCertificateId);
		 * documentRequest.setTurnOverId(turnOverId);
		 * documentRequest.setVendorCompanyId(vendorCompanyId);
		 */

		BaseApiResponse baseApiResponse = null;
		UploadFileResponse uploadFileResponse = null;
		uploadFileResponse = fileService.uploadFile(fileKey, token, file);
		baseApiResponse = ResponseBuilder.getSuccessResponse(uploadFileResponse);

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}
}
