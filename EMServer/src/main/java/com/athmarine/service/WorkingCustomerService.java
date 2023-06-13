package com.athmarine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.WorkingCustomer;
import com.athmarine.exception.InvalidInputException;
import com.athmarine.exception.TravelCostVendorException;
import com.athmarine.repository.WorkingCustomerRepository;
import com.athmarine.request.WorkingCustomerModel;
import com.athmarine.resources.AppConstant;

@Service
public class WorkingCustomerService {

	@Autowired
	WorkingCustomerRepository workingCustomerRepository;

	public WorkingCustomerModel createWorkingCustomer(WorkingCustomerModel model) {

		return convertToModel(workingCustomerRepository.save(convertToEntity(model)));
	}

	public WorkingCustomer findByIds(Integer id) {

		if (id == null) {

			throw new InvalidInputException(AppConstant.ErrorTypes.ID_NULL_EXIST_ERROR,
					AppConstant.ErrorCodes.ID_NULL_ERROR_CODE, AppConstant.ErrorMessages.ID_NULL_NOT_EXIST+"WrkC");

		} else {
			return workingCustomerRepository.findById(id).orElseThrow(
					() -> new TravelCostVendorException(AppConstant.ErrorTypes.TRAVEL_COST_VENDOR_EXIST_ERROR,
							AppConstant.ErrorCodes.TRAVEL_COST_VENDOR_ERROR_CODE,
							AppConstant.ErrorMessages.TRAVEL_COST_VENDOR_EXIST_MESSAGE));
		}
	}

	public WorkingCustomerModel findById(Integer id) {
		if (id == null) {
			throw new TravelCostVendorException(AppConstant.ErrorTypes.TRAVEL_COST_VENDOR_EXIST_ERROR,
					AppConstant.ErrorCodes.TRAVEL_COST_VENDOR_ERROR_CODE, AppConstant.ErrorMessages.ID_EMPTY_MESSAGE);
		} else {
			return convertToModel(workingCustomerRepository.findById(id).orElseThrow(
					() -> new TravelCostVendorException(AppConstant.ErrorTypes.TRAVEL_COST_VENDOR_EXIST_ERROR,
							AppConstant.ErrorCodes.TRAVEL_COST_VENDOR_ERROR_CODE,
							AppConstant.ErrorMessages.TRAVEL_COST_VENDOR_EXIST_MESSAGE)));
		}
	}

	public WorkingCustomer convertToEntity(WorkingCustomerModel model) {

		Integer sum = model.getWorkingRate() * model.getEstimatedWorkingHours();

		Integer sumOTP1 = model.getWorkingRateOT1() * model.getEstimatedWorkingHours();

		Integer totalSum = sum + sumOTP1 + model.getWorkingRateOT2();

		return WorkingCustomer.builder().id(model.getId()).workingRate(model.getWorkingRate())
				.workingRateOT1(model.getWorkingRateOT1()).workingRateOT2(model.getWorkingRateOT2())
				.estimatedWorkingHours(model.getEstimatedWorkingHours())
				//.holidays(model.getHolidays())
				.totalSum(totalSum).build();

	}

	public WorkingCustomerModel convertToModel(WorkingCustomer entity) {

		Integer sum = entity.getWorkingRate() * entity.getEstimatedWorkingHours();

		Integer sumOTP1 = entity.getWorkingRateOT1() * entity.getEstimatedWorkingHours();

		Integer totalSum = sum + sumOTP1 + entity.getWorkingRateOT2();

		return WorkingCustomerModel.builder().id(entity.getId()).workingRate(entity.getWorkingRate())
				.workingRateOT1(entity.getWorkingRateOT1()).workingRateOT2(entity.getWorkingRateOT2())
				.estimatedWorkingHours(entity.getEstimatedWorkingHours())
				//.holidays(entity.getHolidays())
				.totalSum(totalSum).build();
	}

}
