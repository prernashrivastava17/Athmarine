package com.athmarine.service;

import javax.mail.MessagingException;
import javax.validation.constraints.NotBlank;

import com.athmarine.entity.*;
import com.athmarine.exception.*;
import com.athmarine.repository.HeadOfCompanyRepository;
import com.athmarine.request.*;
import com.athmarine.response.AdminCountsResponse;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.athmarine.repository.UserRepository;
import com.athmarine.repository.VendorCompanyRepository;
import com.athmarine.resources.AppConstant;

import java.util.List;
import java.util.Optional;

@Service
public class VendorAdminService {

	@Autowired
	private VendorCompanyRepository vendorCompanyRepository;

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	MasterStateService masterStateService;

	@Autowired
	public EmailService emailService;

	@Autowired
	public UserRepository userRepository;

	@Autowired
	public VendorCompanyService vendorCompanyServicel;

	@Autowired
	public OTPService otpService;

	@Autowired
	VendorCompanyRepository companyRepository;

	@Autowired
	Base64PasswordService passwordService;

	@Autowired
	VendorBidderService vendorBidderService;

	@Autowired
	VendorApproverService vendorApproverService;

	@Autowired
	EngineerEquimentService engineerEquimentService;

	@Autowired
	HeadOfCompanyRepository headOfCompanyRepository;

	@Autowired
	VendorCompanyHeadService vendorCompanyHeadService;

	@Value("${pagination.size}")
	private Integer size;

