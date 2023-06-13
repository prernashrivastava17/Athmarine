package com.athmarine.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.validation.constraints.NotBlank;

import com.athmarine.entity.*;
import com.athmarine.exception.*;
import com.athmarine.repository.*;
import com.athmarine.request.*;
import com.athmarine.response.GetServiceRequestCount;
import com.athmarine.response.RequestBidsResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.athmarine.resources.AppConstant;
import com.athmarine.template.EmailTemplate;

@Service
public class VendorApproverService {

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	public EmailService emailService;

	@Autowired
	public UserRepository userRepository;

	@Autowired
	public VendorCompanyService vendorCompanyService;

	@Autowired
	public OTPService otpService;

	@Autowired
	public BidderApproverRepository bidderApproverRepository;

	@Autowired
	VendorBidderService vendorBidderService;

	@Autowired
	VendorCompanyRepository companyRepository;

	@Autowired
	BidsRepository bidsRepository;

	@Autowired
	ApproverBidRelationRepository approverBidRelationRepository;

	@Autowired
	BidsService bidsService;

	@Autowired
	ServiceRequestService serviceRequestService;

	@Autowired
	HeadOfCompanyRepository headOfCompanyRepository;

	@Value("${pagination.size}")
	private Integer size;

	public List<UserListResponseModel> createVendorApprover(List<VendorApproverModel> vendorApproverModel)
			throws MessagingException {
		List<UserListResponseModel> response = new ArrayList<UserListResponseModel>();

		for (int i = 0; i < vendorApproverModel.size(); i++) {
			UserModel approverModel = vendorApproverModel.get(i).getUserModel();
			if (!approverModel.getSameASUser().equals("Yes")) {

				if (userRepository.existsByEmail(approverModel.getEmail())) {

					throw new EmailExistException(AppConstant.ErrorTypes.EMAIL_EXIST_ERROR, approverModel.getEmail(),
							" " + approverModel.getEmail() + " " + AppConstant.ErrorMessages.EMAIL_EXIST_MESSAGE);

				} else if (userRepository.existsByPrimaryPhone(approverModel.getPrimaryPhone())) {

					throw new PhoneNumberExistException(AppConstant.ErrorTypes.PHONE_NUMBER_ALREADY_EXIST_ERROR,
							approverModel.getPrimaryPhone(), " " + approverModel.getPrimaryPhone()
									+ AppConstant.ErrorMessages.PHONE_NUMBER_ALREADY_EXIST_ERROR);

				}
			}
		}

		for (int i = 0; i < vendorApproverModel.size(); i++) {
			UserModel approverModel = vendorApproverModel.get(i).getUserModel();
			if (approverModel.getSameASUser().equals("No")) {
				List<UserListResponseModel> bidderList = vendorApproverModel.get(i).getBidderModel();

				BidderApprover bidderApproverModel = vendorApproverModel.get(i).getBidderApproverModel();

				new Thread(new Runnable() {

					@Override
					public void run() {
						otpService.createEmailVerificationLink(approverModel.getEmail());
					}
				}).start();
				Integer count = userDetailsService
						.getAllVendorApproverByCompanyId(vendorApproverModel.get(i).getUserModel().getCompanyId());
				UserModel findIds = userDetailsService.convertToModel(userRepository
						.save(userDetailsService.convertToEntity(vendorApproverModel.get(i).getUserModel())));
				User findId = userDetailsService.findByIds(findIds.getId());

				List<User> biddersList = new ArrayList<User>();
				List<BidderApprover> bidderApproverList = new ArrayList<>();
				BidderApprover bidderApproverModel2 = new BidderApprover();
				bidderApproverModel2 = bidderApproverModel;
				if (!bidderList.isEmpty()) {
					for (int j = 0; j < bidderList.size(); j++) {

						User bidder = userDetailsService.findByIds(bidderList.get(j).getId());
						bidder.setApproverId(findId);
						biddersList.add(bidder);
						bidderApproverModel2.setUserVendorBidder(bidder);
						bidderApproverModel2.setApprovedBy(findId);
						bidderApproverList.add(bidderApproverModel2);
						bidderApproverModel2 = new BidderApprover();
						bidderApproverModel2.setIsApprovedStatus(VendorApproverApprovedStatus.PENDING);

					}

					userDetailsService.updateUserDataCustomer(biddersList);

					bidderApproverRepository.saveAll(bidderApproverList);

				}
				VendorCompany vendorCompany = vendorCompanyService
						.findByIds(vendorApproverModel.get(i).getUserModel().getCompanyId());
				vendorCompany.setRegistrationStatus(RegistrationStatus.Approver);
				vendorCompany.setRegisteredSuccessfully(false);
				companyRepository.save(vendorCompany);

				findIds.setUid(userDetailsService.generateUniqueIdVendor(findIds.getId(), count + i + 1));
				userDetailsService.updateUserData(findIds);
				response.add(convertToModel(userDetailsService.convertToModel(findId)));
			} else if (approverModel.getSameASUser().equals("Yes")) {
				UserModel bidderModel = approverModel;
				UserModel vendorRegistrationStatus = userDetailsService.updateSameUser(bidderModel);
				List<UserListResponseModel> bidderList = vendorApproverModel.get(i).getBidderModel();
				BidderApprover bidderApproverModel = vendorApproverModel.get(i).getBidderApproverModel();
				List<User> biddersList = new ArrayList<User>();
				List<BidderApprover> bidderApproverList = new ArrayList<>();
				BidderApprover bidderApproverModel2 = new BidderApprover();
				bidderApproverModel2 = bidderApproverModel;
				User findId = userDetailsService.findByIds(vendorRegistrationStatus.getId());
				if (!bidderList.isEmpty()) {
					for (int j = 0; j < bidderList.size(); j++) {

						User bidder = userDetailsService.findByIds(bidderList.get(j).getId());
						bidder.setApproverId(findId);
						biddersList.add(bidder);
						bidderApproverModel2.setUserVendorBidder(bidder);
						bidderApproverModel2.setApprovedBy(findId);
						bidderApproverList.add(bidderApproverModel2);
						bidderApproverModel2 = new BidderApprover();
						bidderApproverModel2.setIsApprovedStatus(VendorApproverApprovedStatus.PENDING);

					}

					userDetailsService.updateUserDataCustomer(biddersList);

					bidderApproverRepository.saveAll(bidderApproverList);

				}

				VendorCompany vendorCompany = vendorCompanyService.findByIds(vendorRegistrationStatus.getCompanyId());
				vendorCompany.setRegistrationStatus(RegistrationStatus.Approver);
				vendorCompany.setRegisteredSuccessfully(false);
				companyRepository.save(vendorCompany);

			}
		}

		return response;
	}

