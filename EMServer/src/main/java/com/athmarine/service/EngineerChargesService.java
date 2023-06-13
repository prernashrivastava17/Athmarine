package com.athmarine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.EngineerCharges;
import com.athmarine.entity.User;
import com.athmarine.exception.EngineerChargeNotFoundException;
import com.athmarine.exception.UserNotFoundException;
import com.athmarine.repository.EngineerChagesRepository;
import com.athmarine.request.EngineerChargesModel;
import com.athmarine.resources.AppConstant;

@Service
public class EngineerChargesService {

	@Autowired
	EngineerChagesRepository engineerChagesRepository;

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	public EngineerChargesModel createEngineerCharges(EngineerChargesModel engineerChargesModel) {

		return convertToModel(engineerChagesRepository.save(convertToEntity(engineerChargesModel)));
	}

	public EngineerChargesModel findEngineerChargesById() {

		return engineerChagesRepository.findAll().stream().findFirst().map(charge -> convertToModel(charge))
				.orElse(null);
	}

	public EngineerChargesModel updateEngineerCharges(EngineerChargesModel engineerChargesModel) {

		EngineerChargesModel engineerChargesModels = findById(engineerChargesModel.getId());
		engineerChargesModels.setId(engineerChargesModel.getId());
		engineerChargesModels.setCompanyId(engineerChargesModel.getCompanyId());

		return convertToModel(engineerChagesRepository.save(convertToEntity(engineerChargesModels)));
	}

	public EngineerChargesModel findById(Integer id) {
		if (id == null) {
			throw new UserNotFoundException(AppConstant.ErrorTypes.USER_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.USER_DOES_NOT_EXISTS_ERROR_CODE,
					AppConstant.ErrorMessages.EMAIL_NOT_EXIST_MESSAGE);
		} else {
			return convertToModel(engineerChagesRepository.findById(id)
					.orElseThrow(() -> new UserNotFoundException(AppConstant.ErrorTypes.USER_NOT_EXIST_ERROR,
							AppConstant.ErrorCodes.USER_DOES_NOT_EXISTS_ERROR_CODE,
							AppConstant.ErrorMessages.USER_NOT_EXIST_MESSAGE)));
		}

	}

	public EngineerCharges findByIds(Integer id) {
		if (id == null) {

			throw new EngineerChargeNotFoundException(AppConstant.ErrorTypes.ENGINEER_CHARGE_EXIST_ERROR,
					AppConstant.ErrorCodes.ENGINEER_CHARGE_ERROR_CODE,
					AppConstant.ErrorMessages.ENGINEER_CHARGE_ERROR_MESSAGE);

		}
		return engineerChagesRepository.findById(id).orElseThrow(() -> new EngineerChargeNotFoundException(
				AppConstant.ErrorTypes.ENGINEER_CHARGE_EXIST_ERROR, AppConstant.ErrorCodes.ENGINEER_CHARGE_ERROR_CODE,
				AppConstant.ErrorMessages.ENGINEER_CHARGE_ERROR_MESSAGE));
	}

	public EngineerCharges convertToEntity(EngineerChargesModel model) {

		User userCompany = userDetailsService.findByIds(model.getCompanyId());

		return EngineerCharges.builder().id(model.getId()).userCharges(userCompany).maxCharge(model.getMaxCharge())
				.basePrice(model.getBasePrice()).extraPrice(model.getExtraPrice()).extraAbove(model.getExtraAbove())
				.baseEngeerCount(model.getBaseEngeerCount()).extraEngeerCount(model.getExtraEngeerCount())
				.extraAboveEngeerCount(model.getExtraAboveEngeerCount())
				.maxChargeEngeerCount(model.getMaxChargeEngeerCount()).build();

	}

	public EngineerChargesModel convertToModel(EngineerCharges entity) {

		User userCompany = userDetailsService.findByIds(entity.getUserCharges().getId());

		return EngineerChargesModel.builder().id(entity.getId()).companyId(userCompany.getId())
				.maxCharge(entity.getMaxCharge()).basePrice(entity.getBasePrice()).extraPrice(entity.getExtraPrice())
				.extraAbove(entity.getExtraAbove()).baseEngeerCount(entity.getBaseEngeerCount())
				.extraEngeerCount(entity.getExtraEngeerCount()).extraAboveEngeerCount(entity.getExtraAboveEngeerCount())
				.maxChargeEngeerCount(entity.getMaxChargeEngeerCount()).build();
	}

}
