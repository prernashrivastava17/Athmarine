package com.athmarine.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.AvailableOn;
import com.athmarine.exception.ResourceAlreadyExistsException;
import com.athmarine.exception.ResourceNotFoundException;
import com.athmarine.repository.AvailableOnRepository;
import com.athmarine.request.AvailableOnModel;
import com.athmarine.resources.AppConstant;

@Service
public class AvailableOnService {

	@Autowired
	AvailableOnRepository availableOnRepository;

	public AvailableOnModel createAvailableOn(AvailableOnModel model) {
		if (!availableOnRepository.existsByAppName(model.getApp_Name())) {
			return convertToModel(availableOnRepository.save(convertToEntity(model)));
		} else {
			throw new ResourceAlreadyExistsException(AppConstant.ErrorTypes.RESOURCE_EXIST_ERROR,
					AppConstant.ErrorCodes.RESOURCE_EXIST_ERROR_CODE, AppConstant.ErrorMessages.RESOURCE_EXIST_MESSAGE);
		}
	}

	public List<AvailableOnModel> getAllAvailableOn() {
		return availableOnRepository.findAll().stream().map(availableOn -> convertToModel(availableOn))
				.collect(Collectors.toList());
	}

	public AvailableOn findByIds(Integer id) {

		if (id == null) {
			throw new ResourceNotFoundException(AppConstant.ErrorTypes.ID_NULL_EXIST_ERROR,
					AppConstant.ErrorCodes.ID_NULL_ERROR_CODE,
					"Available " + AppConstant.ErrorMessages.ID_NULL_NOT_EXIST);
		} else {
			return availableOnRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException(AppConstant.ErrorTypes.AVAILABLE_ON_ERROR,
							AppConstant.ErrorCodes.AVAILABLE_ON_ERROR_CODE,
							AppConstant.ErrorMessages.AVAILABLE_ON_EXIST_ERROR));
		}
	}

	public AvailableOnModel findById(Integer id) {

		if (id == null) {
			throw new ResourceNotFoundException(AppConstant.ErrorTypes.RESOURCE_NOT_FOUND_ERROR,
					AppConstant.ErrorCodes.RESOURCE_NOT_FOUND, "Role " + AppConstant.ErrorMessages.ID_EMPTY_MESSAGE);
		} else {
			return convertToModel(availableOnRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException(AppConstant.ErrorTypes.RESOURCE_NOT_FOUND_ERROR,
							AppConstant.ErrorCodes.RESOURCE_NOT_FOUND,
							AppConstant.ErrorMessages.RESOURCE_NOT_FOUND_MESSAGE)));
		}
	}

	public AvailableOn convertToEntity(AvailableOnModel model) {

		return AvailableOn.builder().id(model.getId()).appName(model.getApp_Name()).status(model.getStatus()).build();

	}

	public AvailableOnModel convertToModel(AvailableOn entity) {

		return AvailableOnModel.builder().id(entity.getId()).status(entity.getStatus()).app_Name(entity.getAppName())
				.build();
	}
}