	public List<UserListResponseModel> updateAllVendorApprover(List<UserModel> userModel) {
		List<UserListResponseModel> responseModel = new ArrayList<UserListResponseModel>();
		for (int i = 0; i < userModel.size(); i++) {
			UserModel user = userDetailsService.findById(userModel.get(i).getId());

			// if
			// (user.getRoleModel().getName().equals(AppConstant.RoleValues.ROLE_APPROVER))
			// {
			if (!userRepository.existsByEmail(userModel.get(i).getEmail())) {

				String token = UUID.randomUUID().toString();

				String otp = String.valueOf(otpService.generateOTP(userModel.get(i).getEmail()));

				EmailTemplate template = new EmailTemplate("templates/ResetPasswordEmail.html");
				String url = AppConstant.VerifyApprover.VERIFY_APPROVER_LINK + "?token= " + token;

				Map<String, String> replacementValues = new HashMap<>();
				replacementValues.put("link", url);
				replacementValues.put("otp", otp);

				String message = template.getTemplate(replacementValues);

				try {
					emailService.sendEmail(userModel.get(i).getEmail(),
							AppConstant.VerifyApprover.VERIFY_APPROVER_SUBJECT_LINE, message);
				} catch (MessagingException e) {
					e.printStackTrace();
				}

				userModel.get(i).setPassword(token);
				userModel.get(i).setId(user.getId());
				userModel.get(i).setUid(user.getUid());
				responseModel.add(convertToModel(userDetailsService.updateUserData(userModel.get(i))));
			} else {
				throw new ResourceAlreadyExistsException(AppConstant.ErrorTypes.USER_EXIST_ERROR,
						AppConstant.ErrorCodes.USER_ALREADY_EXIST_ERROR_CODE,
						AppConstant.ErrorMessages.USER_ALREADY_EXIST);
			}
		}
		return responseModel;

	}

