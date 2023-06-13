package com.athmarine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.Module;
import com.athmarine.exception.ModuleNotFoundException;
import com.athmarine.repository.ModuleRepository;
import com.athmarine.request.ModuleModel;
import com.athmarine.resources.AppConstant;

@Service
public class ModuleService {

	@Autowired
	ModuleRepository moduleRepository;

	public ModuleModel createModule(ModuleModel modulemodel) {

		return convertToModel(moduleRepository.save(convertToEntity(modulemodel)));
	}

	public ModuleModel findById(int id) {
		return convertToModel(moduleRepository.findById(id)
				.orElseThrow(() -> new ModuleNotFoundException(AppConstant.ErrorTypes.MODULE_NOT_EXIST_ERROR,
						AppConstant.ErrorCodes.MODULE_EXIST_ERROR_CODE,
						AppConstant.ErrorMessages.MODULE_NOT_EXIST_MESSAGE)));

	}

	public ModuleModel updateModuleData(ModuleModel moduleModel) {
		ModuleModel model =findById(moduleModel.getId());
		moduleModel.setId(model.getId());
		return convertToModel(moduleRepository.save(convertToEntity(moduleModel)));

	}

	public ModuleModel deleteModule(Integer id) {

		ModuleModel module = findById(id);

		module.setStatus(AppConstant.DeleteStatus.STATUS_DELETE);
		return convertToModel(moduleRepository.save(convertToEntity(module)));
	}

	public ModuleModel getModuleData(int id) {

		return convertToModel(moduleRepository.findById(id)
				.orElseThrow(() -> new ModuleNotFoundException(AppConstant.ErrorTypes.MODULE_NOT_EXIST_ERROR,
						AppConstant.ErrorCodes.MODULE_EXIST_ERROR_CODE,
						AppConstant.ErrorMessages.MODULE_NOT_EXIST_MESSAGE)));
	}
	
	public Module convertToEntity(ModuleModel moduleModel) {
		
		return Module.builder()
				.id(moduleModel.getId())
				.parentPermission(moduleModel.getParentPermission())
				.permission(moduleModel.getPermission())
				.addRight(moduleModel.isAddRight())
				.viewRight(moduleModel.isViewRight())
				.modifyRight(moduleModel.isModifyRight())
				.status(moduleModel.getStatus())
				.role(moduleModel.getRole())
				.build();
		
	}
	
	public ModuleModel convertToModel(Module moduleEntity) {
		
		 return ModuleModel.builder()
		.id(moduleEntity.getId())
		.parentPermission(moduleEntity.getParentPermission())
		.permission(moduleEntity.getPermission())
		.addRight(moduleEntity.isAddRight())
		.viewRight(moduleEntity.isViewRight())
		.modifyRight(moduleEntity.isModifyRight())
		.status(moduleEntity.getStatus())
		.role(moduleEntity.getRole())
		.build();
		
	}

}
