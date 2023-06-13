package com.athmarine.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.Equipment;
import com.athmarine.entity.Experience;
import com.athmarine.entity.MSTEquipmentCategory;
import com.athmarine.entity.MSTManufacturer;
import com.athmarine.entity.MSTPorts;
import com.athmarine.entity.RegistrationStatus;
import com.athmarine.entity.ServiceCategoryEquipmentMapping;
import com.athmarine.entity.User;
import com.athmarine.entity.VendorCompany;
import com.athmarine.entity.VendorServices;
import com.athmarine.exception.VendorCompanyNotFoundException;
import com.athmarine.repository.DemoServiceRepository;
import com.athmarine.repository.EquipmentRepository;
import com.athmarine.repository.ExperienceRepository;
import com.athmarine.repository.MSTEquipmentCategoryRepository;
import com.athmarine.repository.ServiceCategoryEquipmentRepository;
import com.athmarine.repository.VendorCompanyRepository;
import com.athmarine.request.EquipmentCategoryModel;
import com.athmarine.request.EquipmentCategoryRequestModel;
import com.athmarine.request.EquipmentModel;
import com.athmarine.request.EquipmentsModel;
import com.athmarine.request.ManufacturerModule;
import com.athmarine.request.MasterCountryRequestModel;
import com.athmarine.request.MasterPortModel;
import com.athmarine.request.ServiceDetailsModel;
import com.athmarine.request.VendorServiceDetailModel;
import com.athmarine.request.VendorServiceResponseModel;
import com.athmarine.resources.AppConstant;

@Service
public class VendorServicess {

	@Autowired
	DemoServiceRepository serviceRepository;

	@Autowired
	MasterEquipmentCategoryService masterEquipmentCategoryService;

	@Autowired
	ManufacturerService manufacturerService;

	@Autowired
	EquipmentService equipmentService;

	@Autowired
	MasterPortsService masterPortsService;

	@Autowired
	MasterStateService masterStateService;

	@Autowired
	MasterCountryService masterCountryService;
	@Autowired
	UserDetailsServiceImpl userDetailsServiceImpl;

	@Autowired
	DepartmentNameService departmentNameService;

	@Autowired
	VendorCompanyService vendorCompanyService;

	@Autowired
	public ExperienceRepository experienceRepository;

	@Autowired
	VendorCompanyRepository companyRepository;

	@Autowired
	MSTEquipmentCategoryRepository mstEquipmentCategoryRepository;

	@Autowired
	ServiceCategoryEquipmentRepository serviceCategoryEquipmentRepository;

	@Autowired
	EquipmentRepository equipmentRepository;

