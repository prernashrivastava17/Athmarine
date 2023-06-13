package com.athmarine.service;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.athmarine.entity.*;
import com.athmarine.exception.*;
import com.athmarine.repository.*;
import com.athmarine.request.*;
import com.athmarine.resources.AppConstant;
import com.athmarine.response.*;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.swing.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VendorCompanyHeadService {

	@Autowired
	private VendorCompanyRepository vendorCompanyRepository;

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	MasterStateService masterStateService;

	@Autowired
	public EmailService emailService;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	RoleDetailsServiceImpl roleDetailsServiceImpl;

	@Autowired
	public UserRepository userRepository;

	@Autowired
	public VendorCompanyService vendorCompanyServicel;

	@Autowired
	public OTPService otpService;

	@Autowired
	public MasterCityService masterCityService;

	@Autowired
	private HeadOfCompanyRepository hocRepository;

	@Autowired
	private BidsRepository bidsRepository;
	
	@Autowired
	ServiceRequestService requestService;
	
	@Autowired
	private PurchaseOrderRepository purchaseOrderRepository;
	
	@Autowired
	private BidsService bidsService;
	
	@Autowired
	private ServiceRequestInvoiceRepository invoiceRepository;

	@Autowired
	private ApproverBidRelationRepository approverBidRelationRepository;
	
	@Autowired
	private EngineerBidRelationRepository bidRelationRepository;
	@Value("${pagination.size}")
	private Integer size;

	public UserModel createVendorCompanyHead(UserModel headModel) throws MessagingException {

		if (!headModel.getSameASUser().equals("Yes")) {
			if (!userRepository.existsByEmail(headModel.getEmail())) {

				if (userRepository.existsByPrimaryPhone(headModel.getPrimaryPhone())) {

					throw new PhoneNumberExistException(AppConstant.ErrorTypes.PHONE_NUMBER_ALREADY_EXIST_ERROR,
							AppConstant.ErrorCodes.PHONE_NUMBER_ALREADY_EXIST_ERROR_CODE,
							AppConstant.ErrorMessages.PHONE_NUMBER_ALREADY_EXIST_ERROR);

				} else {

					new Thread(new Runnable() {

						@Override
						public void run() {
							otpService.createEmailVerificationLink(headModel.getEmail());
						}
					}).start();
				}

				VendorCompany vendorCompany = vendorCompanyServicel.findByIds(headModel.getCompanyId());
				vendorCompany.setRegistrationStatus(RegistrationStatus.Company_Head);
				vendorCompany.setRegisteredSuccessfully(false);
				vendorCompanyRepository.save(vendorCompany);
				Integer count = userDetailsService.getAllVendorHeadByCompanyId(headModel.getCompanyId());
				UserModel findId = userDetailsService.createUser(convertToModel(headModel));
				UserModel getUserData = userDetailsService.findById(findId.getId());

				getUserData.setUid(userDetailsService.generateUniqueIdVendor(getUserData.getId(), count + 1));
				return userDetailsService.updateUserData(getUserData);
			} else {

				throw new EmailExistException(AppConstant.ErrorTypes.EMAIL_EXIST_ERROR,
						AppConstant.ErrorCodes.EMAIL_EXIST_ERROR_CODE, AppConstant.ErrorMessages.EMAIL_EXIST_MESSAGE);
			}
		} else if (headModel.getSameASUser().equals("Yes")) {
			userDetailsService.updateSameUser(headModel);
		}
		return headModel;
	}

	public VendorCompanyModel addVendorCompanyHead(VendorCompanyModel vendorCompanyModel) {

		MSTState state = null;

		if (vendorCompanyModel.getMstateStateModel() != null) {

			state = masterStateService.findByIds(vendorCompanyModel.getMstateStateModel().getId());
		}
		if (!checkAlreadyExists(vendorCompanyModel.getUserId())) {
			return vendorCompanyServicel.convertToModel(
					vendorCompanyRepository.save(vendorCompanyServicel.convertToEntity(vendorCompanyModel, state)));
		} else
			throw new ResourceAlreadyExistsException(AppConstant.ErrorTypes.ADMIN_ALREADY_EXIST_ERROR,
					AppConstant.ErrorCodes.ADMIN_ALREADY_EXIST_ERROR_CODE,
					AppConstant.ErrorMessages.ADMIN_ALREADY_EXIST);

	}

	public UserModel findCompanyHead(@NotBlank Integer id) {

		UserModel user = userDetailsService.getUserHeadByCompanyId(id);

		return user;
	}

	public UserModel updateVendorComapnyHead(UserModel model) {

		UserModel user = userDetailsService.findById(model.getId());

		model.setId(user.getId());
		model.setUid(user.getUid());
		return userDetailsService.updateUserData(model);
	}

	public boolean checkAlreadyExists(int id) {
		return vendorCompanyRepository.existsById(id);

	}

	public UserModel convertToModel(UserModel headModel) {

		VendorCompanyModel vendorCompanyModel = vendorCompanyServicel.findById(headModel.getCompanyId());

		return UserModel.builder().id(headModel.getId()).name(headModel.getName()).email(headModel.getEmail())
				.type(headModel.getType()).primaryPhone(headModel.getPrimaryPhone()).dob(headModel.getDob())
				.secondaryPhone(headModel.getSecondaryPhone()).designation(headModel.getDesignation())
				.phoneVerified(headModel.isPhoneVerified()).emailVerified(headModel.isEmailVerified())
				.password(headModel.getPassword()).status(headModel.getStatus())
				.phoneVerified(headModel.isPhoneVerified()).emailVerified(headModel.isEmailVerified())
				.password(headModel.getPassword()).status(headModel.getStatus())
				.biddingLimit(headModel.getBiddingLimit()).uid(headModel.getUid()).currency(headModel.getCurrency())
				.roleModel(headModel.getRoleModel()).phoneCode(headModel.getPhoneCode())
				.onWhatsapp(headModel.isOnWhatsapp()).imageUrl(headModel.getImageUrl())
				.companyId(vendorCompanyModel.getId()).approverId(null).availableOn(headModel.getAvailableOn()).build();
	}

	// ------Get All Counts------
	public HocCountResponse getAllCounts(@NotNull Integer headId) {
		checkForHeadId(headId);
		User user = userDetailsService.findByIds(headId);
		if (user==null || user.getCompanyId().getId()== null)
			throw new VendorCompanyNotFoundException(AppConstant.ErrorMessages.VENDOR_COMPANY_NOT_EXIST_MESSAGE,
					AppConstant.ErrorCodes.VENDOR_COMPANY_EXIST_ERROR_CODE,
					AppConstant.ErrorTypes.VENDOR_COMPANY_NOT_EXIST_ERROR);

		List<User> userlist = userDetailsService.getAllUserByVendorCompanyId(user.getCompanyId().getId());
		return HocCountResponse.builder().headId(headId)
				.raisedBids(hocRepository.getCountByStatus(headId, "RAISED_BID"))
				.interestedRequest(hocRepository.getCountByStatus(headId, "INTERESTED_REQUEST"))
				.purchageOrder(hocRepository.getCountByStatus(headId, "PO_RECEIVED"))
				.invoicePaid(hocRepository.getCountByStatus(headId, "INVOICE_PAID"))
				.invoiceRaised(hocRepository.getCountByStatus(headId, "INVOICE_RAISED"))
				.completedJobs(hocRepository.getCountByStatus(headId, "COMPLETED_JOB")).activeUsers(userlist.size())
				.bidsApproval(bidsRepository.getHocApprovalCount(user.getCompanyId().getId())).build();
	}

	// --------API to get all PO's ----------------------
	public Object getAllPOBystatus(HocRequestModel model) {

		checkForHeadId(model.getHeadId());
		String result= checkForNull(model);
		if (result!="valid")
			return result;
		List<HocResponseModel> hocResponseModel = new ArrayList<HocResponseModel>();
		List<HeadOfCompany> Details = new ArrayList<HeadOfCompany>();
		if(model.getStartDate()==null && model.getEndDate()==null)
		{
			Details = hocRepository.getDetails(model.getHeadId(), model.getStatus(),
				PageRequest.of(model.getPage(), size));
		}
		else if(model.getFilterType().equalsIgnoreCase("PO_RECEIVED"))
		{
			 Details = hocRepository.getDetailsByFilter(model.getHeadId(), model.getStatus(),model.getStartDate(),
					model.getEndDate(),	PageRequest.of(model.getPage(), size));
		}
		else if(model.getFilterType().equalsIgnoreCase("SERVICE_DATE"))
		{
			 Details = hocRepository.getDetailsByServiceFilter(model.getHeadId(), model.getStatus(),model.getStartDate(),
						model.getEndDate(),	PageRequest.of(model.getPage(), size));
		}
		
		
		Details.stream().forEach(detail -> {

			
			PurchaseOrderResponse purchaseOrderResponse=bidsService.getPOById(detail.getStatusId());
			
			hocResponseModel.add(HocResponseModel.builder()
					.poReceivedDate(detail.getCreatedAt())
					.serviceDate(purchaseOrderResponse.getServiceRequestOn())
					.poId(purchaseOrderResponse.getId())
					.customerName(purchaseOrderResponse.getCustomer().getName())
					.vasselName(purchaseOrderResponse.getVesselName())
					.EquipmentCategory(purchaseOrderResponse.getEquipmentCategory())
					.serviceRequestId(purchaseOrderResponse.getRequestId())
					.poValue(purchaseOrderResponse.getBids().getTotalProposedAmount())
					.build());
		});
		if (hocResponseModel.size()==0)
			return "Related Data Not Found";
		return hocResponseModel;
	}

	private String checkForNull(HocRequestModel model) {
		if(model.getStartDate()==null && model.getEndDate()==null)
		{
			if (model.getStatus()==null)
				return "Status is Null";
		}
		else if (model.getStatus()==null) {
			return "Status is Null";
		}
		else if (model.getFilterType()==null)
		{
				return "Filter Type is Null";
		}
		else if (model.getPage()<0 || model.getPage()==null)
		{
			return "Page no is invalid";
		}

	return "valid";
	}

	private void checkForHeadId(@NotNull Integer headId) {
	
	List<HeadOfCompany> headOfCompanies=hocRepository.findByHeadId(headId);
	if(headOfCompanies.size()==0)
	{
		throw new VendorHeadNotFoundException(AppConstant.ErrorTypes.VENDOR_HEAD_NOT_FOUND,AppConstant.ErrorCodes.VENDOR_HEAD_NOT_FOUND, AppConstant.ErrorMessages.VENDOR_HEAD_NOT_FOUND
				);
	}	
	}

//---------------Get all Raised Bids
	public Object getAllRaisedBidsByStatus(HocRequestModel model) {
		
		checkForHeadId(model.getHeadId());
		String result= checkForNull(model);
		if (result!="valid")
			return result;
		List<HocResponseModel> hocResponseModel = new ArrayList<HocResponseModel>();
		List<HeadOfCompany> Details = new ArrayList<HeadOfCompany>();
		if(model.getStartDate()==null && model.getEndDate()==null)
		{
			Details = hocRepository.getDetails(model.getHeadId(), model.getStatus(),
				PageRequest.of(model.getPage(), size));
		}
		else if(model.getFilterType().equalsIgnoreCase("SERVICE_DATE"))
		{
			 Details = hocRepository.getDetailsByServiceFilter(model.getHeadId(), model.getStatus(),model.getStartDate(),
						model.getEndDate(),	PageRequest.of(model.getPage(), size));
		}
		else
			return "Please Enter Correct Data";

		Details.stream().forEach(detail->
		{
			BidsModel bidsModel=bidsService.getBidById(detail.getStatusId());
			ServiceRequestResponseModel serviceRequestResponseModel= requestService.getServiceRequestById(bidsModel.getServiceRequestId());
			hocResponseModel.add(HocResponseModel.builder()
					.serviceDate(serviceRequestResponseModel.getServiceRequestOn())
					.serviceRequestUid(serviceRequestResponseModel.getRequestUID())
					.bidUid(bidsModel.getBidUid())
					.vasselName(serviceRequestResponseModel.getVessel().getVesselName())
					.EquipmentCategory(serviceRequestResponseModel.getCategory().getName())
					.serviceRequestId(serviceRequestResponseModel.getId())
					.bidId(bidsModel.getId())
					.bidValue(bidsModel.getTotalAmountGet().toString())
					.build());
		});
		if (hocResponseModel.size()==0)
			return "Related Data Not Found";
		return hocResponseModel;
	}

	//------Get All Interested Request ----------------
	public Object getAllInterestedRequestByStatus(HocRequestModel model) {
		checkForHeadId(model.getHeadId());

		List<HocResponseModel> hocResponseModel = new ArrayList<HocResponseModel>();
		List<HeadOfCompany> Details = new ArrayList<HeadOfCompany>();
		if (model.getStatus()==null)
			return "Status is Null";
		if (model.getPage()<0 || model.getPage()==null)
		{
			return "Page no is invalid";
		}
		Details = hocRepository.getDetails(model.getHeadId(), model.getStatus(),
				PageRequest.of(model.getPage(), size));
		
		Details.stream().forEach(detail->
		{
			ServiceRequestResponseModel serviceRequestResponseModel= requestService.getServiceRequestById(detail.getStatusId());
			User user=userDetailsService.findByIds(detail.getBidderId());
			
			hocResponseModel.add(HocResponseModel.builder()
					.serviceDate(serviceRequestResponseModel.getServiceRequestOn())
					.serviceRequestUid(serviceRequestResponseModel.getRequestUID())
					.vasselName(serviceRequestResponseModel.getVessel().getVesselName())
					.portName(serviceRequestResponseModel.getPort().getName())
					.EquipmentCategory(serviceRequestResponseModel.getCategory().getName())
					.serviceRequestId(serviceRequestResponseModel.getId())
					.interestShownBy(user.getName())
					.build());
		});
		if (hocResponseModel.size()==0)
			return "Related Data Not Found";
		return hocResponseModel;
	}

	public Object getAllInvoicePaid(HocRequestModel model) {
		
		checkForHeadId(model.getHeadId());
		List<HocResponseModel> hocResponseModel = new ArrayList<HocResponseModel>();
		List<HeadOfCompany> Details = new ArrayList<HeadOfCompany>();
		String result= checkForNull(model);
		if (result!="valid")
			return result;
		if(model.getStartDate()==null && model.getEndDate()==null)
		{
			
			Details = hocRepository.getDetails(model.getHeadId(), model.getStatus(),
				PageRequest.of(model.getPage(), size));
			
		}
		else if(model.getFilterType().equalsIgnoreCase("SERVICE_DATE"))
		{
			
			 Details = hocRepository.getDetailsByServiceFilter(model.getHeadId(), model.getStatus(),model.getStartDate(),
					model.getEndDate(),	PageRequest.of(model.getPage(), size));
			 
		}
		else if(model.getFilterType().equalsIgnoreCase("PO_RECEIVED"))
		{
			
			 Details = hocRepository.getDetailsByPoDateFilter(model.getHeadId(), model.getStatus(),model.getStartDate(),
						model.getEndDate(),	PageRequest.of(model.getPage(), size));
			 
		}
		else if(model.getFilterType().equalsIgnoreCase("INVOICE_PAID"))
		{
			
			 Details = hocRepository.getDetailsByFilter(model.getHeadId(), model.getStatus(),model.getStartDate(),
						model.getEndDate(),	PageRequest.of(model.getPage(), size));
			 
		}
		
		Details.stream().forEach(detail->
		{
			InvoiceResponseModel invoice= bidsService.getInvoiceDetails(detail.getStatusId());
			
			ServiceRequestResponseModel serviceRequestResponseModel= requestService.getServiceRequestById(invoice.getServiceRequestId());			
			 
			
			hocResponseModel.add(HocResponseModel.builder()
					.serviceDate(serviceRequestResponseModel.getServiceRequestOn())
					.poReceivedDate(detail.getPoReceivedAt())
					.invoicePaidAt(detail.getCreatedAt())
					.serviceRequestUid(serviceRequestResponseModel.getRequestUID())
					.vasselName(serviceRequestResponseModel.getVessel().getVesselName())
					.EquipmentCategory(serviceRequestResponseModel.getCategory().getName())
					.poUid(invoice.getPoUid())
					.serviceReport(bidRelationRepository.findServiceReportByServiceRequest(invoice.getServiceRequestId()))
					.invoiceValue(invoice.getTotal().toString())
					.invoiceId(detail.getStatusId())
					.build());
		});
		if (hocResponseModel.size()==0)
			return "Related Data Not Found";
		return hocResponseModel;
	}

	//----------API to get all raised invoice -----------
	public Object getAllRaisedInvoice(HocRequestModel model) {
		checkForHeadId(model.getHeadId());
		List<HocResponseModel> hocResponseModel = new ArrayList<HocResponseModel>();
		List<HeadOfCompany> Details = new ArrayList<HeadOfCompany>();
		String result= checkForNull(model);
		if (result!="valid")
			return result;
		if(model.getStartDate()==null && model.getEndDate()==null)
		{
			
			Details = hocRepository.getDetails(model.getHeadId(), model.getStatus(),
				PageRequest.of(model.getPage(), size));
			
		}
		else if(model.getFilterType().equalsIgnoreCase("SERVICE_DATE"))
		{
			
			 Details = hocRepository.getDetailsByServiceFilter(model.getHeadId(), model.getStatus(),model.getStartDate(),
					model.getEndDate(),	PageRequest.of(model.getPage(), size));
			 
		}
		else if(model.getFilterType().equalsIgnoreCase("PO_RECEIVED"))
		{
			
			 Details = hocRepository.getDetailsByPoDateFilter(model.getHeadId(), model.getStatus(),model.getStartDate(),
						model.getEndDate(),	PageRequest.of(model.getPage(), size));
			 
		}
		else if(model.getFilterType().equalsIgnoreCase("INVOICE_RAISED"))
		{
			
			 Details = hocRepository.getDetailsByFilter(model.getHeadId(), model.getStatus(),model.getStartDate(),
						model.getEndDate(),	PageRequest.of(model.getPage(), size));
			 
		}
		
		Details.stream().forEach(detail->
		{
			InvoiceResponseModel invoice= bidsService.getInvoiceDetails(detail.getStatusId());
			
			ServiceRequestResponseModel serviceRequestResponseModel= requestService.getServiceRequestById(invoice.getServiceRequestId());			
			 
			
			hocResponseModel.add(HocResponseModel.builder()
					.serviceDate(serviceRequestResponseModel.getServiceRequestOn())
					.invoiceRaisedDate(detail.getCreatedAt())
					.poReceivedDate(detail.getPoReceivedAt())
					.serviceRequestUid(serviceRequestResponseModel.getRequestUID())
					.vasselName(serviceRequestResponseModel.getVessel().getVesselName())
					.EquipmentCategory(serviceRequestResponseModel.getCategory().getName())
					.poUid(invoice.getPoUid())
					.serviceReport(bidRelationRepository.findServiceReportByServiceRequest(invoice.getServiceRequestId()))
					.invoiceValue(invoice.getTotal().toString())
					.invoiceId(detail.getStatusId())
					.invoiceStatus(invoice.getInvoiceStatus())
					.build());
		});
		if (hocResponseModel.size()==0)
			return "Related Data Not Found";
		return hocResponseModel;
	}

	public Object getAllCompetedJobs(HocRequestModel model) {
		checkForHeadId(model.getHeadId());
		List<HocResponseModel> hocResponseModel = new ArrayList<HocResponseModel>();
		List<HeadOfCompany> Details = new ArrayList<HeadOfCompany>();
		String result= checkForNull(model);
		if (result!="valid")
			return result;
		if(model.getStartDate()==null && model.getEndDate()==null)
		{
			
			Details = hocRepository.getDetails(model.getHeadId(), model.getStatus(),
				PageRequest.of(model.getPage(), size));
			
		}
		else if(model.getFilterType().equalsIgnoreCase("SERVICE_DATE"))
		{
			
			 Details = hocRepository.getDetailsByServiceFilter(model.getHeadId(), model.getStatus(),model.getStartDate(),
					model.getEndDate(),	PageRequest.of(model.getPage(), size));
			 
		}
		else if(model.getFilterType().equalsIgnoreCase("COMPLETED_JOB"))
		{
			
			 Details = hocRepository.getDetailsByFilter(model.getHeadId(), model.getStatus(),model.getStartDate(),
						model.getEndDate(),	PageRequest.of(model.getPage(), size));
			 
		}
		
		Details.stream().forEach(detail->
		{

			PurchaseOrder order=purchaseOrderRepository.getPOById(invoiceRepository.findPoIdByServiceRequest(detail.getStatusId()));
			
			ServiceRequestResponseModel serviceRequestResponseModel= requestService.getServiceRequestById(detail.getStatusId());			
			 
			
			hocResponseModel.add(HocResponseModel.builder()
					.serviceDate(serviceRequestResponseModel.getServiceRequestOn())
					.completedJobDate(detail.getCreatedAt())
					.serviceRequestUid(serviceRequestResponseModel.getRequestUID())
					.poUid(order.getPoUID())
					.vasselName(serviceRequestResponseModel.getVessel().getVesselName())
					.EquipmentCategory(serviceRequestResponseModel.getCategory().getName())
					.portName(serviceRequestResponseModel.getPort().getName())
					.serviceReport(bidRelationRepository.findServiceReportByServiceRequest(serviceRequestResponseModel.getId()))
					.build());
		});
		if (hocResponseModel.size()==0)
			return "Related Data Not Found";
		return hocResponseModel;
	}
//----------------API to get all users in a company-----------
    public Object getAllUsers(Integer headId, Integer page) {
		checkForHeadId(headId);
		List<HocResponseModel> hocResponseModel = new ArrayList<HocResponseModel>();
		User user = userDetailsService.findByIds(headId);
		if (user==null || user.getCompanyId().getId()== null)
			throw new VendorCompanyNotFoundException(AppConstant.ErrorMessages.VENDOR_COMPANY_NOT_EXIST_MESSAGE,
					AppConstant.ErrorCodes.VENDOR_COMPANY_EXIST_ERROR_CODE,
					AppConstant.ErrorTypes.VENDOR_COMPANY_NOT_EXIST_ERROR);

		List<User> userlist = userDetailsService.getAllUserByVendorCompanyId(user.getCompanyId().getId());
		List<User> userSublist=new ArrayList<User>();

		if(((page*size)+size)<userlist.size()) {
			userSublist= userlist.subList(page * size, (page * size) + size);
		}
		else if((page*size)<userlist.size()){
			userSublist= userlist.subList(page * size, userlist.size());
		}
		else{
			userSublist= userlist;
		}
		userSublist.forEach(u->
		{
			UserModel userModel=userDetailsService.convertToUserModel(u);
			hocResponseModel.add(HocResponseModel.builder()
							.userId(userModel.getId())
							.userUID(userModel.getUid())
							.userName(userModel.getName())
							.userEmail(userModel.getEmail())
							.userPhone(userModel.getPrimaryPhone())
							.userRoleAssigned(userModel.getRoleModel())
							.userStatus(userModel.isUserAccess())
					.build());
		});
		if (hocResponseModel.size()==0)
			return "Related Data Not Found";
		return  hocResponseModel;
    }
//--------------API to get all bids whose status is approval pending------------
	public Object getAllBidsByHeadId(Integer headId,Integer page) {
		checkForHeadId(headId);
		List<HocResponseModel> hocResponseModel = new ArrayList<HocResponseModel>();
		User user = userDetailsService.findByIds(headId);
		if (user==null || user.getCompanyId()== null)
			throw new VendorCompanyNotFoundException(AppConstant.ErrorMessages.VENDOR_COMPANY_NOT_EXIST_MESSAGE,
					AppConstant.ErrorCodes.VENDOR_COMPANY_EXIST_ERROR_CODE,
					AppConstant.ErrorTypes.VENDOR_COMPANY_NOT_EXIST_ERROR);
		System.out.println(user.getCompanyId().getId());
		List<User> bidders=userDetailsService.userRepository.getAllBiddersByCompanyId(user.getCompanyId().getId());
		System.out.println(bidders.size());
		bidders.forEach(bidder->
		{
			System.out.println(bidder.getId());
				List<Bids> bids=bidsRepository.findByBidderId(bidder.getId(),"HOC_APPROVAL_PENDING",true, Pageable.unpaged());
			System.out.println(bids.size());
			bids.forEach(bid->{
				ServiceRequestResponseModel serviceRequest=requestService.getServiceRequestById(bid.getServiceRequest().getId());
				hocResponseModel.add(HocResponseModel.builder()
						.serviceDate(serviceRequest.getServiceRequestOn())
								.bidUid(bid.getBidUID())
								.interestShownBy(bidder.getName())
								.vasselName(serviceRequest.getVessel().getVesselName())
								.EquipmentCategory(serviceRequest.getCategory().getName())
								.serviceRequestUid(serviceRequest.getRequestUID())
								.serviceRequestId(serviceRequest.getId())
								.bidId(bid.getId())
								.bidValue(bid.getTotalAmountGet().toString())
						.build());

			});

		});
		if (hocResponseModel.size()==0)
			return  "Sorry NO Data Found";
		if(((page*size)+size)<hocResponseModel.size()) {
			return hocResponseModel.subList(page * size, (page * size) + size);
		}
		else if((page*size)<hocResponseModel.size()){
			return hocResponseModel.subList(page * size, hocResponseModel.size());
		}
		else{
			return hocResponseModel;
		}
	}

	public Object updateBidStatus(VendorApproverBidRequestModel requestModel) {

	checkForHeadId(requestModel.getHeadId());

	if(requestModel.getBidStatus().equalsIgnoreCase("Approved"))
	{
		bidsRepository.changeBidStatusById(requestModel.getBidId(),"BIDS_IN_PROGRESS");
		approverBidRelationRepository.changeBidStatusById(requestModel.getBidId(),"Approved",null);

	}
	else if (requestModel.getBidStatus().equalsIgnoreCase("Declined")) {
		bidsRepository.changeBidStatusById(requestModel.getBidId(),requestModel.getBidStatus());
		approverBidRelationRepository.changeBidStatusById(requestModel.getBidId(),requestModel.getBidStatus(),requestModel.getDeclineReason());
	}
		return "Bid Request "+requestModel.getBidStatus();
	}

	public Object updateBidByHoC(VendorApproverBidRequestModel model) throws Exception {
		if (model.getBidStatus().equalsIgnoreCase("Approved"))
		{
			model.getBidsModel().setBidStatus(model.getBidStatus());
			bidsService.updateSubmitedBid(model.getBidsModel());
		}
		else if (model.getBidStatus().equalsIgnoreCase("Declined")) {
			bidsRepository.changeBidStatusById(model.getBidId(),model.getBidStatus());
			approverBidRelationRepository.changeBidStatusById(model.getBidId(),model.getBidStatus(),model.getDeclineReason());
		}

		return "Bid Request "+model.getBidStatus();
	}
	//---------------------- Api to get all access request for user -----------------
	public Object getAllUsersforAccessRequestByHeadId(Integer headId,Integer page) {
		checkForHeadId(headId);
		List<HocResponseModel>  hocResponseModels=new ArrayList<HocResponseModel>();
		List<HeadOfCompany> users=hocRepository.getAllAccessRequest(headId,"USER_ACCESS_APPROVAL");

		users.forEach(user->
		{
			User u=userDetailsService.findByIds(user.getStatusId());
			UserModel userModel=userDetailsService.convertToUserModel(u);
			hocResponseModels.add(HocResponseModel.builder()
					.userId(userModel.getId())
					.userUID(userModel.getUid())
					.userName(userModel.getName())
					.userEmail(userModel.getEmail())
					.userPhone(userModel.getPrimaryPhone())
					.userRoleAssigned(userModel.getRoleModel())
					.userStatus(userModel.isUserAccess())
					.build());
		});

		if(((page*size)+size)<hocResponseModels.size()) {
			return hocResponseModels.subList(page * size, (page * size) + size);
		}
		else if((page*size)<hocResponseModels.size()){
			return hocResponseModels.subList(page * size, hocResponseModels.size());
		}
		else{
			return hocResponseModels;
		}
	}

	public Object ChangeStatusOfAccessibility(HocRequestModel model) {
		checkForHeadId(model.getHeadId());

		User user =userDetailsService.findByIds(model.getUserId());
		if (model.getStatus().equalsIgnoreCase("Approved"))
			user.setUserAccess(true);
		else if (model.getStatus().equalsIgnoreCase("Declined"))
			user.setUserAccess(false);
		else
			return "Status is Missing Or Wrong";

		userRepository.save(user);

		return "You Access Request is "+ model.getStatus();
	}


	//----------Method to Get All New Users For Approval----------//

	public List<HocResponseModel> getAllNewUsersForApproval(Integer headId, Integer page){
		
		userDetailsService.findByIds(headId);
		
		/*List<HeadOfCompany> headOfCompanyList = hocRepository.getDetails(headId,"NEW_USER_APPROVAL",
				PageRequest.of(page,size));*/

		List<HeadOfCompany> headOfCompanyList = hocRepository.findByHeadIdAndStatus(headId,
				Arrays.asList("NEW_USER_APPROVAL","UPDATE_USER_APPROVAL"), PageRequest.of(page,size));

		if(headOfCompanyList.size()<1){
			throw new UserNotFoundException(AppConstant.ErrorTypes.USER_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.USER_DOES_NOT_EXISTS_ERROR_CODE,
					AppConstant.ErrorMessages.NEW_USERS_NOT_FOUND);
		}

		return headOfCompanyList.stream().map(hoc -> {

			UserModel user = userDetailsService.findById(hoc.getStatusId());

			String type;

			if(hoc.getStatus().equalsIgnoreCase("NEW_USER_APPROVAL"))
				type = "New";
			else
				type = "Update";

			return HocResponseModel.builder().userId(user.getId()).userUID(user.getUid())
					.userName(user.getName()).userEmail(user.getEmail()).userPhone(user.getPrimaryPhone())
					.userRoleAssigned(user.getRoleModel()).type(type).build();
		}).collect(Collectors.toList());
	}

	//---------Method to Approve Or Decline The Status of the New User or Update User----------//

	public String changeStatusOfApproval(HocRequestModel hocRequestModel) throws InstantiationException, IllegalAccessException {

		userDetailsService.findByIds(hocRequestModel.getHeadId());
		User user = userDetailsService.findByIds(hocRequestModel.getUserId());

		HeadOfCompany headOfCompany = hocRepository.findByHeadAndUserIdAndStatus(hocRequestModel.getHeadId(),
				hocRequestModel.getUserId(),"NEW_USER_APPROVAL");

		if(headOfCompany==null){
			headOfCompany = hocRepository.findByHeadAndUserIdAndStatus(hocRequestModel.getHeadId(),
					hocRequestModel.getUserId(),"UPDATE_USER_APPROVAL");

			if(headOfCompany==null) {
				throw new UserNotFoundException(AppConstant.ErrorTypes.USER_NOT_EXIST_ERROR,
						AppConstant.ErrorCodes.USER_DOES_NOT_EXISTS_ERROR_CODE,
						AppConstant.ErrorMessages.NEW_USERS_NOT_FOUND);
			}
		}

		if(hocRequestModel.getStatus().equalsIgnoreCase("Approve")){

			if(headOfCompany.getStatus().equalsIgnoreCase("UPDATE_USER_APPROVAL"))
			{
				String updateJson = user.getUpdateData();
				UserModel userModel = new Gson().fromJson(updateJson,UserModel.class);
				UserModel updatedModel = userDetailsService.updateUserData(userModel);
				UserModel mergedModel = mergedObjects(userModel,updatedModel);
				headOfCompany.setStatus("UPDATE_USER_APPROVED");
				hocRepository.save(headOfCompany);
				return "User Update Approved Successfully";
			}
			else {
				user.setStatus(AppConstant.DeleteStatus.STATUS_ACTIVE);
				userRepository.save(user);
				headOfCompany.setStatus("NEW_USER_APPROVED");
				hocRepository.save(headOfCompany);
				return "New User Approved Successfully";
			}
		}
		else if(hocRequestModel.getStatus().equalsIgnoreCase("Decline")){

			if(headOfCompany.getStatus().equalsIgnoreCase("UPDATE_USER_APPROVAL"))
			{
				headOfCompany.setStatus("UPDATE_USER_DECLINED");
				headOfCompany.setDeclineReason(hocRequestModel.getDeclineReason());
				hocRepository.save(headOfCompany);

				return "User Update Declined Successfully";
			}
			else{
				user.setStatus(AppConstant.DeleteStatus.STATUS_DELETE);
				userRepository.save(user);
				headOfCompany.setStatus("NEW_USER_DECLINED");
				headOfCompany.setDeclineReason(headOfCompany.getDeclineReason());
				hocRepository.save(headOfCompany);

				return "New User Declined Successfully";
			}
		}
		else{
			throw new ServiceRequestStatusException(AppConstant.ErrorTypes.STATUS_NOT_FOUND,
					AppConstant.ErrorCodes.STATUS_NOT_FOUND,AppConstant.ErrorMessages.STATUS_NOT_FOUND);
		}
	}

	//-----------Method to update an user's access----------//

	public String updateUserAccess(Integer userId,String status){
		User user = userDetailsService.findByIds(userId);
		if (status.equalsIgnoreCase("Allow"))
			user.setUserAccess(user.isUserAccess());
		else if (status.equalsIgnoreCase("Deny"))
			user.setUserAccess(!user.isUserAccess());
		else
			return "Please Select Correct Update Status";
		userRepository.save(user);
		return "User's Access Status Updated Successfully";
	}

	//-----------Method to merge two UserModels---------//

	public UserModel mergedObjects(UserModel first, UserModel second) throws InstantiationException, IllegalAccessException {

		Class<?> clazz = first.getClass();
		Field[] fields = clazz.getDeclaredFields();
		Object returnValue = clazz.newInstance();
		for (Field field : fields) {
			field.setAccessible(true);
			Object value1 = field.get(first);
			Object value2 = field.get(second);
			Object value = (value2 != null) ? value2 : value1;
			field.set(returnValue, value);
		}
		return (UserModel) returnValue;
	}

}
