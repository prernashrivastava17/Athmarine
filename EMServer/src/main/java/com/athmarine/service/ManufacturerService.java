package com.athmarine.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.Equipment;
import com.athmarine.entity.MSTManufacturer;
import com.athmarine.exception.ManufacturerNotFoundException;
import com.athmarine.repository.ManufacturerRepository;
import com.athmarine.request.ManufacturerModule;
import com.athmarine.resources.AppConstant;

@Service
public class ManufacturerService {

	@Autowired
	ManufacturerRepository manufacturerRepository;

	@Autowired
	EquipmentService equipmentService;

	public ManufacturerModule createEquipmentManufacturer(ManufacturerModule manufacturerModule) {

		return convertToModel(manufacturerRepository.save(convertToEntity(manufacturerModule)));
	}

	public List<ManufacturerModule> getAllEquipmentManufacturer() {

		return manufacturerRepository.findAll().stream().sorted(Comparator.comparing(MSTManufacturer::getName))
				.map(equipmentManufacturer -> convertToModel(equipmentManufacturer)).collect(Collectors.toList());
	}

	public List<ManufacturerModule> getAllManufacturerByEquipmentId(@NotBlank int id) {

		Equipment equipment = equipmentService.findByIds(id);

		List<ManufacturerModule> manufacturerModules = manufacturerRepository.findAllByEquipment(equipment).stream()
				.filter(entity -> entity.isVerified())
				.sorted(Comparator.comparing(MSTManufacturer::getName)).map(manufacture -> convertToModel(manufacture))
				.collect(Collectors.toList());
		ManufacturerModule other = new ManufacturerModule();
		other.setName("Others");
		manufacturerModules.add(other);
		return manufacturerModules;
	}

	public ManufacturerModule updateEquipmentManufacturer(ManufacturerModule manufacturerModule) {

		ManufacturerModule manufacturer = findById(manufacturerModule.getId());

		manufacturerModule.setId(manufacturer.getId());

		return convertToModel(manufacturerRepository.save(convertToEntity(manufacturerModule)));
	}

	public ManufacturerModule deleteEquipmentManufacturer(@NotBlank int id) {

		ManufacturerModule manufacturerModule = findById(id);

		manufacturerModule.setStatus(AppConstant.DeleteStatus.STATUS_DELETE);

		return convertToModel(manufacturerRepository.save(convertToEntity(manufacturerModule)));
	}

	public ManufacturerModule findManufacturerById(@NotBlank int id) {

		return convertToModel(manufacturerRepository.findById(id)
				.orElseThrow(() -> new ManufacturerNotFoundException(AppConstant.ErrorTypes.MANUFACTURER_EXIST_ERROR,
						AppConstant.ErrorCodes.MANUFACTURER_ERROR_CODE,
						AppConstant.ErrorMessages.MANUFACTURER_NOT_EXIST)));

	}

	public ManufacturerModule findById(Integer id) {
		return convertToModel(manufacturerRepository.findById(id)
				.orElseThrow(() -> new ManufacturerNotFoundException(AppConstant.ErrorTypes.MANUFACTURER_EXIST_ERROR,
						AppConstant.ErrorCodes.MANUFACTURER_ERROR_CODE,
						AppConstant.ErrorMessages.MANUFACTURER_NOT_EXIST)));

	}

	public MSTManufacturer findByIds(Integer id) {

		if (id == null) {

			throw new ManufacturerNotFoundException(AppConstant.ErrorTypes.MANUFACTURER_EXIST_ERROR,
					AppConstant.ErrorCodes.MANUFACTURER_ERROR_CODE, AppConstant.ErrorMessages.MANUFACTURER_NOT_EXIST);

		}
		return manufacturerRepository.findById(id)
				.orElseThrow(() -> new ManufacturerNotFoundException(AppConstant.ErrorTypes.MANUFACTURER_EXIST_ERROR,
						AppConstant.ErrorCodes.MANUFACTURER_ERROR_CODE,
						AppConstant.ErrorMessages.MANUFACTURER_NOT_EXIST));
	}

	public MSTManufacturer convertToEntity(ManufacturerModule model) {

		return MSTManufacturer.builder().id(model.getId()).name(model.getName()).status(model.getStatus())
				.isVerified(false)
				.build();

	}

	public ManufacturerModule convertToModel(MSTManufacturer entity) {
		return ManufacturerModule.builder().id(entity.getId()).name(entity.getName()).status(entity.getStatus())
				.build();

	}

}
