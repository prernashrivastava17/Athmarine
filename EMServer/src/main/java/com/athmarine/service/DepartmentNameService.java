package com.athmarine.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.DepartmentName;
import com.athmarine.exception.ResourceAlreadyExistsException;
import com.athmarine.exception.ResourceNotFoundException;
import com.athmarine.repository.DepartmentNameRepositroy;
import com.athmarine.request.DepartmentNameModel;
import com.athmarine.resources.AppConstant;

@Service
public class DepartmentNameService {

	@Autowired
	DepartmentNameRepositroy departmentNameRepositroy;

	public DepartmentNameModel createDepartmentName(DepartmentNameModel model) {
		if (!departmentNameRepositroy.existsByDepartmentName(model.getDepartmentName())) {
			return convertToModel(departmentNameRepositroy.save(convertToEntity(model)));
		} else {
			throw new ResourceAlreadyExistsException(AppConstant.ErrorTypes.RESOURCE_EXIST_ERROR,
					AppConstant.ErrorCodes.RESOURCE_EXIST_ERROR_CODE, AppConstant.ErrorMessages.RESOURCE_EXIST_MESSAGE);
		}
	}

	public DepartmentNameModel verifyDepartmentName(DepartmentNameModel model) {

		DepartmentNameModel model2 = findById(model.getId());
		model.setId(model2.getId());
		return convertToModel(departmentNameRepositroy.save(convertToEntity(model)));

	}

	public List<DepartmentNameModel> getAllVerifiedDeprtmentName() {
		return departmentNameRepositroy.findAll().stream().filter(d -> d.isVerified()).map(dep -> convertToModel(dep))
				.collect(Collectors.toList());
	}

	public DepartmentName findByIds(Integer id) {

		if (id == null) {
			throw new ResourceNotFoundException(AppConstant.ErrorTypes.RESOURCE_NOT_FOUND_ERROR,
					AppConstant.ErrorCodes.RESOURCE_NOT_FOUND, "Role " + AppConstant.ErrorMessages.ID_EMPTY_MESSAGE);
		} else {
			return departmentNameRepositroy.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException(AppConstant.ErrorTypes.RESOURCE_NOT_FOUND_ERROR,
							AppConstant.ErrorCodes.RESOURCE_NOT_FOUND,
							AppConstant.ErrorMessages.RESOURCE_NOT_FOUND_MESSAGE));
		}
	}

	public DepartmentNameModel findById(Integer id) {

		if (id == null) {
			throw new ResourceNotFoundException(AppConstant.ErrorTypes.RESOURCE_NOT_FOUND_ERROR,
					AppConstant.ErrorCodes.RESOURCE_NOT_FOUND, "Role " + AppConstant.ErrorMessages.ID_EMPTY_MESSAGE);
		} else {
			return convertToModel(departmentNameRepositroy.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException(AppConstant.ErrorTypes.RESOURCE_NOT_FOUND_ERROR,
							AppConstant.ErrorCodes.RESOURCE_NOT_FOUND,
							AppConstant.ErrorMessages.RESOURCE_NOT_FOUND_MESSAGE)));
		}
	}

	public DepartmentName convertToEntity(DepartmentNameModel model) {

		return DepartmentName.builder().id(model.getId()).departmentName(model.getDepartmentName())
				.status(model.getStatus())
				.verified(model.isVerified()).build();

	}

	public DepartmentNameModel convertToModel(DepartmentName entity) {

		return DepartmentNameModel.builder().id(entity.getId())
				.status(entity.getStatus())
				.departmentName(entity.getDepartmentName()).verified(entity.isVerified()).build();
	}
}
