package com.athmarine.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.MSTCountry;
import com.athmarine.entity.MSTState;
import com.athmarine.exception.ManufacturerNotFoundException;
import com.athmarine.repository.MasterCountryRepository;
import com.athmarine.repository.MasterStateRepository;
import com.athmarine.request.MasterStateModel;
import com.athmarine.request.VendorCompanyModel;
import com.athmarine.resources.AppConstant;

@Service
public class MasterStateService {

	@Autowired
	MasterStateRepository masterStateRepository;
	@Autowired
	MasterCountryService masterCountryService;
	@Autowired
	MasterCountryRepository masterCountryRepository;

	public MasterStateModel findById(Integer id) {
		return convertToModel(masterStateRepository.findById(id)
				.orElseThrow(() -> new ManufacturerNotFoundException(AppConstant.ErrorTypes.MANUFACTURER_EXIST_ERROR,
						AppConstant.ErrorCodes.MANUFACTURER_ERROR_CODE,
						AppConstant.ErrorMessages.MANUFACTURER_NOT_EXIST)));

	}

	public MSTState findByIds(Integer id) {

		if (id == null) {

			throw new ManufacturerNotFoundException(AppConstant.ErrorTypes.ID_NULL_EXIST_ERROR,
					AppConstant.ErrorCodes.ID_NULL_ERROR_CODE, AppConstant.ErrorMessages.ID_EMPTY_MESSAGE);

		}
		return masterStateRepository.findById(id)
				.orElseThrow(() -> new ManufacturerNotFoundException(AppConstant.ErrorTypes.MASTER_STATE_EXIST_ERROR,
						AppConstant.ErrorCodes.MASTER_STATE_ERROR_CODE,
						AppConstant.ErrorMessages.MASTER_STATE_NOT_EXTIS_MESSAGE));
	}

	public MSTState findState(Integer id) {

		MSTState state = null;

		if (id != null) {

			state = findByIds(id);
		}
		return state;
	}

	public MSTState convertToEntity(MasterStateModel model) {

		return MSTState.builder().id(model.getId()).name(model.getName()).status(model.getStatus()).build();

	}

	public MasterStateModel convertToModel(MSTState entity) {

		return MasterStateModel.builder().id(entity.getId()).name(entity.getName()).status(entity.getStatus()).build();

	}

	public List<MasterStateModel> getAllStatesByCountryId(@NotNull int id) {

		MSTCountry mstCountry = masterCountryRepository.findById(id)
				.orElseThrow(() -> new ManufacturerNotFoundException(AppConstant.ErrorTypes.MASTER_COUNTRY_EXIST_ERROR,
						AppConstant.ErrorCodes.MASTER_COUNTRY_ERROR_CODE,
						AppConstant.ErrorMessages.MASTER_COUNTRY_NOT_EXTIS_MESSAGE));
		List<MasterStateModel> list = mstCountry.getState().stream().map(states -> convertToModel(states))
				.collect(Collectors.toList());
		return list.stream().sorted((o1, o2) -> o1.getName().compareTo(o2.getName())).collect(Collectors.toList());
	}
	// create other state 
	
	public  MSTState createState(VendorCompanyModel model) {
		MSTState state=null;
		
		MSTCountry country= masterCountryService.findByCountryId(model.getMasterCountryModel().getId());
		state=MSTState.builder().id(model.getMstateStateModel().getId()).name(model.getMstateStateModel().getName()).status(model.getStatus()).country(country).build();
		System.out.println(state.getId());
		state=masterStateRepository.save(state);
		System.out.println(state.getId());
		model.getMstateStateModel().setId(state.getId());
		return state;
		
	}
	

}
