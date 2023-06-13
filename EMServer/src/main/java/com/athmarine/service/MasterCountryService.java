package com.athmarine.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.MSTCountry;
import com.athmarine.exception.ModuleNotFoundException;
import com.athmarine.repository.MasterCountryRepository;
import com.athmarine.request.CountryModel;
import com.athmarine.request.CurrencySymbollModel;
import com.athmarine.request.MasterCountryModel;
import com.athmarine.request.MasterCountryRequestModel;
import com.athmarine.resources.AppConstant;

@Service
public class MasterCountryService {

	@Autowired
	MasterCountryRepository masterCountryRepository;

	public MasterCountryModel getMasterCountryDetails(@NotNull int id) {
		return convertToModel(masterCountryRepository.findById(id)
				.orElseThrow(() -> new ModuleNotFoundException(AppConstant.ErrorTypes.MASTER_COUNTRY_EXIST_ERROR,
						AppConstant.ErrorCodes.MASTER_COUNTRY_ERROR_CODE,
						AppConstant.ErrorMessages.MASTER_COUNTRY_NOT_EXTIS_MESSAGE)));
	}

	public List<MasterCountryModel> getAllCountry() {
		return masterCountryRepository.findAllByOrderByNameAsc().stream().map(country -> convertToModel(country))
				.collect(Collectors.toList());
	}

	public List<CurrencySymbollModel> getAllContryCurrencySymbol() {

		Set<String> nameSet = new HashSet<>();
		return masterCountryRepository.findAllByOrderByCurrencyAsc().stream()
				.map(country -> convertToModelCurrency(country)).filter(e -> nameSet.add(e.getCurrency()))
				.collect(Collectors.toList());
	}

	public MasterCountryRequestModel getVendorServiceByCountryId(Integer countryId) {
		MSTCountry masterCountry = findByCountryId(countryId);
		return convertToModelVendorCompanyService(masterCountry);

	}

	public CountryModel findById(int id) {
		return convertToModels(masterCountryRepository.findById(id)
				.orElseThrow(() -> new ModuleNotFoundException(AppConstant.ErrorTypes.MASTER_COUNTRY_EXIST_ERROR,
						AppConstant.ErrorCodes.MASTER_COUNTRY_ERROR_CODE,
						AppConstant.ErrorMessages.MASTER_COUNTRY_NOT_EXTIS_MESSAGE)));

	}

	public MasterCountryModel findByIds(int id) {
		return convertToModel(masterCountryRepository.findById(id)
				.orElseThrow(() -> new ModuleNotFoundException(AppConstant.ErrorTypes.MASTER_COUNTRY_EXIST_ERROR,
						AppConstant.ErrorCodes.MASTER_COUNTRY_ERROR_CODE,
						AppConstant.ErrorMessages.MASTER_COUNTRY_NOT_EXTIS_MESSAGE)));

	}

	public MSTCountry findByCountryId(Integer id) {

		if (id == null) {
			throw new ModuleNotFoundException(AppConstant.ErrorTypes.MASTER_COUNTRY_EXIST_ERROR,
					AppConstant.ErrorCodes.MASTER_COUNTRY_ERROR_CODE, AppConstant.ErrorMessages.ID_NULL_NOT_EXIST);
		} else {
			return masterCountryRepository.findById(id)
					.orElseThrow(() -> new ModuleNotFoundException(AppConstant.ErrorTypes.MASTER_COUNTRY_EXIST_ERROR,
							AppConstant.ErrorCodes.MASTER_COUNTRY_ERROR_CODE,
							AppConstant.ErrorMessages.MASTER_COUNTRY_NOT_EXTIS_MESSAGE));
		}

	}


	MasterCountryModel convertToModel(MSTCountry entity) {

		return MasterCountryModel.builder().id(entity.getId()).name(entity.getName()).phoneCode(entity.getPhoneCode())
				.shortName(entity.getShortName()).flag(entity.getFlag()).currency(entity.getCurrency())
				.status(entity.getStatus()).build();

	}

	MasterCountryRequestModel convertToModelVendorCompanyService(MSTCountry entity) {

		return MasterCountryRequestModel.builder().id(entity.getId()).name(entity.getName())
				.phoneCode(entity.getPhoneCode()).shortName(entity.getShortName()).flag(entity.getFlag())
				.currency(entity.getCurrency()).status(entity.getStatus()).build();

	}

	CurrencySymbollModel convertToModelCurrency(MSTCountry entity) {

		return CurrencySymbollModel.builder().id(entity.getId()).currency(entity.getCurrency().substring(0, 3)).build();

	}

	public CountryModel convertToModels(MSTCountry entity) {

		return CountryModel.builder().id(entity.getId()).name(entity.getName()).shortName(entity.getShortName())
				.currency(entity.getCurrency()).phoneCode(entity.getPhoneCode()).flag(entity.getFlag())
				.status(entity.getStatus()).build();

	}

}
