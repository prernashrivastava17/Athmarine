package com.athmarine.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.MSTEquipmentCategory;
import com.athmarine.exception.MasterEquipmentCategoryNotFoundException;
import com.athmarine.repository.MSTEquipmentCategoryRepository;
import com.athmarine.request.EquipmentCategoryRequestModel;
import com.athmarine.request.MasterEquipmentCategoryModel;
import com.athmarine.request.MasterEquipmentsCategoryModel;
import com.athmarine.resources.AppConstant;

@Service
public class MasterEquipmentCategoryService {

	@Autowired
	MSTEquipmentCategoryRepository masterEquipmentCategoryRepository;

	public MSTEquipmentCategory saveMasterEquipmentCategoryDetail(
			@Valid MasterEquipmentCategoryModel masterEquipmentCategoryModel) {
		return masterEquipmentCategoryRepository.save(convertToEntity(masterEquipmentCategoryModel));
	}

	public MasterEquipmentCategoryModel saveMasterEquipmentCategoryDetails(
			@Valid MasterEquipmentCategoryModel masterEquipmentCategoryModel) {
		return convertToModel(masterEquipmentCategoryRepository.save(convertToEntity(masterEquipmentCategoryModel)));
	}

	public MasterEquipmentCategoryModel getMasterEquipmentCategoryData(int id) {
		return convertToModel(masterEquipmentCategoryRepository.findById(id)
				.orElseThrow(() -> new MasterEquipmentCategoryNotFoundException(
						AppConstant.ErrorTypes.MASTER_EQUIMENT_CATEGORY_EXIST_ERROR,
						AppConstant.ErrorCodes.MASTER_EQUIMENT_CATEGORY_ERROR_CODE,
						AppConstant.ErrorMessages.MASTER_EQUIMENT_CATEGORY_NOT_EXTIS_MESSAGE)));
	}

	public List<MasterEquipmentsCategoryModel> getAllMasterEquipmentCategory() {

		return masterEquipmentCategoryRepository.findAll().stream().filter(entity -> entity.isVerified())
				.map(equipmentCategory -> convertToModels(equipmentCategory)).collect(Collectors.toList());

	}

	public List<MasterEquipmentsCategoryModel> getAllEquipmentCategoryNonVerified() {

		return masterEquipmentCategoryRepository.findAll().stream().filter(entity -> !entity.isVerified())
				.map(equipmentCategory -> convertToModels(equipmentCategory)).collect(Collectors.toList());
	}

	public MasterEquipmentCategoryModel updateMasterEquipmentCategory(
			MasterEquipmentCategoryModel masterEquipmentCategoryModel) {

		MasterEquipmentCategoryModel masterEquipmentCategory = findById(masterEquipmentCategoryModel.getId());
		masterEquipmentCategoryModel.setId(masterEquipmentCategory.getId());

		return convertToModel(masterEquipmentCategoryRepository.save(convertToEntity(masterEquipmentCategoryModel)));
	}

	public MasterEquipmentCategoryModel findById(Integer id) {

		if (id == null) {
			throw new MasterEquipmentCategoryNotFoundException(
					AppConstant.ErrorTypes.MASTER_EQUIMENT_CATEGORY_EXIST_ERROR,
					AppConstant.ErrorCodes.MASTER_EQUIMENT_CATEGORY_ERROR_CODE,
					AppConstant.ErrorMessages.ID_EMPTY_MESSAGE);
		} else {
			return convertToModel(masterEquipmentCategoryRepository.findById(id)
					.orElseThrow(() -> new MasterEquipmentCategoryNotFoundException(
							AppConstant.ErrorTypes.MASTER_EQUIMENT_CATEGORY_EXIST_ERROR,
							AppConstant.ErrorCodes.MASTER_EQUIMENT_CATEGORY_ERROR_CODE,
							AppConstant.ErrorMessages.MASTER_EQUIMENT_CATEGORY_NOT_EXTIS_MESSAGE)));
		}

	}

	public MasterEquipmentCategoryModel deleteEquipmentCategoryDetails(@NotBlank int id) {

		MasterEquipmentCategoryModel equipmentCategory = findById(id);

		equipmentCategory.setStatus(AppConstant.DeleteStatus.STATUS_DELETE);

		return convertToModel(masterEquipmentCategoryRepository.save(convertToEntity(equipmentCategory)));
	}

	public MSTEquipmentCategory findByIds(Integer id) {

		if (id == null) {

			throw new MasterEquipmentCategoryNotFoundException(
					AppConstant.ErrorTypes.MASTER_EQUIMENT_CATEGORY_EXIST_ERROR,
					AppConstant.ErrorCodes.MASTER_EQUIMENT_CATEGORY_ERROR_CODE,
					AppConstant.ErrorMessages.MASTER_EQUIMENT_CATEGORY_NOT_EXTIS_MESSAGE);

		}
		return masterEquipmentCategoryRepository.findById(id)
				.orElseThrow(() -> new MasterEquipmentCategoryNotFoundException(
						AppConstant.ErrorTypes.MASTER_EQUIMENT_CATEGORY_EXIST_ERROR,
						AppConstant.ErrorCodes.MASTER_EQUIMENT_CATEGORY_ERROR_CODE,
						AppConstant.ErrorMessages.MASTER_EQUIMENT_CATEGORY_NOT_EXTIS_MESSAGE));

	}

	public MSTEquipmentCategory convertToEntity(MasterEquipmentCategoryModel model) {

		return MSTEquipmentCategory.builder().id(model.getId()).name(model.getName()).shortName(model.getShortName())
				.status(model.getStatus()).isVerified(false).build();
	}

	public MasterEquipmentCategoryModel convertToModel(MSTEquipmentCategory entity) {

		return MasterEquipmentCategoryModel.builder().id(entity.getId()).name(entity.getName())
				.shortName(entity.getShortName()).status(entity.getStatus()).build();

	}

	public EquipmentCategoryRequestModel convertToVendorServiceEquipmentCategoryModel(MSTEquipmentCategory entity) {

		return EquipmentCategoryRequestModel.builder().id(entity.getId()).name(entity.getName())
				.shortName(entity.getShortName()).status(entity.getStatus()).build();

	}

	public MasterEquipmentsCategoryModel convertToModels(MSTEquipmentCategory entity) {

		return MasterEquipmentsCategoryModel.builder().id(entity.getId()).name(entity.getName())
				.shortName(entity.getShortName()).status(entity.getStatus()).isVerified(entity.isVerified()).build();

	}

}