	public UserModel createVendorAdmin(UserModel userModel) throws MessagingException {
		if (userModel.getSameASUser().equals("No")) {
			if (!userRepository.existsByEmail(userModel.getEmail())) {
				if (userRepository.existsByPrimaryPhone(userModel.getPrimaryPhone())) {

					throw new PhoneNumberExistException(AppConstant.ErrorTypes.PHONE_NUMBER_ALREADY_EXIST_ERROR,
							AppConstant.ErrorCodes.PHONE_NUMBER_ALREADY_EXIST_ERROR_CODE, userModel.getPrimaryPhone()
							+ " " + AppConstant.ErrorMessages.PHONE_NUMBER_ALREADY_EXIST_ERROR);

				} else {
					String email=userModel.getEmail();
					new Thread(new Runnable() {

						@Override
						public void run() {
							otpService.createEmailOTP(email);

						}
					}).start();
				}

				User company = null;

				if (userModel.getCompanyId() != null) {

					company = userDetailsService.convertToEntity(userDetailsService.findById(userModel.getCompanyId()),
							company);
				}

				if (userRepository.existsByEmail(userModel.getEmail())) {

					throw new EmailExistException(AppConstant.ErrorTypes.EMAIL_EXIST_ERROR,
							AppConstant.ErrorCodes.EMAIL_EXIST_ERROR_CODE,
							AppConstant.ErrorMessages.EMAIL_EXIST_MESSAGE);

				}
				Integer count = userDetailsService.getAllAdminByCompanyId(userModel.getCompanyId());
				UserModel findId = userDetailsService.createUser(userModel);
				VendorCompany vendorCompany = vendorCompanyServicel.findByIds(company.getId());
				vendorCompany.setRegistrationStatus(RegistrationStatus.Admin);
				vendorCompany.setRegisteredSuccessfully(false);
				companyRepository.save(vendorCompany);
				UserModel getUserData = userDetailsService.findById(findId.getId());
				getUserData.setUid(userDetailsService.generateUniqueIdVendor(getUserData.getId(), count + 1));
				return userDetailsService.updateUserData(getUserData);
			} else {
				throw new EmailExistException(AppConstant.ErrorTypes.EMAIL_EXIST_ERROR,
						AppConstant.ErrorCodes.EMAIL_EXIST_ERROR_CODE,
						userModel.getEmail() + " " + AppConstant.ErrorMessages.EMAIL_EXIST_MESSAGE);

			}
		}
		else if (userModel.getSameASUser().equals("Yes")) {

			Optional<VendorCompany> vendorCompanyOptional = vendorCompanyRepository.findById(userModel.getCompanyId());

			if (vendorCompanyOptional.isPresent()) {
				VendorCompany company = vendorCompanyOptional.get();
				if(!userModel.getEmail().equals(company.getEmail()))
				{
					if(!userModel.getPrimaryPhone().equals(company.getPrimaryPhone()))
					{
						userModel.setSameASUser("No");
						return createVendorAdmin(userModel);
					}
					else{
						UserModel companyUser = userDetailsService.findById(company.getId());

						Integer count = userDetailsService.getAllAdminByCompanyId(userModel.getCompanyId());

						String email = userModel.getEmail();

						new Thread(new Runnable() {

							@Override
							public void run() {
								otpService.createEmailOTP(email);

							}
						}).start();

						companyUser.setEmail(userModel.getEmail());
						companyUser.setPrimaryPhone(userModel.getPrimaryPhone());
						companyUser.setPassword(userModel.getPassword());
//						companyUser.setUid(userDetailsService.generateUniqueIdVendor(company.getId(), count + 1));
						companyUser.setCompanyId(userModel.getCompanyId());
						companyUser.setName(userModel.getName());
						companyUser.setOnWhatsapp(userModel.isOnWhatsapp());
						companyUser.setAvailableOn(userModel.getAvailableOn());
						companyUser.setSameASUser(userModel.getSameASUser());
						User findId = null;
						findId = userRepository.save(userDetailsService.convertToEntity(companyUser,findId));
						company.setRegistrationStatus(RegistrationStatus.Admin);
						company.setRegisteredSuccessfully(false);
						companyRepository.save(company);

						UserModel getUserData = userDetailsService.findById(findId.getId());
						getUserData.setUid(userDetailsService.generateUniqueIdVendor(findId.getId(), count + 1));
						UserModel updateduserModel = userDetailsService.updateUserData(getUserData);
						return userModel=updateduserModel;
					}
				}
				else if(userModel.getEmail().equals(company.getEmail()))
				{
					UserModel companyUser = userDetailsService.findById(company.getId());

					Integer count = userDetailsService.getAllAdminByCompanyId(userModel.getCompanyId());

					String email = userModel.getEmail();

					new Thread(new Runnable() {

						@Override
						public void run() {
							otpService.createEmailOTP(email);

						}
					}).start();

					companyUser.setEmail(userModel.getEmail());
					companyUser.setPrimaryPhone(userModel.getPrimaryPhone());
					companyUser.setPassword(userModel.getPassword());
//					companyUser.setUid(userDetailsService.generateUniqueIdVendor(company.getId(), count + 1));
					companyUser.setCompanyId(userModel.getCompanyId());
					companyUser.setName(userModel.getName());
					companyUser.setOnWhatsapp(userModel.isOnWhatsapp());
					companyUser.setAvailableOn(userModel.getAvailableOn());
					companyUser.setSameASUser(userModel.getSameASUser());
					User findId = null;
					findId = userRepository.save(userDetailsService.convertToEntity(companyUser,findId));

					company.setRegistrationStatus(RegistrationStatus.Admin);
					company.setRegisteredSuccessfully(false);
					companyRepository.save(company);

					UserModel getUserData = userDetailsService.findById(findId.getId());
					getUserData.setUid(userDetailsService.generateUniqueIdVendor(findId.getId(), count + 1));
					UserModel updateduserModel = userDetailsService.updateUserData(getUserData);
					return userModel=updateduserModel;
				}
				else {
					String password = userModel.getPassword();
					userModel = userDetailsService.updateSameUser(userModel);
					String email = userModel.getEmail();
					new Thread(new Runnable() {

						@Override
						public void run() {
							otpService.createEmailOTP(email);

						}
					}).start();
					if (userModel.getCompanyId() == 0 || userModel.getCompanyId() == null) {
						userModel.setPassword(password);
						userModel.setCompanyId(userModel.getId());
						userDetailsService.updateCompany(userModel);
						VendorCompany vendorCompany = vendorCompanyServicel.findByIds(company.getId());
						vendorCompany.setRegistrationStatus(RegistrationStatus.Admin);
						vendorCompany.setRegisteredSuccessfully(false);
						companyRepository.save(vendorCompany);
					}

				}
			}
		}
		return userModel;
	}

	public UserModel updateVendorAdmin(UserModel model) {

		UserModel user = userDetailsService.findById(model.getId());

		model.setId(user.getId());
		model.setUid(user.getUid());
		return userDetailsService.updateUserData(model);

	}

	public UserModel findVendorAdminById(@NotBlank int id) {

		return userDetailsService.findById(id);

	}

	public UserModel deleteVendorAdmin(Integer id) {

		UserModel user = userDetailsService.findById(id);
		User companyId = userDetailsService.findByIds(user.getCompanyId());

		user.setStatus(AppConstant.DeleteStatus.STATUS_DELETE);
		return userDetailsService
				.convertToModel(userRepository.save(userDetailsService.convertToEntity(user, companyId)));

	}

	public boolean checkAlreadyExists(int id) {
		return vendorCompanyRepository.existsById(id);

	}

