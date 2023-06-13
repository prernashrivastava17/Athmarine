package com.athmarine.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.Equipment;
import com.athmarine.entity.MSTEquipmentCategory;
import com.athmarine.entity.MSTManufacturer;
import com.athmarine.exception.EquipmentNotFoundException;
import com.athmarine.repository.EquipmentRepository;
import com.athmarine.request.AllEquipmentModel;
import com.athmarine.request.EquipmentModel;
import com.athmarine.request.EquipmentModelManufacture;
import com.athmarine.request.EquipmentsModel;
import com.athmarine.request.ManufacturerModule;
import com.athmarine.resources.AppConstant;

@Service
public class EquipmentService {

	@Autowired
	EquipmentRepository equipmentRepository;

	@Autowired
	ManufacturerService manufacturerService;

	@Autowired
	MasterEquipmentCategoryService masterEquipmentCategoryService;

	public EquipmentModel createEquipmentName(EquipmentModel equipmentModel) {

		return convertToModel(equipmentRepository.save(convertToEntity(equipmentModel)));
	}

	public EquipmentsModel createEquipment(EquipmentsModel equipmentModel) {

		return convertToModelEquiment(equipmentRepository.save(convertToModelEquimentEntity(equipmentModel)));
	}
	public EquipmentModel findEquipmentNameById(@NotBlank int id) {

		return convertToModel(equipmentRepository.findById(id)
				.orElseThrow(() -> new EquipmentNotFoundException(AppConstant.ErrorTypes.EQUIPMENT_EXIST_ERROR,
						AppConstant.ErrorCodes.EQUIPMENT_ERROR_CODE, AppConstant.ErrorMessages.EQUIPMENT_NOT_EXIST)));

	}

	public List<AllEquipmentModel> getAllEquimentName() {

		AllEquipmentModel otherEquipmentModel = new AllEquipmentModel();
		otherEquipmentModel.setName("Others");
		otherEquipmentModel.setIsVerified("1");

		List<AllEquipmentModel> allEquipmentModel = equipmentRepository.findAll().stream()
				.sorted(Comparator.comparing(Equipment::getName)).map(continents -> convertToModels(continents))
				.collect(Collectors.toList());
		allEquipmentModel.add(otherEquipmentModel);

		return allEquipmentModel;

	}

	public EquipmentModel updateEquipment(EquipmentModel equipmentModel) {

		EquipmentModel equipment = findById(equipmentModel.getId());

		equipmentModel.setId(equipment.getId());

		return convertToModel(equipmentRepository.save(convertToEntity(equipmentModel)));
	}

	public EquipmentModel findById(int id) {
		return convertToModel(equipmentRepository.findById(id)
				.orElseThrow(() -> new EquipmentNotFoundException(AppConstant.ErrorTypes.EQUIPMENT_EXIST_ERROR,
						AppConstant.ErrorCodes.EQUIPMENT_ERROR_CODE, AppConstant.ErrorMessages.EQUIPMENT_NOT_EXIST)));

	}

	public EquipmentsModel findByEquimentId(int id) {
		return convertToModelEquiment(equipmentRepository.findById(id)
				.orElseThrow(() -> new EquipmentNotFoundException(AppConstant.ErrorTypes.EQUIPMENT_EXIST_ERROR,
						AppConstant.ErrorCodes.EQUIPMENT_ERROR_CODE, AppConstant.ErrorMessages.EQUIPMENT_NOT_EXIST)));

	}

	public Equipment findByIds(Integer id) {

		if (id == null) {

			throw new EquipmentNotFoundException(AppConstant.ErrorTypes.EQUIPMENT_EXIST_ERROR,
					AppConstant.ErrorCodes.EQUIPMENT_ERROR_CODE, AppConstant.ErrorMessages.EQUIPMENT_NOT_EXIST);

		}
		return equipmentRepository.findById(id)
				.orElseThrow(() -> new EquipmentNotFoundException(AppConstant.ErrorTypes.EQUIPMENT_EXIST_ERROR,
						AppConstant.ErrorCodes.EQUIPMENT_ERROR_CODE, AppConstant.ErrorMessages.EQUIPMENT_NOT_EXIST));
	}

	public List<EquipmentModel> getAllEquipmentByCategory(Integer id) {
		MSTEquipmentCategory mstCategory = masterEquipmentCategoryService.findByIds(id);
		
		List<EquipmentModel> equipmentModel =mstCategory.getEquipment().stream().sorted(Comparator.comparing(Equipment::getName))
				.filter(verified -> verified.getIsVerified().equals("1")).map(equipment -> convertToModel(equipment))
				.collect(Collectors.toList());
		EquipmentModel otherEquipmentModel = new EquipmentModel();
		otherEquipmentModel.setName("Others");
		//otherEquipmentModel.setIsVerified("1");
		equipmentModel.add(otherEquipmentModel);

		return equipmentModel;

	}

