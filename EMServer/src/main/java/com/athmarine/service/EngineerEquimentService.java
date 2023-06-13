package com.athmarine.service;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.EngineerCertificates;
import com.athmarine.entity.EngineerEquiments;
import com.athmarine.entity.Equipment;
import com.athmarine.entity.Experience;
import com.athmarine.entity.MSTEquipmentCategory;
import com.athmarine.entity.Make;
import com.athmarine.entity.RegistrationStatus;
import com.athmarine.entity.User;
import com.athmarine.entity.VendorCompany;
import com.athmarine.exception.EmailExistException;
import com.athmarine.exception.EngineerEquimentsNotFoundException;
import com.athmarine.exception.EquipmentNotFoundException;
import com.athmarine.exception.PhoneNumberExistException;
import com.athmarine.exception.ResourceAlreadyExistsException;
import com.athmarine.exception.VendorCompanyNotFoundException;
import com.athmarine.repository.EngineerCertificateRepository;
import com.athmarine.repository.EngineerEquimentsRepository;
import com.athmarine.repository.ExperienceRepository;
import com.athmarine.repository.UserRepository;
import com.athmarine.repository.VendorCompanyRepository;
import com.athmarine.request.EngineerCertificateModel;
import com.athmarine.request.EngineerEquimentsModel;
import com.athmarine.request.EngineerEquipmentModel;
import com.athmarine.request.EngineerModels;
import com.athmarine.request.EquipmentModel;
import com.athmarine.request.ExperienceModel;
import com.athmarine.request.MakeModel;
import com.athmarine.request.MasterEquipmentCategoryModel;
import com.athmarine.request.UserListResponseModel;
import com.athmarine.request.UserModel;
import com.athmarine.resources.AppConstant;

@Service
public class EngineerEquimentService {

	@Autowired
	EngineerEquimentsRepository engineerEquimentsRepository;

	@Autowired
	EquipmentService equipmentService;

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	MasterEquipmentCategoryService categoryService;

	@Autowired
	EngineerCertificateService engineerCertificateService;

	@Autowired
	EngineerCertificateRepository certificateRepository;

	@Autowired
	public EmailService emailService;

	@Autowired
	public OTPService otpService;

	@Autowired
	public VendorCompanyService vendorCompanyService;

	@Autowired
	public MakeService makeService;

	@Autowired
	public ExperienceRepository experienceRepository;

	@Autowired
	VendorCompanyRepository companyRepository;