	public List<UserListResponseModel> updateVendorApprover(List<VendorApproverModel> vendorApproverModel)
			throws MessagingException {
		List<UserListResponseModel> response = new ArrayList<UserListResponseModel>();

		for (int i = 0; i < vendorApproverModel.size(); i++) {
			UserModel approverModel = vendorApproverModel.get(i).getUserModel();

			List<UserListResponseModel> bidderList = vendorApproverModel.get(i).getBidderModel();

			BidderApprover bidderApproverModel = vendorApproverModel.get(i).getBidderApproverModel();
			UserModel approver = userDetailsService.findById(approverModel.getId());
			if (approver != null) {

				String token = UUID.randomUUID().toString();

				String otp = String.valueOf(otpService.generateOTP(approverModel.getEmail()));

				EmailTemplate template = new EmailTemplate("templates/ResetPasswordEmail.html");
				String url = AppConstant.VerifyApprover.VERIFY_APPROVER_LINK + "?token= " + token;

				Map<String, String> replacementValues = new HashMap<>();
				replacementValues.put("link", url);
				replacementValues.put("otp", otp);

				String message = template.getTemplate(replacementValues);

				try {
					emailService.sendEmail(approverModel.getEmail(),
							AppConstant.VerifyApprover.VERIFY_APPROVER_SUBJECT_LINE, message);
				} catch (MessagingException e) { // TODO Auto-generated catch block
					e.printStackTrace();
				}

				approverModel.setPassword(token);

				User company = null;

				if (approverModel.getCompanyId() != null) {

					company = userDetailsService
							.convertToEntity(userDetailsService.findById(approverModel.getCompanyId()), company);
				}
				approverModel.setId(approver.getId());
				User findId = userDetailsService.convertToEntity(userDetailsService.updateUserData(approverModel),
						company);

				List<User> biddersList = new ArrayList<User>();
				List<BidderApprover> bidderApproverList = new ArrayList<>();
				for (int j = 0; j < bidderList.size(); j++) {
					User bidder = userDetailsService.findByIds(bidderList.get(j).getId());
					bidder.setApproverId(findId);
					biddersList.add(bidder);
					bidderApproverModel.setUserVendorBidder(bidder);
					bidderApproverModel.setApprovedBy(findId);
					bidderApproverList.add(bidderApproverModel);

				}
				userDetailsService.updateUserDataCustomer(biddersList);
				bidderApproverRepository.saveAll(bidderApproverList);

				response.add(convertToModel(userDetailsService.convertToModel(findId)));

			} else {
				throw new ResourceAlreadyExistsException(AppConstant.ErrorTypes.USER_EXIST_ERROR,
						AppConstant.ErrorCodes.USER_ALREADY_EXIST_ERROR_CODE,
						AppConstant.ErrorMessages.USER_ALREADY_EXIST);
			}

		}
		return response;
	}