	public VendorCompanyModel findById(@NotBlank Integer id) {

		if (id == null) {
			throw new ResourceNotFoundException(AppConstant.ErrorTypes.ID_NULL_EXIST_ERROR,
					AppConstant.ErrorCodes.ID_NULL_ERROR_CODE, AppConstant.ErrorMessages.ID_EMPTY_MESSAGE);
		} else {

			return vendorCompanyServicel.convertToModel(vendorCompanyRepository.findById(id)
					.orElseThrow(() -> new VendorCompanyNotFoundException(
							AppConstant.ErrorTypes.VENDOR_COMPANY_NOT_EXIST_ERROR,
							AppConstant.ErrorCodes.VENDOR_COMPANY_EXIST_ERROR_CODE,
							AppConstant.ErrorMessages.VENDOR_COMPANY_NOT_EXIST_MESSAGE)));
		}

	}


	//----------Method to get all counts for the dashboard------------//

	public AdminCountsResponse getAllCounts(Integer adminId){

		User user = userDetailsService.findByIds(adminId);

		User vendorCompany = user.getCompanyId();

		Integer activeUsers = userDetailsService.getAllUserByVendorCompanyId(vendorCompany.getId()).size();

		Integer activeBidders = userDetailsService.getAllBidderByCompanyId(vendorCompany.getId());

		Integer activeApprovers = userDetailsService.getAllVendorApproverByCompanyId(vendorCompany.getId());

		Integer activeEngineers = userDetailsService.getAllEngineerByCompanyId(vendorCompany.getId());

		Integer activeFinancers = userDetailsService.getAllFinancerByCompanyId(vendorCompany.getId());

		return AdminCountsResponse.builder().activeUsers(activeUsers).activeBidders(activeBidders)
				.activeApprovers(activeApprovers).activeEngineers(activeEngineers)
				.activeFinancers(activeFinancers).build();
	}

	//----------Method to get all active users-----------//

	public List<UserModel> getAllActiveUsers(Integer adminId, Integer page) {

		if(page==null){
			throw new PaginationException(AppConstant.ErrorTypes.PAGE_NUMBER_NULL,
					AppConstant.ErrorCodes.PAGE_NUMBER_NULL,AppConstant.ErrorMessages.PAGE_NUMBER_NULL);
		}

		User user = userDetailsService.findByIds(adminId);

		List<UserModel> usersList = userDetailsService.getAllVendorUserByCompanyId(user.getCompanyId().getId());

		usersList.forEach(userModel -> {
			HeadOfCompany headOfCompany = headOfCompanyRepository.findByUserIdAndStatus(userModel.getId(),
					"UPDATE_USER_APPROVAL");

			if(headOfCompany==null){
				headOfCompany = headOfCompanyRepository.findByUserIdAndStatus(userModel.getId(),
						"UPDATE_USER_DECLINED");
				if(headOfCompany==null)
					userModel.setUpdateType("Updated");
				else
					userModel.setUpdateType("Declined");
			}
			else
				userModel.setUpdateType("Pending");
		});

		if(((page*size)+size)<usersList.size()) {
			return usersList.subList(page * size, (page * size) + size);
		}
		else if((page*size)<usersList.size()){
			return usersList.subList(page * size, usersList.size());
		}
		else{
			return usersList;
		}
	}

	//----------Method to get all active bidders-----------//

	public List<UserModel> getAllActiveBidders(Integer adminId, Integer page) {

		if(page==null){
			throw new PaginationException(AppConstant.ErrorTypes.PAGE_NUMBER_NULL,
					AppConstant.ErrorCodes.PAGE_NUMBER_NULL,AppConstant.ErrorMessages.PAGE_NUMBER_NULL);
		}

		User user = userDetailsService.findByIds(adminId);

		List<UserModel> usersList = vendorBidderService.getAllBidderByCMPId(user.getCompanyId().getId());

		if(((page*size)+size)<usersList.size()) {
			return usersList.subList(page * size, (page * size) + size);
		}
		else if((page*size)<usersList.size()){
			return usersList.subList(page * size, usersList.size());
		}
		else{
			return usersList;
		}
	}

	//----------Method to get all active approvers-----------//

	public List<VendorApproverModel> getAllActiveApprovers(Integer adminId, Integer page) {

		if(page==null){
			throw new PaginationException(AppConstant.ErrorTypes.PAGE_NUMBER_NULL,
					AppConstant.ErrorCodes.PAGE_NUMBER_NULL,AppConstant.ErrorMessages.PAGE_NUMBER_NULL);
		}

		User user = userDetailsService.findByIds(adminId);

		List<VendorApproverModel> usersList = vendorApproverService.getVendorAllApproverByCompanyId(user.getCompanyId().getId());

		if(((page*size)+size)<usersList.size()) {
			return usersList.subList(page * size, (page * size) + size);
		}
		else if((page*size)<usersList.size()){
			return usersList.subList(page * size, usersList.size());
		}
		else{
			return usersList;
		}
	}

