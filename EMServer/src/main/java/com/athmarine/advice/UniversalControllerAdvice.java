package com.athmarine.advice;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.athmarine.exception.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.athmarine.resources.AppConstant;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseStatus;

@ControllerAdvice
public class UniversalControllerAdvice extends ResponseEntityExceptionHandler {

	/**
	 * For Handling MethodArgumentNotValidException
	 **/
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		List<FieldError> allBindingErrorsList = ex.getBindingResult().getFieldErrors();
		List<String> allBindingErrorsMessageList = new ArrayList<String>();

		for (FieldError error : allBindingErrorsList) {
			allBindingErrorsMessageList.add(error.getField() + " - " + error.getDefaultMessage());
		}

		BaseApiResponse baseApiResponse = new BaseApiResponse();
		baseApiResponse.setResponseStatus(new ResponseStatus(AppConstant.StatusCodes.FAILURE));

		InvalidInputException invalidInputException = new InvalidInputException(
				AppConstant.ErrorTypes.INVALID_INPUT_ERROR, AppConstant.ErrorCodes.INVALID_INPUT_ERROR_CODE,
				allBindingErrorsMessageList.toString());

		baseApiResponse.setResponseData(invalidInputException);

		return new ResponseEntity<Object>(baseApiResponse, HttpStatus.OK);
	}

	/**
	 * For Handling UserAlreadyExistException
	 **/

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<BaseApiResponse> userNotFoundException(UserNotFoundException userNotFoundException,
			HttpServletRequest request) {
		BaseApiResponse baseApiResponse = new BaseApiResponse();
		baseApiResponse.setResponseStatus(new ResponseStatus(AppConstant.StatusCodes.FAILURE));
		baseApiResponse.setResponseData(userNotFoundException);
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@ExceptionHandler(InvalidInputException.class)
	public ResponseEntity<BaseApiResponse> invalidInputException(InvalidInputException invalidInputException,
			HttpServletRequest request) {
		BaseApiResponse baseApiResponse = new BaseApiResponse();
		baseApiResponse.setResponseStatus(new ResponseStatus(AppConstant.StatusCodes.FAILURE));
		baseApiResponse.setResponseData(invalidInputException);
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@ExceptionHandler(InvalidOtpException.class)
	public ResponseEntity<BaseApiResponse> invalidOtpException(InvalidOtpException invalidOtpException,
			HttpServletRequest request) {
		BaseApiResponse baseApiResponse = new BaseApiResponse();
		baseApiResponse.setResponseStatus(new ResponseStatus(AppConstant.StatusCodes.FAILURE));
		baseApiResponse.setResponseData(invalidOtpException);
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<BaseApiResponse> userNotFoundException(ResourceNotFoundException resourceNotFoundException,
			HttpServletRequest request) {
		BaseApiResponse baseApiResponse = new BaseApiResponse();
		baseApiResponse.setResponseStatus(new ResponseStatus(AppConstant.StatusCodes.FAILURE));
		baseApiResponse.setResponseData(resourceNotFoundException);
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@ExceptionHandler(PasswordMismatchException.class)
	public ResponseEntity<BaseApiResponse> passwordMismatchException(
			PasswordMismatchException passwordMismatchException, HttpServletRequest request) {
		BaseApiResponse baseApiResponse = new BaseApiResponse();
		baseApiResponse.setResponseStatus(new ResponseStatus(AppConstant.StatusCodes.FAILURE));
		baseApiResponse.setResponseData(passwordMismatchException);
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@ExceptionHandler(EmailExistException.class)
	public ResponseEntity<BaseApiResponse> emailExistException(EmailExistException emailExistException,
			HttpServletRequest request) {
		BaseApiResponse baseApiResponse = new BaseApiResponse();
		baseApiResponse.setResponseStatus(new ResponseStatus(AppConstant.StatusCodes.FAILURE));
		baseApiResponse.setResponseData(emailExistException);
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@ExceptionHandler(ModuleNotFoundException.class)
	public ResponseEntity<BaseApiResponse> moduleNotFoundException(ModuleNotFoundException moduleNotFoundException,
			HttpServletRequest request) {
		BaseApiResponse baseApiResponse = new BaseApiResponse();
		baseApiResponse.setResponseStatus(new ResponseStatus(AppConstant.StatusCodes.FAILURE));
		baseApiResponse.setResponseData(moduleNotFoundException);
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@ExceptionHandler(VendorCompanyNotFoundException.class)
	public ResponseEntity<BaseApiResponse> vendorCompanyNotFoundException(
			VendorCompanyNotFoundException vendorCompanyNotFoundException, HttpServletRequest request) {
		BaseApiResponse baseApiResponse = new BaseApiResponse();
		baseApiResponse.setResponseStatus(new ResponseStatus(AppConstant.StatusCodes.FAILURE));
		baseApiResponse.setResponseData(vendorCompanyNotFoundException);
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@ExceptionHandler(TokenExpiredException.class)
	public ResponseEntity<BaseApiResponse> tokenExpiredException(TokenExpiredException tokenExpiredException,
			HttpServletRequest request) {
		BaseApiResponse baseApiResponse = new BaseApiResponse();
		baseApiResponse.setResponseStatus(new ResponseStatus(AppConstant.StatusCodes.FAILURE));
		baseApiResponse.setResponseData(tokenExpiredException);
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@ExceptionHandler(RoleMismatchException.class)
	public ResponseEntity<BaseApiResponse> roleMismatchException(RoleMismatchException roleMismatchException,
			HttpServletRequest request) {
		BaseApiResponse baseApiResponse = new BaseApiResponse();
		baseApiResponse.setResponseStatus(new ResponseStatus(AppConstant.StatusCodes.FAILURE));
		baseApiResponse.setResponseData(roleMismatchException);
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@ExceptionHandler(ResourceAlreadyExistsException.class)
	public ResponseEntity<BaseApiResponse> roleMismatchException(
			ResourceAlreadyExistsException resourceAlreadyExistsException, HttpServletRequest request) {
		BaseApiResponse baseApiResponse = new BaseApiResponse();
		baseApiResponse.setResponseStatus(new ResponseStatus(AppConstant.StatusCodes.FAILURE));
		baseApiResponse.setResponseData(resourceAlreadyExistsException);
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@ExceptionHandler(CustomerCompanyNotFoundException.class)
	public ResponseEntity<BaseApiResponse> customerCompanyNotFoundException(
			CustomerCompanyNotFoundException customerCompanyNotFoundException, HttpServletRequest request) {
		BaseApiResponse baseApiResponse = new BaseApiResponse();
		baseApiResponse.setResponseStatus(new ResponseStatus(AppConstant.StatusCodes.FAILURE));
		baseApiResponse.setResponseData(customerCompanyNotFoundException);
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@ExceptionHandler(CustomerApproverNotFoundException.class)
	public ResponseEntity<BaseApiResponse> customerApproverNotFoundException(
			CustomerApproverNotFoundException customerApproverNotFoundException, HttpServletRequest request) {
		BaseApiResponse baseApiResponse = new BaseApiResponse();
		baseApiResponse.setResponseStatus(new ResponseStatus(AppConstant.StatusCodes.FAILURE));
		baseApiResponse.setResponseData(customerApproverNotFoundException);
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@ExceptionHandler(BidderApproverNotFoundException.class)
	public ResponseEntity<BaseApiResponse> bidderApproverNotFoundException(
			BidderApproverNotFoundException bidderApproverNotFoundException, HttpServletRequest request) {
		BaseApiResponse baseApiResponse = new BaseApiResponse();
		baseApiResponse.setResponseStatus(new ResponseStatus(AppConstant.StatusCodes.FAILURE));
		baseApiResponse.setResponseData(bidderApproverNotFoundException);
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@ExceptionHandler(VendorApproversPriceRangeNotFoundException.class)
	public ResponseEntity<BaseApiResponse> vendorApproversPriceRangeNotFoundException(
			VendorApproversPriceRangeNotFoundException vendorApproversPriceRangeNotFoundException,
			HttpServletRequest request) {
		BaseApiResponse baseApiResponse = new BaseApiResponse();
		baseApiResponse.setResponseStatus(new ResponseStatus(AppConstant.StatusCodes.FAILURE));
		baseApiResponse.setResponseData(vendorApproversPriceRangeNotFoundException);
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@ExceptionHandler(MasterEquipmentCategoryNotFoundException.class)
	public ResponseEntity<BaseApiResponse> masterEquipmentCategoryNotFoundException(
			MasterEquipmentCategoryNotFoundException masterEquipmentCategoryNotFoundException,
			HttpServletRequest request) {
		BaseApiResponse baseApiResponse = new BaseApiResponse();
		baseApiResponse.setResponseStatus(new ResponseStatus(AppConstant.StatusCodes.FAILURE));
		baseApiResponse.setResponseData(masterEquipmentCategoryNotFoundException);
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@ExceptionHandler(MasterCountryNotFoundException.class)
	public ResponseEntity<BaseApiResponse> masterCountryNotFoundException(
			MasterCountryNotFoundException masterCountryNotFoundException, HttpServletRequest request) {
		BaseApiResponse baseApiResponse = new BaseApiResponse();
		baseApiResponse.setResponseStatus(new ResponseStatus(AppConstant.StatusCodes.FAILURE));
		baseApiResponse.setResponseData(masterCountryNotFoundException);
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@ExceptionHandler(ServiceNotFoundException.class)
	public ResponseEntity<BaseApiResponse> serviceNotFoundException(ServiceNotFoundException serviceNotFoundException,
			HttpServletRequest request) {
		BaseApiResponse baseApiResponse = new BaseApiResponse();
		baseApiResponse.setResponseStatus(new ResponseStatus(AppConstant.StatusCodes.FAILURE));
		baseApiResponse.setResponseData(serviceNotFoundException);
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@ExceptionHandler(FileNotFoundException.class)
	public ResponseEntity<BaseApiResponse> FileNotFoundException(FileNotFoundException fileNotFoundException,
			HttpServletRequest request) {
		BaseApiResponse baseApiResponse = new BaseApiResponse();
		baseApiResponse.setResponseStatus(new ResponseStatus(AppConstant.StatusCodes.FAILURE));
		baseApiResponse.setResponseData(fileNotFoundException);
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@ExceptionHandler(EquipmentNotFoundException.class)
	public ResponseEntity<BaseApiResponse> equipmentNotFoundException(
			EquipmentNotFoundException equipmentNotFoundException, HttpServletRequest request) {
		BaseApiResponse baseApiResponse = new BaseApiResponse();
		baseApiResponse.setResponseStatus(new ResponseStatus(AppConstant.StatusCodes.FAILURE));
		baseApiResponse.setResponseData(equipmentNotFoundException);
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@ExceptionHandler(ManufacturerNotFoundException.class)
	public ResponseEntity<BaseApiResponse> manufacturerNotFoundException(
			ManufacturerNotFoundException manufacturerNotFoundException, HttpServletRequest request) {
		BaseApiResponse baseApiResponse = new BaseApiResponse();
		baseApiResponse.setResponseStatus(new ResponseStatus(AppConstant.StatusCodes.FAILURE));
		baseApiResponse.setResponseData(manufacturerNotFoundException);
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@ExceptionHandler(ServiceRequestNotFoundException.class)
	public ResponseEntity<BaseApiResponse> serviceRequestNotFoundException(
			ServiceRequestNotFoundException serviceRequestNotFoundException, HttpServletRequest request) {
		BaseApiResponse baseApiResponse = new BaseApiResponse();
		baseApiResponse.setResponseStatus(new ResponseStatus(AppConstant.StatusCodes.FAILURE));
		baseApiResponse.setResponseData(serviceRequestNotFoundException);
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@ExceptionHandler(ServiceRequestStatusException.class)
	public ResponseEntity<BaseApiResponse> serviceRequestStatusException(
			ServiceRequestStatusException serviceRequestStatusException, HttpServletRequest request) {
		BaseApiResponse baseApiResponse = new BaseApiResponse();
		baseApiResponse.setResponseStatus(new ResponseStatus(AppConstant.StatusCodes.FAILURE));
		baseApiResponse.setResponseData(serviceRequestStatusException);
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@ExceptionHandler(BidsNotFoundException.class)
	public ResponseEntity<BaseApiResponse> bidsNotFoundException(BidsNotFoundException bidsNotFoundException,
			HttpServletRequest request) {
		BaseApiResponse baseApiResponse = new BaseApiResponse();
		baseApiResponse.setResponseStatus(new ResponseStatus(AppConstant.StatusCodes.FAILURE));
		baseApiResponse.setResponseData(bidsNotFoundException);
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@ExceptionHandler(VendorBidderNotFoundException.class)
	public ResponseEntity<BaseApiResponse> vendorBidderFoundException(
			VendorBidderNotFoundException vendorBidderFoundException, HttpServletRequest request) {
		BaseApiResponse baseApiResponse = new BaseApiResponse();
		baseApiResponse.setResponseStatus(new ResponseStatus(AppConstant.StatusCodes.FAILURE));
		baseApiResponse.setResponseData(vendorBidderFoundException);
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@ExceptionHandler(VendorApproverNotFoundException.class)
	public ResponseEntity<BaseApiResponse> vendorApproverNotFoundException(
			VendorApproverNotFoundException vendorApproverNotFoundException, HttpServletRequest request) {
		BaseApiResponse baseApiResponse = new BaseApiResponse();
		baseApiResponse.setResponseStatus(new ResponseStatus(AppConstant.StatusCodes.FAILURE));
		baseApiResponse.setResponseData(vendorApproverNotFoundException);
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@ExceptionHandler(VendorEngineerNotFoundException.class)
	public ResponseEntity<BaseApiResponse> vendorEngineerNotFoundException(
			VendorEngineerNotFoundException vendorEngineerNotFoundException, HttpServletRequest request) {
		BaseApiResponse baseApiResponse = new BaseApiResponse();
		baseApiResponse.setResponseStatus(new ResponseStatus(AppConstant.StatusCodes.FAILURE));
		baseApiResponse.setResponseData(vendorEngineerNotFoundException);
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@ExceptionHandler(AdminNotFoundException.class)
	public ResponseEntity<BaseApiResponse> adminNotFoundException(AdminNotFoundException adminNotFoundException,
			HttpServletRequest request) {
		BaseApiResponse baseApiResponse = new BaseApiResponse();
		baseApiResponse.setResponseStatus(new ResponseStatus(AppConstant.StatusCodes.FAILURE));
		baseApiResponse.setResponseData(adminNotFoundException);
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@ExceptionHandler(MasterPortsServiceNotFoundException.class)
	public ResponseEntity<BaseApiResponse> masterPortsServiceNotFoundException(
			MasterPortsServiceNotFoundException masterPortsServiceNotFoundException, HttpServletRequest request) {
		BaseApiResponse baseApiResponse = new BaseApiResponse();
		baseApiResponse.setResponseStatus(new ResponseStatus(AppConstant.StatusCodes.FAILURE));
		baseApiResponse.setResponseData(masterPortsServiceNotFoundException);
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@ExceptionHandler(MasterCityNotFoundException.class)
	public ResponseEntity<BaseApiResponse> masterCityNotFoundException(
			MasterCityNotFoundException masterCityNotFoundException, HttpServletRequest request) {
		BaseApiResponse baseApiResponse = new BaseApiResponse();
		baseApiResponse.setResponseStatus(new ResponseStatus(AppConstant.StatusCodes.FAILURE));
		baseApiResponse.setResponseData(masterCityNotFoundException);
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@ExceptionHandler(EngineerEquimentsNotFoundException.class)
	public ResponseEntity<BaseApiResponse> engineerEquimentsNotFoundException(
			EngineerEquimentsNotFoundException engineerEquimentsNotFoundException, HttpServletRequest request) {
		BaseApiResponse baseApiResponse = new BaseApiResponse();
		baseApiResponse.setResponseStatus(new ResponseStatus(AppConstant.StatusCodes.FAILURE));
		baseApiResponse.setResponseData(engineerEquimentsNotFoundException);
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@ExceptionHandler(CustomerAdminNotFoundException.class)
	public ResponseEntity<BaseApiResponse> customerAdminNotFoundException(
			CustomerAdminNotFoundException customerAdminNotFoundException, HttpServletRequest request) {
		BaseApiResponse baseApiResponse = new BaseApiResponse();
		baseApiResponse.setResponseStatus(new ResponseStatus(AppConstant.StatusCodes.FAILURE));
		baseApiResponse.setResponseData(customerAdminNotFoundException);
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@ExceptionHandler(PhoneNumberExistException.class)
	public ResponseEntity<BaseApiResponse> phoneNumberExistException(
			PhoneNumberExistException phoneNumberExistException, HttpServletRequest request) {
		BaseApiResponse baseApiResponse = new BaseApiResponse();
		baseApiResponse.setResponseStatus(new ResponseStatus(AppConstant.StatusCodes.FAILURE));
		baseApiResponse.setResponseData(phoneNumberExistException);
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@ExceptionHandler(FailedToSendOTPException.class)
	public ResponseEntity<BaseApiResponse> failedToSendOTPException(FailedToSendOTPException failedToSendOTPException,
			HttpServletRequest request) {
		BaseApiResponse baseApiResponse = new BaseApiResponse();
		baseApiResponse.setResponseStatus(new ResponseStatus(AppConstant.StatusCodes.FAILURE));
		baseApiResponse.setResponseData(failedToSendOTPException);
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@ExceptionHandler(SparesVendorException.class)
	public ResponseEntity<BaseApiResponse> sparesVendorException(SparesVendorException sparesVendorException,
			HttpServletRequest request) {
		BaseApiResponse baseApiResponse = new BaseApiResponse();
		baseApiResponse.setResponseStatus(new ResponseStatus(AppConstant.StatusCodes.FAILURE));
		baseApiResponse.setResponseData(sparesVendorException);
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@ExceptionHandler(TravelCostVendorException.class)
	public ResponseEntity<BaseApiResponse> travelCostVendorException(
			TravelCostVendorException travelCostVendorException, HttpServletRequest request) {
		BaseApiResponse baseApiResponse = new BaseApiResponse();
		baseApiResponse.setResponseStatus(new ResponseStatus(AppConstant.StatusCodes.FAILURE));
		baseApiResponse.setResponseData(travelCostVendorException);
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@ExceptionHandler(MiscellaneousVendorException.class)
	public ResponseEntity<BaseApiResponse> miscellaneousVendorException(
			MiscellaneousVendorException miscellaneousVendorException, HttpServletRequest request) {
		BaseApiResponse baseApiResponse = new BaseApiResponse();
		baseApiResponse.setResponseStatus(new ResponseStatus(AppConstant.StatusCodes.FAILURE));
		baseApiResponse.setResponseData(miscellaneousVendorException);
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@ExceptionHandler(WorkingVendorException.class)
	public ResponseEntity<BaseApiResponse> workingVendorException(WorkingVendorException workingVendorException,
			HttpServletRequest request) {
		BaseApiResponse baseApiResponse = new BaseApiResponse();
		baseApiResponse.setResponseStatus(new ResponseStatus(AppConstant.StatusCodes.FAILURE));
		baseApiResponse.setResponseData(workingVendorException);
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}
	
	@ExceptionHandler(PhoneNumberNotFoundException.class)
	public ResponseEntity<BaseApiResponse> phoneNumberNotFoundException(PhoneNumberNotFoundException phoneNumberNotFoundException,
			HttpServletRequest request) {
		BaseApiResponse baseApiResponse = new BaseApiResponse();
		baseApiResponse.setResponseStatus(new ResponseStatus(AppConstant.StatusCodes.FAILURE));
		baseApiResponse.setResponseData(phoneNumberNotFoundException);
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@ExceptionHandler(InvoiceException.class)
	public ResponseEntity<BaseApiResponse> invoiceException(InvoiceException invoiceException,
																		HttpServletRequest request) {
		BaseApiResponse baseApiResponse = new BaseApiResponse();
		baseApiResponse.setResponseStatus(new ResponseStatus(AppConstant.StatusCodes.FAILURE));
		baseApiResponse.setResponseData(invoiceException);
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@ExceptionHandler(PurchaseOrderException.class)
	public ResponseEntity<BaseApiResponse> purchaseOrderException(PurchaseOrderException purchaseOrderException,
															HttpServletRequest request) {
		BaseApiResponse baseApiResponse = new BaseApiResponse();
		baseApiResponse.setResponseStatus(new ResponseStatus(AppConstant.StatusCodes.FAILURE));
		baseApiResponse.setResponseData(purchaseOrderException);
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}
	@ExceptionHandler(VendorHeadNotFoundException.class)
	public ResponseEntity<BaseApiResponse> vendorHeadNotFoundException(VendorHeadNotFoundException vendorHeadNotFoundException,
																  HttpServletRequest request) {
		BaseApiResponse baseApiResponse = new BaseApiResponse();
		baseApiResponse.setResponseStatus(new ResponseStatus(AppConstant.StatusCodes.FAILURE));
		baseApiResponse.setResponseData(vendorHeadNotFoundException);
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@ExceptionHandler(PaginationException.class)
	public ResponseEntity<BaseApiResponse> paginationException(PaginationException paginationException,
																  HttpServletRequest request) {
		BaseApiResponse baseApiResponse = new BaseApiResponse();
		baseApiResponse.setResponseStatus(new ResponseStatus(AppConstant.StatusCodes.FAILURE));
		baseApiResponse.setResponseData(paginationException);
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

}