	public List<VendorApproverModel> getVendorAllApproverByCompanyId(@NotBlank Integer id) {
		if (vendorCompanyService.checkAlreadyExists(id)) {
			List<UserModel> userList = userDetailsService.getAllVendorUserByCompanyId(id);
			List<UserModel> approver = new ArrayList<UserModel>();
			List<VendorApproverModel> userResponseList = new ArrayList<>();
			for (int i = 0; i < userList.size(); i++) {
				UserModel u = userList.get(i);
				for (RoleModels role : u.getRoleModel()) {
					if (role.getName().equals(AppConstant.RoleValues.ROLE_APPROVER)
							|| role.getName().equals(AppConstant.RoleValues.ROLE_BIDDER_APPROVER)) {

						approver.add(u);
					}
				}
			}
			VendorApproverModel approverModel = new VendorApproverModel();
			for (Integer j = 0; j < approver.size(); j++) {

				approverModel.setUserModel(approver.get(j));
				List<UserModel> bidder = vendorBidderService.getAllBidderByAproverId(approver.get(j).getId());
				List<UserListResponseModel> bidderList = bidder.stream().map(bid -> convertToModel(bid))
						.collect(Collectors.toList());
				approverModel.setBidderModel(bidderList);
				userResponseList.add(approverModel);
				approverModel = new VendorApproverModel();
			}

			return userResponseList;

		} else {
			throw new VendorCompanyNotFoundException(AppConstant.ErrorTypes.VENDOR_COMPANY_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.VENDOR_COMPANY_EXIST_ERROR_CODE,
					AppConstant.ErrorMessages.VENDOR_COMPANY_NOT_EXIST_MESSAGE);
		}
	}

	private UserListResponseModel convertToModel(UserModel model) {
		return UserListResponseModel.builder().id(model.getId()).email(model.getEmail()).name(model.getName()).build();

	}


	//-------Method to get counts for dashboard---------//

	public GetServiceRequestCount getCounts(Integer approverId){

		UserModel user = userDetailsService.findById(approverId);

		VendorCompany vendorCompany = vendorCompanyService.findByIds(user.getCompanyId());

		List<User> bidders = userRepository.getAllBiddersByCompanyId(vendorCompany.getId());

		if(bidders.size()<1){
			throw new UserNotFoundException(AppConstant.ErrorTypes.VENDOR_BIDDER_EXIST_ERROR,
					AppConstant.ErrorCodes.VENDOR_BIDDER_ERROR_CODE,
					AppConstant.ErrorMessages.VENDOR_BIDDER_NOT_EXIST);
		}

		List<Bids> bids = bidsRepository.findAllBiddersBids(bidders);

		List<Integer> bidsIds = bids.stream().map(Bids::getId).collect(Collectors.toList());

		Integer toApproveCount = approverBidRelationRepository.countByBidsAndBidStatus(bidsIds,
				ServiceRequestStatus.APPROVER_PENDING.name());

		Integer approvedCount = approverBidRelationRepository.countByApproverIdAndBidStatus(approverId,
				ServiceRequestStatus.APPROVED.name());

		return GetServiceRequestCount.builder().approved(approvedCount).toApprove(toApproveCount).build();

	}


	//----------Method to get all approved bids----------//

	public List<RequestBidsResponseModel> getAllApprovedBids(Integer approverId, Integer page){

		userDetailsService.findById(approverId);

		if(page==null){
			throw new PaginationException(AppConstant.ErrorTypes.PAGE_NUMBER_NULL,
					AppConstant.ErrorCodes.PAGE_NUMBER_NULL,AppConstant.ErrorMessages.PAGE_NUMBER_NULL);
		}

		List<ApproverBidRelation> approverBidRelations = approverBidRelationRepository
				.findByApproverIdAndBidStatus(approverId, ServiceRequestStatus.APPROVED.name(), PageRequest.of(page,size));

		if(approverBidRelations.size()<1){
			throw new BidsNotFoundException(AppConstant.ErrorTypes.BIDS_EXIST_ERROR,
					AppConstant.ErrorCodes.BIDS_ERROR_CODE,AppConstant.ErrorMessages.BIDS_NOT_EXIST);
		}

		return approverBidRelations.stream().map(approverBidRelation -> {

			Bids bid = bidsService.findByIds(approverBidRelation.getBidId());

			return RequestBidsResponseModel.builder().serviceRequestId(bid.getServiceRequest().getId())
				.bidId(bid.getId()).bidUID(bid.getBidUID()).bidderName(bid.getUserId().getName())
				.serviceRequestOn(bid.getServiceRequest().getServiceRequestOn())
				.vessel(bid.getServiceRequest().getVessel().getShipname())
				.category(bid.getServiceRequest().getEquipmentCategory().getName())
				.proposedAmount(bid.getTotalProposedAmount())
				.currency(bid.getServiceRequest().getPreferredCurrency()).build();
		}).collect(Collectors.toList());
	}


