package com.athmarine.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.Module;
import com.athmarine.entity.Role;
import com.athmarine.exception.ResourceNotFoundException;
import com.athmarine.repository.ModuleRepository;
import com.athmarine.repository.RoleRepository;
import com.athmarine.request.ModuleModel;
import com.athmarine.request.RoleModel;
import com.athmarine.request.RoleModels;
import com.athmarine.resources.AppConstant;

@Service
public class RoleDetailsServiceImpl {

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	ModuleRepository moduleRepository;

	@Autowired
	ModuleService moduleService;

	public RoleModel createRole(RoleModel roleModel) {

		List<ModuleModel> listModule = new ArrayList<>();

		HashSet<Integer> hs = new HashSet<>(); // To avoid duplicate module

		for (ModuleModel m : roleModel.getModules()) {
			hs.add(m.getId());

		}

		for (Integer i : hs) {
			listModule.add(moduleService.findById(i));

		}
		return convertToModel(roleRepository.save(convertToEntity(roleModel)));

	}

	public RoleModel getRoleData(Integer id) {

		return convertToModel(roleRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.ErrorTypes.RESOURCE_NOT_FOUND_ERROR,
						AppConstant.ErrorCodes.RESOURCE_NOT_FOUND,
						AppConstant.ErrorMessages.RESOURCE_NOT_FOUND_MESSAGE)));
	}

	public RoleModel deleteRole(Integer id) {

		Role roleModel = roleRepository.findById(id).get();
		roleModel.setStatus(AppConstant.DeleteStatus.STATUS_DELETE);

		return convertToModels(roleRepository.save(roleModel));

	}
	
	public Role findByIds(Integer id) {
		
		/*if(id == null) {
			throw new ResourceNotFoundException(AppConstant.ErrorTypes.RESOURCE_NOT_FOUND_ERROR,
					AppConstant.ErrorCodes.RESOURCE_NOT_FOUND,"Role "+
					AppConstant.ErrorMessages.ID_EMPTY_MESSAGE);
		}
		else {*/
		return roleRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.ErrorTypes.RESOURCE_NOT_FOUND_ERROR,
						AppConstant.ErrorCodes.RESOURCE_NOT_FOUND,
						AppConstant.ErrorMessages.RESOURCE_NOT_FOUND_MESSAGE));
		//}
	}

	public RoleModel findById(Integer id) {
		if(id == null) {
			throw new ResourceNotFoundException(AppConstant.ErrorTypes.RESOURCE_NOT_FOUND_ERROR,
					AppConstant.ErrorCodes.RESOURCE_NOT_FOUND,"Role "+
					AppConstant.ErrorMessages.ID_EMPTY_MESSAGE);
		}
		else {
		return convertToModel(roleRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.ErrorTypes.RESOURCE_NOT_FOUND_ERROR,
						AppConstant.ErrorCodes.RESOURCE_NOT_FOUND,
						AppConstant.ErrorMessages.RESOURCE_NOT_FOUND_MESSAGE)));
		}
	}
	
	public RoleModels findByRoleId(Integer id) {
		if(id == null) {
			throw new ResourceNotFoundException(AppConstant.ErrorTypes.RESOURCE_NOT_FOUND_ERROR,
					AppConstant.ErrorCodes.RESOURCE_NOT_FOUND,"Role "+
					AppConstant.ErrorMessages.ID_EMPTY_MESSAGE);
		}
		else {
		return convertToModelS(roleRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.ErrorTypes.RESOURCE_NOT_FOUND_ERROR,
						AppConstant.ErrorCodes.RESOURCE_NOT_FOUND,
						AppConstant.ErrorMessages.RESOURCE_NOT_FOUND_MESSAGE)));
		}
	}

	public RoleModel updateRoleData(RoleModel roleModel) {
		RoleModel oldRole = findById(roleModel.getId());
		oldRole.setName(roleModel.getName());
		oldRole.setValue(roleModel.getValue());

		List<ModuleModel> listModule = new ArrayList<>();

		HashSet<Integer> hs = new HashSet<>();

		for (ModuleModel m : roleModel.getModules()) {
			hs.add(m.getId());

		}

		for (Integer i : hs) {
			listModule.add(moduleService.findById(i));

		}
		oldRole.getModules().addAll(listModule);

		return convertToModel(roleRepository.save(convertToEntity(roleModel)));

	}

	private Role convertToEntity(RoleModel roleModel) {

		List<Module> moduleList = roleModel.getModules().stream().map(roles -> moduleService.convertToEntity(roles))
				.collect(Collectors.toList());

		return Role.builder().id(roleModel.getId()).name(roleModel.getName()).value(roleModel.getValue())
				.status(roleModel.getStatus()).modules(moduleList).build();

	}

	private RoleModel convertToModel(Role roleEntity) {

		List<ModuleModel> moduleList = roleEntity.getModules().stream()
				.map(roles -> moduleService.convertToModel(roles)).collect(Collectors.toList());

		return RoleModel.builder()
				.id(roleEntity.getId())
				.name(roleEntity.getName())
				.value(roleEntity.getValue())
				.status(roleEntity.getStatus())
				.modules(moduleList).build();

	}
	
	private RoleModel convertToModels(Role roleEntity) {


		return RoleModel.builder()
				.id(roleEntity.getId())
				.name(roleEntity.getName())
				.value(roleEntity.getValue())
				.status(roleEntity.getStatus()).build();

	}
	
	private RoleModels convertToModelS(Role roleEntity) {


		return RoleModels.builder()
				.id(roleEntity.getId())
				.name(roleEntity.getName())
				.value(roleEntity.getValue())
				.status(roleEntity.getStatus()).build();

	}

}