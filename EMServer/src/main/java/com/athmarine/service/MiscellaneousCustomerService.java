package com.athmarine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.MiscellaneousCustomer;
import com.athmarine.exception.InvalidInputException;
import com.athmarine.exception.MiscellaneousVendorException;
import com.athmarine.repository.MiscellaneousCustomerRepository;
import com.athmarine.request.MiscellaneousCustomerModel;
import com.athmarine.resources.AppConstant;

@Service
public class MiscellaneousCustomerService {

	@Autowired
	MiscellaneousCustomerRepository miscellaneousCustomerRepository;

	public MiscellaneousCustomerModel createMiscellaneousCustomer(MiscellaneousCustomerModel model) {

		return convertToModel(miscellaneousCustomerRepository.save(convertToEntity(model)));
	}

	public MiscellaneousCustomer findByIds(Integer id) {

		if (id == null) {

			throw new InvalidInputException(AppConstant.ErrorTypes.ID_NULL_EXIST_ERROR,
					AppConstant.ErrorCodes.ID_NULL_ERROR_CODE, AppConstant.ErrorMessages.ID_NULL_NOT_EXIST+"misc");

		}
		return miscellaneousCustomerRepository.findById(id).orElseThrow(
				() -> new MiscellaneousVendorException(AppConstant.ErrorTypes.MISCELLANEOUS_VENDOR_EXIST_ERROR,
						AppConstant.ErrorCodes.MISCELLANEOUS_VENDOR_ERROR_CODE,
						AppConstant.ErrorMessages.MISCELLANEOUS_VENDOR_EXIST_MESSAGE));
	}

	public MiscellaneousCustomerModel findById(Integer id) {
		if (id == null) {
			throw new MiscellaneousVendorException(AppConstant.ErrorTypes.MISCELLANEOUS_VENDOR_EXIST_ERROR,
					AppConstant.ErrorCodes.MISCELLANEOUS_VENDOR_ERROR_CODE, AppConstant.ErrorMessages.ID_EMPTY_MESSAGE);
		} else {
			return convertToModel(miscellaneousCustomerRepository.findById(id)
					.orElseThrow(() -> new MiscellaneousVendorException(
							AppConstant.ErrorTypes.MISCELLANEOUS_VENDOR_EXIST_ERROR,
							AppConstant.ErrorCodes.MISCELLANEOUS_VENDOR_ERROR_CODE,
							AppConstant.ErrorMessages.MISCELLANEOUS_VENDOR_EXIST_MESSAGE)));
		}
	}

	public MiscellaneousCustomer convertToEntity(MiscellaneousCustomerModel model) {

		Integer totalSum = model.getCovidTestCharge() + model.getCovidTestCharge() + model.getShipyardCharges()
				+ model.getShipyardSurcharge() + model.getOtherCharge();

		return MiscellaneousCustomer.builder().id(model.getId()).portCharges(model.getCovidTestCharge())
				.covidTestCharge(model.getCovidTestCharge()).shipyardCharges(model.getShipyardCharges())
				.shipyardSurcharge(model.getShipyardSurcharge()).otherCharge(model.getOtherCharge()).totalSum(totalSum)
				.build();

	}

	public MiscellaneousCustomerModel convertToModel(MiscellaneousCustomer entity) {

		Integer totalSum = entity.getCovidTestCharge() + entity.getCovidTestCharge() + entity.getShipyardCharges()
				+ entity.getShipyardSurcharge() + entity.getOtherCharge();

		return MiscellaneousCustomerModel.builder().id(entity.getId()).portCharges(entity.getCovidTestCharge())
				.covidTestCharge(entity.getCovidTestCharge()).shipyardCharges(entity.getShipyardCharges())
				.shipyardSurcharge(entity.getShipyardSurcharge()).otherCharge(entity.getOtherCharge())
				.totalSum(totalSum).build();
	}

}