	public List<UserListResponseModel> createEngineerEquipments(List<EngineerEquimentsModel> engineerEquimentsModel) {

		List<UserListResponseModel> response = new ArrayList<UserListResponseModel>();

		for (int i = 0; i < engineerEquimentsModel.size(); i++) {
			UserModel engineerUsModel = engineerEquimentsModel.get(i).getUserModel();
			if (!engineerUsModel.getSameASUser().equals("Yes")) {
				if (!engineerEquimentsModel.get(i).getYesOrNo().equals("Yes")) {
					if (userRepository.existsByEmail(engineerUsModel.getEmail())) {

						throw new EmailExistException(AppConstant.ErrorTypes.EMAIL_EXIST_ERROR,
								engineerUsModel.getEmail(),
								engineerUsModel.getEmail() + " " + AppConstant.ErrorMessages.EMAIL_EXIST_MESSAGE);

					}
					if (userRepository.existsByPrimaryPhone(engineerUsModel.getPrimaryPhone())) {

						throw new PhoneNumberExistException(AppConstant.ErrorTypes.PHONE_NUMBER_ALREADY_EXIST_ERROR,
								engineerUsModel.getPrimaryPhone(), engineerUsModel.getPrimaryPhone() + " "
										+ AppConstant.ErrorMessages.PHONE_NUMBER_ALREADY_EXIST_ERROR);

					}
				}
			}

		}

		for (int j = 0; j < engineerEquimentsModel.size(); j++) {
			UserModel userModel = engineerEquimentsModel.get(j).getUserModel();

			EngineerEquimentsModel model = engineerEquimentsModel.get(j);
			if (userModel.getSameASUser().equals("No")) {

				if (engineerEquimentsModel.get(j).getYesOrNo().equals("No")) {

					if (!userRepository.existsByEmail(userModel.getEmail())) {

						if (userRepository.existsByPrimaryPhone(userModel.getPrimaryPhone())) {

							throw new PhoneNumberExistException(AppConstant.ErrorTypes.PHONE_NUMBER_ALREADY_EXIST_ERROR,
									AppConstant.ErrorCodes.PHONE_NUMBER_ALREADY_EXIST_ERROR_CODE,
									AppConstant.ErrorMessages.PHONE_NUMBER_ALREADY_EXIST_ERROR);

						}
						String userEmail = userModel.getEmail();
						new Thread(new Runnable() {

							@Override
							public void run() {
								otpService.createEmailVerificationLink(userEmail);
							}
						}).start();
						engineerEquimentsModel.get(j).setUserModel(userModel);
						Integer count = userDetailsService.getAllEngineerByCompanyId(userModel.getCompanyId());
						userModel = userDetailsService.createUser(userModel);
						userModel.setUid(userDetailsService.generateUniqueIdVendor(userModel.getId(), count + j + 1));
						userDetailsService.updateUserData(userModel);
						model.setUserModel(userModel);
						EngineerEquiments entity = convertToEntity(model);

						EngineerEquiments entityResponse = engineerEquimentsRepository.save(entity);
						List<EngineerCertificates> certificates = new ArrayList<EngineerCertificates>();
						List<Experience> experiences = new ArrayList<Experience>();

						for (int x = 0; x < engineerEquimentsModel.get(j).getEngineerModelList().size(); x++) {

							List<EngineerModels> engineerModels = engineerEquimentsModel.get(j).getEngineerModelList();

							Experience experience = new Experience();

							List<EngineerCertificateModel> engineerCertificateModels = engineerEquimentsModel.get(j).getEngineerModelList().get(x)
									.getCertificate();

							for(int y =0; y < engineerCertificateModels.size(); y++) {
								EngineerCertificates certificate = new EngineerCertificates();
							certificate.setEnggequip(entityResponse);
							certificate.setCertificates(engineerCertificateModels.get(y).getCertificates());

								experience.setEnggequip(entityResponse);
								experience.setExperience(
										engineerEquimentsModel.get(j).getEngineerModelList().get(x).getExperience());
								certificates.add(certificate);
							}
							experiences.add(experience);
						}
						certificateRepository.saveAll(certificates);
						experienceRepository.saveAll(experiences);
						response.add(convertToModel(userModel));
					} else {
						throw new ResourceAlreadyExistsException(AppConstant.ErrorTypes.EMAIL_EXIST_ERROR,
								AppConstant.ErrorCodes.EMAIL_EXIST_ERROR_CODE,
								AppConstant.ErrorMessages.EMAIL_EXIST_MESSAGE);
					}
				} else {

					String engineersId = engineerEquimentsModel.get(j).getEngineerId();

					Integer companyId = engineerEquimentsModel.get(j).getUserModel().getCompanyId();

					UserModel userModels = userDetailsService.findByUid(engineersId);
					userModels.setCompanyId(companyId);
					userModels.setPassword(null);
					userModels.setStatus(0);
					String userEmail = userModels.getEmail();
					new Thread(new Runnable() {

						@Override
						public void run() {
							otpService.createEmailVerificationLink(userEmail);
						}
					}).start();
					userModel = userDetailsService.updateUserData(userModels);
					model.setUserModel(userModel);
					EngineerEquiments entity = convertToEntity(model);
					EngineerEquipmentModel engineerEquipmentModel = getEngineerId(userModel.getId());
					engineerEquipmentModel.setUserModel(null);
					updateEngineerEquipment(engineerEquipmentModel);
					EngineerEquiments entityResponse = engineerEquimentsRepository.save(entity);
					List<EngineerCertificates> certificates = new ArrayList<EngineerCertificates>();
					List<Experience> experiences = new ArrayList<Experience>();

					for (int x = 0; x < engineerEquimentsModel.get(j).getEngineerModelList().size(); x++) {
						Experience experience = new Experience();

						List<EngineerCertificateModel> engineerCertificateModels = engineerEquimentsModel.get(j).getEngineerModelList().get(x)
								.getCertificate();

						for(int y =0; y < engineerCertificateModels.size(); y++) {
							EngineerCertificates certificate = new EngineerCertificates();
							certificate.setEnggequip(entityResponse);
							certificate.setCertificates(engineerCertificateModels.get(y).getCertificates());
							experience.setEnggequip(entityResponse);
							experience.setExperience(engineerEquimentsModel.get(j).getEngineerModelList().get(x).getExperience());
							certificates.add(certificate);
						}
						experiences.add(experience);
					}
					certificateRepository.saveAll(certificates);
					experienceRepository.saveAll(experiences);

				}

				VendorCompany vendorCompany = vendorCompanyService
						.findByIds(engineerEquimentsModel.get(0).getUserModel().getCompanyId());
				vendorCompany.setRegistrationStatus(RegistrationStatus.Engineer);
				companyRepository.save(vendorCompany);
			} else if (userModel.getSameASUser().equals("Yes")) {

				model.setUserModel(userDetailsService.findByEmail(userModel.getEmail()));
				EngineerEquiments entity = convertToEntity(model);

				EngineerEquiments entityResponse = engineerEquimentsRepository.save(entity);
				List<EngineerCertificates> certificates = new ArrayList<EngineerCertificates>();
				List<Experience> experiences = new ArrayList<Experience>();

				for (int x = 0; x < engineerEquimentsModel.get(j).getEngineerModelList().size(); x++) {
					Experience experience = new Experience();

					List<EngineerCertificateModel> engineerCertificateModels = engineerEquimentsModel.get(j).getEngineerModelList().get(x)
							.getCertificate();

					for(int y =0; y < engineerCertificateModels.size(); y++) {
						EngineerCertificates certificate = new EngineerCertificates();
						certificate.setEnggequip(entityResponse);
						certificate.setCertificates(engineerCertificateModels.get(y).getCertificates());
						experience.setEnggequip(entityResponse);
						experience.setExperience(engineerEquimentsModel.get(j).getEngineerModelList().get(x).getExperience());
						certificates.add(certificate);
					}
					experiences.add(experience);
				}
				certificateRepository.saveAll(certificates);
				experienceRepository.saveAll(experiences);

				UserModel vendorRegistrationStatus = userDetailsService.updateSameUser(userModel);
				Integer companyIdInteger = 0;
				if (vendorRegistrationStatus.getCompanyId() == 0 || vendorRegistrationStatus.getCompanyId() == null) {
					companyIdInteger = vendorRegistrationStatus.getId();
					UserModel updateModel = vendorRegistrationStatus;
					updateModel.setCompanyId(companyIdInteger);
					userDetailsService.updateCompany(updateModel);
				} else {
					companyIdInteger = vendorRegistrationStatus.getCompanyId();
				}

				VendorCompany vendorCompany = vendorCompanyService.findByIds(companyIdInteger);
				vendorCompany.setRegistrationStatus(RegistrationStatus.Engineer);
				companyRepository.save(vendorCompany);
			}
		}
		return response;

	}