	public VendorServiceResponseModel createServices(VendorServiceDetailModel serviceModel) {

		if (serviceModel.getId() == null) {
			if (vendorCompanyService.checkAlreadyExists(serviceModel.getCompanyId())) {
				VendorServices findIdService = convertToEntity(serviceModel);
				serviceRepository.save(findIdService);
				List<Experience> experiences = new ArrayList<Experience>();
				for (int x = 0; x < serviceModel.getEquipmentCategory().size(); x++) {
					for (int j = 0; j < serviceModel.getEquipmentCategory().get(x).getEquipmentCategoryId()
							.getServiceDetailsModels().size(); j++) {

						ManufacturerModule manufacturerModule = serviceModel.getEquipmentCategory().get(x)
								.getEquipmentCategoryId().getServiceDetailsModels().get(j).getManufacturerModule();
						ManufacturerModule manufacturerModuleOther = null;
						if (manufacturerModule.getId() == 0) {
							manufacturerModuleOther = manufacturerService
									.createEquipmentManufacturer(manufacturerModule);
							manufacturerModuleOther = manufacturerService.findById(manufacturerModuleOther.getId());

						} else {
							manufacturerModuleOther = manufacturerModule;
						}

						ServiceCategoryEquipmentMapping serviceCategoryEquipmentMapping = new ServiceCategoryEquipmentMapping();
						EquipmentsModel equipmentsModel =serviceModel.getEquipmentCategory().get(x).getEquipmentCategoryId()
								.getServiceDetailsModels().get(j).getEquipmentModel();
						Equipment equipmentOther = null;
						if (equipmentsModel.getId() == 0) {
							Equipment equipmentObject = new Equipment();
							equipmentObject.setName(serviceModel.getEquipmentCategory().get(x).getEquipmentCategoryId()
									.getServiceDetailsModels().get(j).getEquipmentModel().getName());
							equipmentObject.setIsVerified("0");
							equipmentObject.setStatus(0);
							equipmentOther = equipmentRepository.save(equipmentObject);

						} else {
							equipmentOther = equipmentService
									.findByIds(equipmentsModel.getId());
						}
						Experience experience = new Experience();
						experience.setEnggequip(null);

						experience.setExperience(serviceModel.getEquipmentCategory().get(x).getEquipmentCategoryId()
								.getServiceDetailsModels().get(j).getEquipmentExperience());
						experience.setEquipment(equipmentOther);
						experience.setVendor_services(findIdService);
						experiences.add(experience);
						serviceCategoryEquipmentMapping.setVendorServices(findIdService);
						serviceCategoryEquipmentMapping.setEquipment(equipmentOther);
						serviceCategoryEquipmentMapping
								.setMstManufacturer(convertToMSTManufacturerModel(manufacturerModule));

						Optional<MSTEquipmentCategory> mstEquipmentCategory = mstEquipmentCategoryRepository
								.findById(serviceModel.getEquipmentCategory().get(x).getEquipmentCategoryId().getId());
						if (mstEquipmentCategory.isPresent()) {
							serviceCategoryEquipmentMapping.setMstEquipmentCategory(mstEquipmentCategory.get());
							serviceCategoryEquipmentMapping.setVendorServices(findIdService);
							serviceCategoryEquipmentMapping.setEquipment(equipmentOther);
							serviceCategoryEquipmentMapping
									.setMstManufacturer(convertToMSTManufacturerModel(manufacturerModuleOther));
						}

						serviceCategoryEquipmentRepository.save(serviceCategoryEquipmentMapping);
					}
				}

				experienceRepository.saveAll(experiences);
				findIdService.setExperience(experiences);

				VendorCompany vendorCompany = vendorCompanyService.findByIds(serviceModel.getCompanyId());
				vendorCompany.setRegistrationStatus(RegistrationStatus.Service);
				companyRepository.save(vendorCompany);
				return convertToResponseModel(findIdService);
			} else {
				throw new VendorCompanyNotFoundException(AppConstant.ErrorTypes.VENDOR_COMPANY_NOT_EXIST_ERROR,
						AppConstant.ErrorCodes.VENDOR_COMPANY_EXIST_ERROR_CODE,
						AppConstant.ErrorMessages.VENDOR_COMPANY_NOT_EXIST_MESSAGE);
			}

		} else {
			return convertToResponseModel(findVendorServiceById(updateVendorService(serviceModel).getId()));
		}

	}

	public VendorServiceDetailModel updateVendorService(VendorServiceDetailModel updateVendorService) {

		VendorServices vendorServiceDetailModel = findVendorServiceById(updateVendorService.getId());
		updateVendorService.setId(vendorServiceDetailModel.getId());
		vendorServiceDetailModel.setVendorServiceKey(updateVendorService.getVendorServiceKey());

		return convertToModelUpdate(serviceRepository.save(vendorServiceDetailModel));

	}

	public VendorServices findVendorServiceById(Integer id) {

		if (id == null || id == 0) {
			throw new VendorCompanyNotFoundException(AppConstant.ErrorTypes.VENDOR_COMPANY_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.VENDOR_COMPANY_EXIST_ERROR_CODE,
					AppConstant.ErrorMessages.VENDOR_COMPANY_NOT_EXIST_MESSAGE);
		} else {
			return serviceRepository.findById(id)
					.orElseThrow(() -> new VendorCompanyNotFoundException(
							AppConstant.ErrorTypes.VENDOR_COMPANY_NOT_EXIST_ERROR,
							AppConstant.ErrorCodes.VENDOR_COMPANY_EXIST_ERROR_CODE,
							AppConstant.ErrorMessages.VENDOR_SERVICE_NOT_EXIST_MESSAGE));
		}

	}

