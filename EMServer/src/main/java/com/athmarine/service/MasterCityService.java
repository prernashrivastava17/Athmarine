package com.athmarine.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.MSTCity;
import com.athmarine.entity.MSTCountry;
import com.athmarine.entity.MSTState;
import com.athmarine.exception.InvalidInputException;
import com.athmarine.exception.MasterCityNotFoundException;
import com.athmarine.repository.MSTCityRepository;
import com.athmarine.repository.MasterCountryRepository;
import com.athmarine.repository.MasterStateRepository;
import com.athmarine.request.MasterCityModel;
import com.athmarine.resources.AppConstant;

@Service
public class MasterCityService {

	@Autowired
	MSTCityRepository mSTCityRepository;

	@Autowired
	MasterStateRepository masterStateRepository;

	@Autowired
	MasterCountryRepository masterCountryRepository;

	public MasterCityModel findById(Integer id) {
		return convertToModel(mSTCityRepository.findById(id)
				.orElseThrow(() -> new MasterCityNotFoundException(AppConstant.ErrorTypes.MANUFACTURER_EXIST_ERROR,
						AppConstant.ErrorCodes.MANUFACTURER_ERROR_CODE,
						AppConstant.ErrorMessages.MANUFACTURER_NOT_EXIST)));

	}

	public MSTCity findByIds(Integer id) {

		if (id == null) {

			throw new InvalidInputException(AppConstant.ErrorTypes.ID_NULL_EXIST_ERROR,
					AppConstant.ErrorCodes.ID_NULL_ERROR_CODE, AppConstant.ErrorMessages.ID_EMPTY_MESSAGE);

		}
		return mSTCityRepository.findById(id)
				.orElseThrow(() -> new MasterCityNotFoundException(AppConstant.ErrorTypes.CITY_NOT_EXIST_ERROR,
						AppConstant.ErrorCodes.CITY_NOT_EXIST_ERROR_CODE,
						AppConstant.ErrorMessages.CITY_NOT_EXIST_ERROR));
	}

	public List<MasterCityModel> getAllCityByStateId(@NotNull int id) {

		MSTState mstCountry = masterStateRepository.findById(id)
				.orElseThrow(() -> new MasterCityNotFoundException(AppConstant.ErrorTypes.MASTER_COUNTRY_EXIST_ERROR,
						AppConstant.ErrorCodes.MASTER_COUNTRY_ERROR_CODE,
						AppConstant.ErrorMessages.MASTER_COUNTRY_NOT_EXTIS_MESSAGE));

		List<MasterCityModel> list = mstCountry.getMstCity().stream().map(cities -> convertToModel(cities))
				.collect(Collectors.toList());
		return list.stream().sorted((o1, o2) -> o1.getName().compareTo(o2.getName())).collect(Collectors.toList());

	}

	public List<MasterCityModel> getAllCityByCountryId(@NotNull int id) {

		MSTCountry mstCountry = masterCountryRepository.findById(id)
				.orElseThrow(() -> new MasterCityNotFoundException(AppConstant.ErrorTypes.MASTER_COUNTRY_EXIST_ERROR,
						AppConstant.ErrorCodes.MASTER_COUNTRY_ERROR_CODE,
						AppConstant.ErrorMessages.MASTER_COUNTRY_NOT_EXTIS_MESSAGE));

		List<MasterCityModel> list = mstCountry.getCountry().stream().map(cities -> convertToModel(cities))
				.collect(Collectors.toList());
		return list.stream().sorted((o1, o2) -> o1.getName().compareTo(o2.getName())).collect(Collectors.toList());

	}

	private MasterCityModel convertToModel(MSTCity entity) {

		return MasterCityModel.builder().id(entity.getId()).name(entity.getName()).status(entity.getStatus()).build();

	}

}