	public void deleteEngineerEquiment(Integer id) {

		EngineerEquiments eng = findByIds(id);

		engineerEquimentsRepository.delete(eng);

	}

	public EngineerEquipmentModel updateEngineerEquipment(EngineerEquipmentModel engineerEquipmentModel) {

		return convertToEngineerEquipmentModel(
				engineerEquimentsRepository.save(convertToEngineerEquipmentEntity(engineerEquipmentModel)));

	}

	public EngineerEquimentsModel updateEngineerEquipments(EngineerEquimentsModel engineerEquipmentModel) {

		return convertToModels(engineerEquimentsRepository.save(convertToEntity(engineerEquipmentModel)));

	}

	public EngineerEquipmentModel getEngineerId(Integer id) {

		User user = userDetailsService.findByIds(id);
		return convertToEngineerEquipmentModel(engineerEquimentsRepository.findByEngineerId(user));

	}

	public EngineerEquimentsModel getEngineerByUid(String id) {

		User user = userRepository.getUserByUid(id);
		if(user==null) {
			throw new EngineerEquimentsNotFoundException(AppConstant.ErrorTypes.EQUIPMENT_EXIST_ERROR,
					AppConstant.ErrorCodes.EQUIPMENT_ERROR_CODE, AppConstant.ErrorMessages.EQUIPMENT_NOTS_EXIST);
		}
		return convertToModels(engineerEquimentsRepository.findByEngineerId(user));

	}