	//----------Method to get all to-approve bids----------//

	public List<RequestBidsResponseModel> getAllToApproveBids(Integer approverId, Integer page){

		if(page==null){
			throw new PaginationException(AppConstant.ErrorTypes.PAGE_NUMBER_NULL,
					AppConstant.ErrorCodes.PAGE_NUMBER_NULL,AppConstant.ErrorMessages.PAGE_NUMBER_NULL);
		}

		UserModel user = userDetailsService.findById(approverId);

		VendorCompany vendorCompany = vendorCompanyService.findByIds(user.getCompanyId());

		List<User> bidders = userRepository.getAllBiddersByCompanyId(vendorCompany.getId());

		if(bidders.size()<1){
			throw new UserNotFoundException(AppConstant.ErrorTypes.VENDOR_BIDDER_EXIST_ERROR,
					AppConstant.ErrorCodes.VENDOR_BIDDER_ERROR_CODE,
					AppConstant.ErrorMessages.VENDOR_BIDDER_NOT_EXIST);
		}

		List<Bids> bids = bidsRepository.findAllBiddersBids(bidders);

		List<Integer> bidsIds = bids.stream().map(Bids::getId).collect(Collectors.toList());

		List<ApproverBidRelation> approverBidRelations = approverBidRelationRepository
				.findByBidsAndBidStatus(bidsIds, ServiceRequestStatus.APPROVER_PENDING.name(), PageRequest.of(page,size));

		if(approverBidRelations.size()<1){
			throw new BidsNotFoundException(AppConstant.ErrorTypes.BIDS_EXIST_ERROR,
					AppConstant.ErrorCodes.BIDS_ERROR_CODE,AppConstant.ErrorMessages.BIDS_NOT_EXIST);
		}

		return approverBidRelations.stream().map(approverBidRelation -> {

			Bids bid = bidsService.findByIds(approverBidRelation.getBidId());

			return RequestBidsResponseModel.builder().serviceRequestId(bid.getServiceRequest().getId())
					.bidId(bid.getId()).bidUID(bid.getBidUID()).bidderName(bid.getUserId().getName())
					.serviceRequestOn(bid.getServiceRequest().getServiceRequestOn())
					.vessel(bid.getServiceRequest().getVessel().getShipname())
					.category(bid.getServiceRequest().getEquipmentCategory().getName())
					.proposedAmount(bid.getTotalProposedAmount())
					.currency(bid.getServiceRequest().getPreferredCurrency()).build();
		}).collect(Collectors.toList());
	}


	//--------Method to Approve or Decline the bid---------//