	public EquipmentModel deleteEquipmentName(@NotBlank int id) {

		EquipmentModel equipment = findById(id);

		equipment.setStatus(AppConstant.DeleteStatus.STATUS_DELETE);

		return convertToModel(equipmentRepository.save(convertToEntity(equipment)));
	}

	public Equipment convertToEntity(EquipmentModel model) {
		Integer catId = model.getMasterEquipmentCategoryModel().getId();

		List<MSTManufacturer> manufacturerList = model.getManufacturerModule().stream()
				.map(m -> manufacturerService.convertToEntity(m)).collect(Collectors.toList());
		if (model.getId() == null) {
			return equipmentRepository.save(convertToEntity1(model));
		} else {
			return Equipment.builder().id(model.getId()).name(model.getName()).code(model.getCode())
					.isVerified(model.getIsVerified()).status(model.getStatus())

					.category(catId == 0
							? masterEquipmentCategoryService
									.saveMasterEquipmentCategoryDetail(model.getMasterEquipmentCategoryModel())
							: (masterEquipmentCategoryService
									.findByIds(model.getMasterEquipmentCategoryModel().getId())))

					.manufacturerId(manufacturerList).build();
		}

	}

	public Equipment convertToEntity1(EquipmentModel model) {
		Integer catId = model.getMasterEquipmentCategoryModel().getId();

		List<MSTManufacturer> manufacturerList = model.getManufacturerModule().stream()
				.map(m -> manufacturerService.convertToEntity(m)).collect(Collectors.toList());

		return Equipment.builder().id(model.getId()).name(model.getName()).code(model.getCode())
				.isVerified(model.getIsVerified()).status(model.getStatus())

				.category(catId == 0
						? masterEquipmentCategoryService
								.saveMasterEquipmentCategoryDetail(model.getMasterEquipmentCategoryModel())
						: (masterEquipmentCategoryService.findByIds(model.getMasterEquipmentCategoryModel().getId())))

				.manufacturerId(manufacturerList).build();

	}

	public Equipment convertToEntityManufacture(EquipmentModelManufacture model) {

		List<MSTManufacturer> manufacturerList = model.getManufacturerModule().stream()
				.map(m -> manufacturerService.findByIds(m.getId())).collect(Collectors.toList());

		return Equipment.builder().id(model.getId()).name(model.getName()).code(model.getCode())
				.isVerified(model.getIsVerified()).status(model.getStatus()).manufacturerId(manufacturerList).build();

	}

	public EquipmentModel convertToModel(Equipment entity) {

		List<ManufacturerModule> manufacturerList = entity.getManufacturerId().stream()
				.sorted(Comparator.comparing(MSTManufacturer::getName)).map(m -> manufacturerService.convertToModel(m))
				.collect(Collectors.toList());

		return EquipmentModel.builder().id(entity.getId()).name(entity.getName()).code(entity.getCode())
				.isVerified(entity.getIsVerified()).status(entity.getStatus())
				.masterEquipmentCategoryModel(masterEquipmentCategoryService.findById(entity.getCategory().getId()))
				.manufacturerModule(manufacturerList).build();
	}

	public EquipmentModelManufacture convertToModelManufacrure(Equipment entity) {

		List<ManufacturerModule> manufacturerList = entity.getManufacturerId().stream()
				.map(m -> manufacturerService.convertToModel(m)).collect(Collectors.toList());

		return EquipmentModelManufacture.builder().id(entity.getId()).name(entity.getName()).code(entity.getCode())
				.isVerified(entity.getIsVerified()).status(entity.getStatus()).manufacturerModule(manufacturerList)
				.build();
	}

	private AllEquipmentModel convertToModels(Equipment entity) {

		return AllEquipmentModel.builder().id(entity.getId()).name(entity.getName()).code(entity.getCode())
				.isVerified(entity.getIsVerified()).status(entity.getStatus()).build();
	}

	private EquipmentsModel convertToModelEquiment(Equipment entity) {

		return EquipmentsModel.builder().id(entity.getId()).name(entity.getName()).code(entity.getCode())
				.isVerified(entity.getIsVerified()).status(entity.getStatus()).build();
	}
	
	public Equipment convertToModelEquimentEntity(EquipmentsModel entity) {

		return Equipment.builder().id(entity.getId()).name(entity.getName()).code(entity.getCode())
				.isVerified(entity.getIsVerified()).status(entity.getStatus()).build();
	}

}