	public List<VendorServiceDetailModel> getVendorServiceByCompanyId(Integer companyId) {

		VendorCompany vendorCompnay = vendorCompanyService.findByIds(companyId);

		User company = userDetailsServiceImpl.findByIds(vendorCompnay.getId());

		List<VendorServices> serviceList = serviceRepository.findAllByCompanyId(company);
		return serviceList.stream().map(serviceLytytututuist -> convertToModelNew(serviceLytytututuist))
				.collect(Collectors.toList());
	}

	private VendorServices convertToEntity(VendorServiceDetailModel model) {

		MasterCountryRequestModel masterCountryRequestModel = model.getCountry();
		List<MSTPorts> portList = null;
		if (masterCountryRequestModel.getPortId() == null) {
			portList = null;
		} else {
			List<MSTPorts> portLists = new ArrayList<>();
			for (MasterPortModel portId : masterCountryRequestModel.getPortId()) {
				MSTPorts ports = null;
				if (portId.getId() == 0) {
					ports = masterPortsService.createMasterOtherPort(portId);
					ports = masterPortsService.findByIds(ports.getId());
				} else {
					ports = masterPortsService.findByIds(portId.getId());
				}
				portLists.add(ports);
			}
			portList = portLists;
//			portList = masterCountryRequestModel.getPortId().stream()
//					.map(port -> masterPortsService.findByIds(port.getId())).collect(Collectors.toList());
		}

		List<EquipmentCategoryRequestModel> listOfEquipmentCategoryModels = new ArrayList<>();
		for (int i = 0; i < model.getEquipmentCategory().size(); i++) {
			EquipmentCategoryRequestModel listOfCategoryModels = model.getEquipmentCategory().get(i)
					.getEquipmentCategoryId();
			listOfEquipmentCategoryModels.add(listOfCategoryModels);
		}
		List<MSTEquipmentCategory> category = listOfEquipmentCategoryModels.stream()
				.map(details -> masterEquipmentCategoryService.findByIds(details.getId())).collect(Collectors.toList());

		List<ServiceDetailsModel> detailsList = new ArrayList<ServiceDetailsModel>();
		for (int i = 0; i < listOfEquipmentCategoryModels.size(); i++) {
			List<ServiceDetailsModel> detailsModels = listOfEquipmentCategoryModels.get(i).getServiceDetailsModels();
			detailsList.addAll(detailsModels);
		}

		List<Equipment> equipmentList = new ArrayList<>(); 
		for (ServiceDetailsModel serviceDetailsModel : detailsList) {
			if (serviceDetailsModel.getEquipmentModel().getId() == 0) {
				EquipmentsModel manufacturerModule = equipmentService
						.createEquipment(serviceDetailsModel.getEquipmentModel());
				equipmentList.add(equipmentService.findByIds(manufacturerModule.getId()));
			} else {
				equipmentList.add(equipmentService.convertToModelEquimentEntity(serviceDetailsModel.getEquipmentModel()));
			}

		}
		List<MSTManufacturer> manufacturerList = new ArrayList<>();
		for (ServiceDetailsModel serviceDetailsModel : detailsList) {
			if (serviceDetailsModel.getManufacturerModule().getId() == 0) {
				ManufacturerModule manufacturerModule = manufacturerService
						.createEquipmentManufacturer(serviceDetailsModel.getManufacturerModule());
				manufacturerList.add(manufacturerService.findByIds(manufacturerModule.getId()));
			} else {
				manufacturerList.add(manufacturerService.convertToEntity(serviceDetailsModel.getManufacturerModule()));
			}

		}

		return VendorServices.builder().id(model.getId()).categoryId(category).portId(portList)
				.equipmentId(equipmentList).manufacturerId(manufacturerList)
				.vendorServiceKey(model.getVendorServiceKey())
				.countryIds(masterCountryService.findByCountryId(masterCountryRequestModel.getId()))
				.companyId(userDetailsServiceImpl.findByIds(model.getCompanyId())).build();

	}