	//----------Method to get all active engineers-----------//

	public List<EngineerEquimentsModel> getAllActiveEngineers(Integer adminId, Integer page) {

		if(page==null){
			throw new PaginationException(AppConstant.ErrorTypes.PAGE_NUMBER_NULL,
					AppConstant.ErrorCodes.PAGE_NUMBER_NULL,AppConstant.ErrorMessages.PAGE_NUMBER_NULL);
		}

		User user = userDetailsService.findByIds(adminId);

		List<EngineerEquimentsModel> usersList = engineerEquimentService.getEngineerEquipmentByCompanyId(user.getCompanyId().getId());

		if(((page*size)+size)<usersList.size()) {
			return usersList.subList(page * size, (page * size) + size);
		}
		else if((page*size)<usersList.size()){
			return usersList.subList(page * size, usersList.size());
		}
		else{
			return usersList;
		}
	}

	//----------Method to get all active financers-----------//

	public List<UserModel> getAllActiveFinancers(Integer adminId, Integer page) {

		if(page==null){
			throw new PaginationException(AppConstant.ErrorTypes.PAGE_NUMBER_NULL,
					AppConstant.ErrorCodes.PAGE_NUMBER_NULL,AppConstant.ErrorMessages.PAGE_NUMBER_NULL);
		}

		User user = userDetailsService.findByIds(adminId);

		List<UserModel> usersList = userDetailsService.getAllFinanceByCompanyId(user.getCompanyId().getId());

		if(((page*size)+size)<usersList.size()) {
			return usersList.subList(page * size, (page * size) + size);
		}
		else if((page*size)<usersList.size()){
			return usersList.subList(page * size, usersList.size());
		}
		else{
			return usersList;
		}
	}


	//----------Method to get active HOC details----------//

	public UserModel getActiveHOC(Integer adminId){
		User user  = userDetailsService.findByIds(adminId);

		return userDetailsService.getUserHeadByCompanyId(user.getCompanyId().getId());

	}


	//-----------Method to update a user's access----------//

	public String updateUserAccess(Integer userId){
		User user = userDetailsService.findByIds(userId);

		UserModel companyHead = userDetailsService.getUserHeadByCompanyId(user.getCompanyId().getId());

		if(companyHead==null) {
			throw new VendorHeadNotFoundException(AppConstant.ErrorTypes.VENDOR_HEAD_NOT_FOUND,
					AppConstant.ErrorCodes.COMPANY_HEAD_DOES_NOT_EXIST_ERROR_CODE,
					AppConstant.ErrorMessages.COMPANY_HEAD_DOES_NOT_EXIST_MESSAGE);
		}

		headOfCompanyRepository.save(HeadOfCompany.builder().headId(companyHead.getId())
				.status("USER_ACCESS_APPROVAL").statusId(userId).build());

		return "The user's access request has been sent to Head of Company. Wait for the approval";
	}


	//---------Method to create new user by admin------------//

	public UserModel createNewUser(UserModel userModel){

		UserModel user = userDetailsService.createUser(userModel);

		String role = user.getRoleModel().get(0).getName();

		Integer count=0;

		if (role.equalsIgnoreCase("Admin")) {
			count = userDetailsService.getAllAdminByCompanyId(user.getCompanyId());
		} else if (role.equalsIgnoreCase("Engineer")) {
			count = userDetailsService.getAllEngineerByCompanyId(user.getCompanyId());
		} else if (role.equalsIgnoreCase("Bidder")) {
			count = userDetailsService.getAllBidderByCompanyId(user.getCompanyId());
		} else if (role.equalsIgnoreCase("Approver")) {
			count = userDetailsService.getAllVendorApproverByCompanyId(user.getCompanyId());
		} else if (role.equalsIgnoreCase("Head")) {
			count = 1;
		} else if (role.equalsIgnoreCase("Finance")) {
			count = userDetailsService.getAllFinancerByCompanyId(user.getCompanyId());
		}
		user.setUid(userDetailsService.generateUniqueIdVendor(user.getId(), count + 1));
		UserModel userModel1 = userDetailsService.updateUserData(user);

		UserModel headOfCompany = userDetailsService.getUserHeadByCompanyId(userModel1.getCompanyId());

		headOfCompanyRepository.save(HeadOfCompany.builder().headId(headOfCompany.getId())
				.statusId(userModel1.getId()).status("NEW_USER_APPROVAL").build());

		return userModel1;
	}