	public String approveOrDeclineBid(VendorApproverBidRequestModel approverBidRequestModel){

		User user = userDetailsService.findByIds(approverBidRequestModel.getApproverId());

		Bids bid = bidsService.findByIds(approverBidRequestModel.getBidId());

		ApproverBidRelation approverBidRelation = approverBidRelationRepository.findByBidId(approverBidRequestModel.getBidId());

		if(approverBidRequestModel.getBidStatus().equals(ServiceRequestStatus.APPROVED.name())) {
			approverBidRelation.setApproverId(approverBidRequestModel.getApproverId());
			approverBidRelation.setBidStatus(ServiceRequestStatus.APPROVED.name());
			approverBidRelationRepository.save(approverBidRelation);

			bid.setBidsStatus(ServiceRequestStatus.BID_RAISED.name());
			UserModel hoc = userDetailsService.getUserHeadByCompanyId(user.getCompanyId().getId());
			headOfCompanyRepository.save(HeadOfCompany.builder().headId(hoc.getId())
					.statusId(bid.getId()).status("RAISED_BID").build());
			bidsRepository.save(bid);

			return "Bid Approved and Raised Successfully";
		}
		else if(approverBidRequestModel.getBidStatus().equals(ServiceRequestStatus.DECLINED.name())){
			approverBidRelation.setApproverId(approverBidRequestModel.getApproverId());
			approverBidRelation.setBidStatus(ServiceRequestStatus.DECLINED.name());
			approverBidRelation.setDeclineReason(approverBidRequestModel.getDeclineReason());
			approverBidRelationRepository.save(approverBidRelation);

			bid.setBidsStatus(ServiceRequestStatus.DECLINED.name());
			bidsRepository.save(bid);

			return "Bid Declined Successfully";
		}
		else{
			throw new ServiceRequestStatusException(AppConstant.ErrorTypes.STATUS_NOT_FOUND,
					AppConstant.ErrorCodes.STATUS_NOT_FOUND,AppConstant.ErrorMessages.STATUS_NOT_FOUND);
		}
	}


	//---------Method to update bid by approver----------//

	public String updateBidByApprover(BidsModel bidsModel) throws Exception {

		if(bidsModel.getApproverId()==null){
			throw new VendorApproverNotFoundException(AppConstant.ErrorTypes.APPROVER_ID_NOT_PRESENT,
					AppConstant.ErrorCodes.APPROVER_ID_NOT_PRESENT, AppConstant.ErrorMessages.APPROVER_ID_NOT_PRESENT);
		}

		User user = userDetailsService.findByIds(bidsModel.getApproverId());

		if(user.getBiddingLimit()==null){
			throw new BidderApproverNotFoundException(AppConstant.ErrorTypes.BIDDING_LIMIT_NOT_ASSIGNED,
					AppConstant.ErrorCodes.BIDDING_LIMIT_NOT_ASSIGNED, AppConstant.ErrorMessages.BIDDING_LIMIT_NOT_ASSIGNED);
		}

		List<Bids> bidsList = bidsRepository.findByBidderAndRequestId(bidsModel.getBidderId(),
				serviceRequestService.findByIds(bidsModel.getServiceRequestId()).getId(),
				ServiceRequestStatus.BIDS_IN_PROGRESS.name());

		if (bidsList.size() > 0) {
			for (Bids b : bidsList) {
				bidsService.checkBidValidOrNot(bidsModel.getTotalProposedAmount(), b.getTotalProposedAmount());
			}
		}

		ApproverBidRelation approverBidRelation = approverBidRelationRepository.findByBidAndBidStatus(bidsModel.getId(),
				ServiceRequestStatus.APPROVER_PENDING.name());

		if(approverBidRelation==null){
			throw new BidsNotFoundException(AppConstant.ErrorTypes.BIDS_EXIST_ERROR,
					AppConstant.ErrorCodes.BIDS_ERROR_CODE, AppConstant.ErrorMessages.BID_NOT_FOUND_FOR_APPROVAL);
		}

		if(bidsModel.getTotalProposedAmount()>=Integer.parseInt(user.getBiddingLimit().replaceAll(",",""))){

			approverBidRelation.setApproverId(bidsModel.getApproverId());
			approverBidRelation.setBidStatus(ServiceRequestStatus.HOC_APPROVAL_PENDING.name());

			approverBidRelationRepository.save(approverBidRelation);

			bidsModel.setBidStatus(ServiceRequestStatus.HOC_APPROVAL_PENDING.name());
		}
		else{
			approverBidRelation.setApproverId(bidsModel.getApproverId());
			approverBidRelation.setBidStatus(ServiceRequestStatus.APPROVED.name());

			approverBidRelationRepository.save(approverBidRelation);
		}

		bidsService.updateSubmitedBid(bidsModel);

		return "Bid Update Successfully";
	}

}