	@SuppressWarnings("unused")
	private VendorServiceDetailModel convertToModel(VendorServices entity) {
		convertToModelNew(entity);

		List<MasterPortModel> portList = null;
		MasterCountryRequestModel countryRequestModel = masterCountryService
				.getVendorServiceByCountryId(entity.getCountryIds().getId());

		if (entity.getPortId() == null) {
			portList = null;
		} else {
			portList = entity.getPortId().stream().map(port -> masterPortsService.convertToModels(port))
					.collect(Collectors.toList());
		}
		countryRequestModel.setPortId(portList);

		List<EquipmentModel> equipmentModels = entity.getEquipmentId().stream()
				.map(equip -> equipmentService.convertToModel(equip)).collect(Collectors.toList());

		List<Experience> expList = entity.getExperience();

		List<MSTManufacturer> manufacturerList = entity.getManufacturerId().stream()
				.map(manuf -> manufacturerService.findByIds(manuf.getId())).collect(Collectors.toList());

		List<EquipmentCategoryRequestModel> listOfEquipmentCategoryModels = entity.getCategoryId().stream()
				.map(category -> masterEquipmentCategoryService.convertToVendorServiceEquipmentCategoryModel(category))
				.collect(Collectors.toList());

		for (EquipmentCategoryRequestModel list : listOfEquipmentCategoryModels) {
			List<ServiceDetailsModel> serviceDetailsModels = new ArrayList<>();

			List<EquipmentsModel> equipmentList = equipmentModels.stream().map(eq -> convertToEquipmentsModel(eq))
					.collect(Collectors.toList());

			for (int j = 0; j < equipmentList.size(); j++) {
				Equipment equipment = equipmentService.findByIds(equipmentList.get(j).getId());

				ServiceDetailsModel detailsModel = new ServiceDetailsModel();
				Experience exp = null;
				for (Experience experience : expList) {
					if (experience.getEquipment().equals(equipment)) {
						exp = experience;
					}
				}

				MSTManufacturer manufacturer = new MSTManufacturer();
				manufacturer = manufacturerList.stream()
						.filter(m -> m.getEquipment().stream().anyMatch(e -> e.equals(equipment))).findAny().get();

				detailsModel.setEquipmentModel(equipmentList.get(j));

				detailsModel.setEquipmentExperience(exp.getExperience());

				detailsModel.setManufacturerModule(manufacturerService.convertToModel(manufacturer));

				manufacturerList.remove(manufacturer);

				serviceDetailsModels.add(detailsModel);

			}
			list.setServiceDetailsModels(serviceDetailsModels);

		}
		List<EquipmentCategoryModel> equipmentCategoryModels = listOfEquipmentCategoryModels.stream()
				.map(equipmentCategoytytyryModels -> convertToEquipmentCategoryModel(equipmentCategoytytyryModels))
				.collect(Collectors.toList());

		VendorServiceDetailModel vendorServiceDetailModel = VendorServiceDetailModel.builder().id(entity.getId())
				.companyId(entity.getCompanyId().getId()).status(entity.getStatus()).country(countryRequestModel)
				.equipmentCategory(equipmentCategoryModels).vendorServiceKey(entity.isVendorServiceKey()).build();

		return vendorServiceDetailModel;
	}

	public EquipmentsModel convertToEquipmentsModel(EquipmentModel model) {
		return EquipmentsModel.builder().id(model.getId()).code(model.getCode()).isVerified(model.getIsVerified())
				.name(model.getName()).status(model.getStatus()).build();
	}

	public EquipmentCategoryModel convertToEquipmentCategoryModel(
			EquipmentCategoryRequestModel equipmentCategoryRequestModel) {

		return EquipmentCategoryModel.builder().equipmentCategoryId(equipmentCategoryRequestModel).build();
	}