	public EngineerEquimentsModel deleteEngineerEquipments(@NotBlank int id) {

		EngineerEquimentsModel engineerEquiments = findById(id);

		engineerEquiments.setStatus(AppConstant.DeleteStatus.STATUS_DELETE);

		return convertToModels(engineerEquimentsRepository.save(convertToEntity(engineerEquiments)));
	}

	/// get All Engineer(User) By companyId
	public List<UserModel> getAllEngineerByCompanyId2(@NotBlank Integer id) {

		if (vendorCompanyService.checkAlreadyExists(id)) {
			List<UserModel> userList = userDetailsService.getAllEngineersByCompanyId(id);
			return userList;

		} else {
			throw new VendorCompanyNotFoundException(AppConstant.ErrorTypes.VENDOR_COMPANY_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.VENDOR_COMPANY_EXIST_ERROR_CODE,
					AppConstant.ErrorMessages.VENDOR_COMPANY_NOT_EXIST_MESSAGE);
		}
	}

	public List<EngineerEquimentsModel> getEngineerEquipmentByCompanyId(@NotBlank Integer id) {
		List<EngineerEquimentsModel> response = new ArrayList<EngineerEquimentsModel>();
		if (vendorCompanyService.checkAlreadyExists(id)) {

			List<UserModel> engineer = getAllEngineerByCompanyId2(id);
			for (int i = 0; i < engineer.size(); i++) {

				response.add(convertToModels(engineerEquimentsRepository
						.getEngineerEquimentsByEngineerId(userDetailsService.findByIds(engineer.get(i).getId()))));

			}

		} else {
			throw new VendorCompanyNotFoundException(AppConstant.ErrorTypes.VENDOR_COMPANY_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.VENDOR_COMPANY_EXIST_ERROR_CODE,
					AppConstant.ErrorMessages.VENDOR_COMPANY_NOT_EXIST_MESSAGE);
		}
		return response;
	}

	public EngineerEquimentsModel findById(Integer id) {
		return convertToModels(engineerEquimentsRepository.findById(id)
				.orElseThrow(() -> new EngineerEquimentsNotFoundException(
						AppConstant.ErrorTypes.ENGINEER_EQUIMENTS_EXTIS_ERROR,
						AppConstant.ErrorCodes.ENGINEER_EQUIMENTS_EXTIS_CODE,
						AppConstant.ErrorMessages.ENGINEER_EQUIMENTS_EXTIS_MESSAGE)));

	}

	public EngineerEquiments findByIds(Integer id) {

		if (id == null) {

			throw new EngineerEquimentsNotFoundException(AppConstant.ErrorTypes.EQUIPMENT_EXIST_ERROR,
					AppConstant.ErrorCodes.EQUIPMENT_ERROR_CODE, AppConstant.ErrorMessages.EQUIPMENT_NOT_EXIST);

		}
		return engineerEquimentsRepository.findById(id)
				.orElseThrow(() -> new EquipmentNotFoundException(AppConstant.ErrorTypes.EQUIPMENT_EXIST_ERROR,
						AppConstant.ErrorCodes.EQUIPMENT_ERROR_CODE, AppConstant.ErrorMessages.EQUIPMENT_NOT_EXIST));
	}