	//--------Method to Create New Vendor Head----------//

	public UserModel createNewVendorHead(UserModel headModel){

		if (!userRepository.existsByEmail(headModel.getEmail())){
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
			Integer count = userDetailsService.getAllVendorHeadByCompanyId(headModel.getCompanyId());
			UserModel findId = userDetailsService.createUser(vendorCompanyHeadService.convertToModel(headModel));
			UserModel getUserData = userDetailsService.findById(findId.getId());

			getUserData.setUid(userDetailsService.generateUniqueIdVendor(getUserData.getId(), count + 1));
			getUserData.setUserAccess(false);
			return userDetailsService.updateUserData(getUserData);
		} else {
			throw new EmailExistException(AppConstant.ErrorTypes.EMAIL_EXIST_ERROR,
					AppConstant.ErrorCodes.EMAIL_EXIST_ERROR_CODE, AppConstant.ErrorMessages.EMAIL_EXIST_MESSAGE);
		}
	}

	//---------Method to Update a User Details---------//

	public String updateUser(UserModel userModel){

		User user = userDetailsService.findByIds(userModel.getId());

		HeadOfCompany hoc = headOfCompanyRepository.findByUserIdAndStatus(userModel.getId(),
				"UPDATE_USER_APPROVAL");

		if(hoc!=null){
			throw new ResourceAlreadyExistsException(AppConstant.ErrorTypes.UPDATE_APPROVAL_ALREADY_PENDING,
					AppConstant.ErrorCodes.UPDATE_APPROVAL_ALREADY_PENDING,
					AppConstant.ErrorMessages.UPDATE_APPROVAL_ALREADY_PENDING);
		}

		String userJson = new Gson().toJson(userModel);

		user.setUpdateData(userJson);

		userRepository.save(user);

		UserModel headOfCompany = userDetailsService.getUserHeadByCompanyId(userModel.getCompanyId());

		headOfCompanyRepository.save(HeadOfCompany.builder().headId(headOfCompany.getId())
				.statusId(userModel.getId()).status("UPDATE_USER_APPROVAL").build());

		return "The changes of details have been saved successfully";
	}

	//---------Method to details User's Updated Details--------//

	public UserModel getUserUpdatedDetails(Integer userId){

		User user = userDetailsService.findByIds(userId);

		HeadOfCompany headOfCompany = headOfCompanyRepository.findByUserIdAndStatus(userId, "UPDATE_USER_APPROVAL");

		if(headOfCompany==null) {
			throw new UserNotFoundException(AppConstant.ErrorTypes.USER_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.USER_DOES_NOT_EXISTS_ERROR_CODE,
					AppConstant.ErrorMessages.NEW_USERS_NOT_FOUND);
		}

		String updateDetails = user.getUpdateData();

		return new Gson().fromJson(updateDetails,UserModel.class);
	}

	//----------Method to get User's Update Decline Reason--------//

	public String getUpdateDeclineReason(Integer userId){

		userDetailsService.findByIds(userId);
		HeadOfCompany headOfCompany = headOfCompanyRepository.findByUserIdAndStatus(userId, "UPDATE_USER_DECLINED");

		if(headOfCompany==null) {
			throw new UserNotFoundException(AppConstant.ErrorTypes.USER_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.USER_DOES_NOT_EXISTS_ERROR_CODE,
					AppConstant.ErrorMessages.NEW_USERS_NOT_FOUND);
		}

		return headOfCompany.getDeclineReason();
	}

	//----------Method to get User By Id and Role---------//

	public Object getUserByIdAndRole(Integer userId, String role){
		User user = userDetailsService.findByIds(userId);
		Object userModel=null;
		switch (role) {
			case "Bidder":
				List<UserModel> userModels = vendorBidderService.getAllBidderByCMPId(user.getCompanyId().getId());
				for (UserModel u : userModels) {
					if (u.getId().equals(userId))
						userModel = u;
				}
				break;
			case "Engineer":
				List<EngineerEquimentsModel> engineerEquipmentsModels = engineerEquimentService
						.getEngineerEquipmentByCompanyId(user.getCompanyId().getId());
				for (EngineerEquimentsModel e : engineerEquipmentsModels) {
					if (e.getUserModel().getId().equals(userId))
						userModel = e;
				}
				break;
			case "Approver":
				List<VendorApproverModel> vendorApproverModels = vendorApproverService
						.getVendorAllApproverByCompanyId(user.getCompanyId().getId());
				for (VendorApproverModel v : vendorApproverModels) {
					if (v.getUserModel().getId().equals(userId))
						userModel = v;
				}
				break;
		}

		return userModel;
	}
}