	private VendorServiceDetailModel convertToModelNew(VendorServices entity) {
		List<MasterPortModel> portList = null;
		MasterCountryRequestModel countryRequestModel = masterCountryService
				.getVendorServiceByCountryId(entity.getCountryIds().getId());

		if (entity.getPortId() == null) {
			portList = null;
		} else {
			portList = entity.getPortId().stream().map(port -> masterPortsService.convertToModels(port))
					.collect(Collectors.toList());
		}
		countryRequestModel.setPortId(portList);

		List<EquipmentCategoryRequestModel> listOfEquipmentCategoryModels = entity.getCategoryId().stream()
				.map(category -> masterEquipmentCategoryService.convertToVendorServiceEquipmentCategoryModel(category))
				.collect(Collectors.toList());

		List<Experience> expList = entity.getExperience();

		@SuppressWarnings("unused")
		List<ServiceCategoryEquipmentMapping> serviceCategoryEquipmentMappings = serviceCategoryEquipmentRepository
				.findByService(entity.getId());

		for (int i = 0; i < listOfEquipmentCategoryModels.size(); i++) {
			List<ServiceDetailsModel> serviceDetailsModels = new ArrayList<>();

			List<ServiceCategoryEquipmentMapping> serviceCategoryEquipmentMappingList = serviceCategoryEquipmentRepository
					.findByServiceAndCategory(entity.getId(), listOfEquipmentCategoryModels.get(i).getId());

			for (int j = 0; j < serviceCategoryEquipmentMappingList.size(); j++) {

				ServiceDetailsModel detailsModel = new ServiceDetailsModel();

				if (serviceCategoryEquipmentMappingList.size() > 0) {
					Equipment equipment = serviceCategoryEquipmentMappingList.get(j).getEquipment();

					Experience exp = expList.stream().filter(e -> e.getEquipment().equals(equipment)).findAny().get();

					MSTManufacturer mstManufacturer = serviceCategoryEquipmentMappingList.get(j).getMstManufacturer();

					detailsModel.setEquipmentModel(convertToEquipmentModel(equipment));

					detailsModel.setEquipmentExperience(exp.getExperience());

					detailsModel.setManufacturerModule(convertToManufacturerModel(mstManufacturer));

					serviceDetailsModels.add(detailsModel);

				}
			}

			listOfEquipmentCategoryModels.get(i).setServiceDetailsModels(serviceDetailsModels);

		}
		List<EquipmentCategoryModel> equipmentCategoryModels = listOfEquipmentCategoryModels.stream()
				.map(equipmentCategoytytyryModels -> convertToEquipmentCategoryModel(equipmentCategoytytyryModels))
				.collect(Collectors.toList());

		VendorServiceDetailModel vendorServiceDetailModel = VendorServiceDetailModel.builder().id(entity.getId())
				.companyId(entity.getCompanyId().getId()).status(entity.getStatus()).country(countryRequestModel)
				.equipmentCategory(equipmentCategoryModels).vendorServiceKey(entity.isVendorServiceKey()).build();

		return vendorServiceDetailModel;

	}

	public EquipmentsModel convertToEquipmentModel(Equipment model) {
		return EquipmentsModel.builder().id(model.getId()).code(model.getCode()).isVerified(model.getIsVerified())
				.name(model.getName()).status(model.getStatus()).build();
	}

	public ManufacturerModule convertToManufacturerModel(MSTManufacturer model) {
		return ManufacturerModule.builder().id(model.getId()).name(model.getName()).status(model.getStatus()).build();
	}

	public MSTManufacturer convertToMSTManufacturerModel(ManufacturerModule model) {
		return MSTManufacturer.builder().id(model.getId()).name(model.getName()).status(model.getStatus()).build();
	}

	private VendorServiceDetailModel convertToModelUpdate(VendorServices entity) {
		return VendorServiceDetailModel.builder().id(entity.getId()).vendorServiceKey(entity.isVendorServiceKey())
				.build();
	}

	public VendorServiceResponseModel convertToResponseModel(VendorServices entity) {
		return VendorServiceResponseModel.builder().id(entity.getId()).status(entity.getStatus())
				.vendorServiceKey(entity.isVendorServiceKey()).companyId(entity.getCompanyId().getId()).build();

	}
}