	public EngineerEquiments convertToEntity(EngineerEquimentsModel model) {

		List<Equipment> equipmentList = new ArrayList<Equipment>();
		List<MSTEquipmentCategory> categoryList = new ArrayList<MSTEquipmentCategory>();
		List<EngineerCertificates> certificateList = new ArrayList<EngineerCertificates>();
		List<Make> makeList = new ArrayList<Make>();
		for (int i = 0; i < model.getEngineerModelList().size(); i++) {

			equipmentList
					.add(equipmentService.convertToEntity(model.getEngineerModelList().get(i).getEquipmentModel()));

			categoryList.add(categoryService.convertToEntity(model.getEngineerModelList().get(i).getCategoryModel()));

			List<EngineerCertificateModel> engineerCertificateModelList = model.getEngineerModelList().get(i).getCertificate();

			for(int j=0; j<engineerCertificateModelList.size();j++) {
				certificateList.add(
						engineerCertificateService.convertToEntity(engineerCertificateModelList.get(j)));
			}
			if (model.getEngineerModelList().get(i).getMake().getId() == 0) {
				makeList.add(makeService.createMake((model.getEngineerModelList().get(i).getMake())));
			} else {
				makeList.add(makeService.convertToEntity(model.getEngineerModelList().get(i).getMake()));
			}

		}

		return EngineerEquiments.builder().id(model.getId()).yesOrNo(model.getYesOrNo())
				.engineersId(model.getEngineerId())
				.engineerId(userDetailsService.findByIds(model.getUserModel().getId())).status(model.getStatus())
				.equimentId(equipmentList).categoryId(categoryList).engineerCertificates(certificateList)
				.certified(certificateList.size() > 0 ? "Yes" : "No").makeId(makeList).build();
	}

	public EngineerEquimentsModel convertToModels(EngineerEquiments entity) {

		List<EngineerModels> engineerModels = new ArrayList<EngineerModels>();

		for (int i = 0; i < entity.getCategoryId().size(); i++) {
			EngineerModels detailsModel = new EngineerModels();
			MasterEquipmentCategoryModel categoryModel = categoryService.convertToModel(entity.getCategoryId().get(i));
			EquipmentModel equipmentModel = equipmentService.convertToModel(entity.getEquimentId().get(i));

			List<EngineerCertificates> engineerCertificatesList = entity.getEngineerCertificates();
			List<EngineerCertificateModel> certificateModel = new ArrayList<>();
			for(EngineerCertificates j : engineerCertificatesList) {
				certificateModel.add(engineerCertificateService
						.convertToModel(j));
			}

			MakeModel makeModel = makeService.convertToModel(entity.getMakeId().get(i));
			if (entity.getExperience().size() > 0) {

				ExperienceModel experienceModel = convertToExperienceModel(entity.getExperience().get(i));
				detailsModel.setCategoryModel(categoryModel);
				detailsModel.setEquipmentModel(equipmentModel);
				detailsModel.setCertificate(certificateModel);
				detailsModel.setMake(makeModel);
				detailsModel.setExperience(experienceModel.getExperience());
				engineerModels.add(detailsModel);
			}
		}
		return EngineerEquimentsModel.builder().id(entity.getId()).yesOrNo(entity.getYesOrNo())
				.userModel(userDetailsService.findById(entity.getEngineerId().getId())).status(entity.getStatus())
				.yesOrNo(entity.getYesOrNo()).engineerModelList(engineerModels).engineerId(entity.getEngineersId())
				.build();
	}

	public EngineerEquipmentModel convertToEngineerEquipmentModel(EngineerEquiments entity) {

		UserModel model = null;

		if (entity.getEngineerId() == null) {
			model = null;

		} else {
			model = userDetailsService.findById(entity.getEngineerId().getId());
		}

		return EngineerEquipmentModel.builder().id(entity.getId()).certified(entity.getCertified()).userModel(model)
				.yesOrNo(entity.getYesOrNo()).engineerId(entity.getEngineersId()).build();
	}

	public EngineerEquiments convertToEngineerEquipmentEntity(EngineerEquipmentModel model) {

		User user = null;
		if (model.getUserModel() == null) {
			user = null;

		} else {
			user = userDetailsService.findByIds(model.getUserModel().getId());
		}

		return EngineerEquiments.builder().id(model.getId()).certified(model.getCertified()).yesOrNo(model.getYesOrNo())
				.engineerId(user).engineersId(model.getEngineerId()).build();

	}

	private UserListResponseModel convertToModel(UserModel model) {
		return UserListResponseModel.builder().id(model.getId()).email(model.getEmail()).name(model.getName()).build();

	}

	public ExperienceModel convertToExperienceModel(Experience entity) {
		return ExperienceModel.builder().id(entity.getId()).experience(entity.getExperience()).build();
	}
}
