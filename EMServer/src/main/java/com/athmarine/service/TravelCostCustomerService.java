package com.athmarine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.TravelCostCustomer;
import com.athmarine.exception.InvalidInputException;
import com.athmarine.exception.TravelCostVendorException;
import com.athmarine.repository.TravelCostCustomerRepository;
import com.athmarine.request.TravelCostCustomerModel;
import com.athmarine.resources.AppConstant;

@Service
public class TravelCostCustomerService {

	@Autowired
	TravelCostCustomerRepository travelCostCustomerRepository;

	public TravelCostCustomerModel createTravelCostCustomer(TravelCostCustomerModel model) {

		return convertToModel(travelCostCustomerRepository.save(convertToEntity(model)));
	}

	public TravelCostCustomer findByIds(Integer id) {

		if (id == null) {

			throw new InvalidInputException(AppConstant.ErrorTypes.ID_NULL_EXIST_ERROR,
					AppConstant.ErrorCodes.ID_NULL_ERROR_CODE, AppConstant.ErrorMessages.ID_NULL_NOT_EXIST+"traC");

		} else {
			return travelCostCustomerRepository.findById(id).orElseThrow(
					() -> new TravelCostVendorException(AppConstant.ErrorTypes.TRAVEL_COST_VENDOR_EXIST_ERROR,
							AppConstant.ErrorCodes.TRAVEL_COST_VENDOR_ERROR_CODE,
							AppConstant.ErrorMessages.TRAVEL_COST_VENDOR_EXIST_MESSAGE));
		}
	}

	public TravelCostCustomerModel findById(Integer id) {
		if (id == null) {
			throw new TravelCostVendorException(AppConstant.ErrorTypes.TRAVEL_COST_VENDOR_EXIST_ERROR,
					AppConstant.ErrorCodes.TRAVEL_COST_VENDOR_ERROR_CODE, AppConstant.ErrorMessages.ID_EMPTY_MESSAGE);
		} else {
			return convertToModel(travelCostCustomerRepository.findById(id).orElseThrow(
					() -> new TravelCostVendorException(AppConstant.ErrorTypes.TRAVEL_COST_VENDOR_EXIST_ERROR,
							AppConstant.ErrorCodes.TRAVEL_COST_VENDOR_ERROR_CODE,
							AppConstant.ErrorMessages.TRAVEL_COST_VENDOR_EXIST_MESSAGE)));
		}
	}

	public TravelCostCustomer convertToEntity(TravelCostCustomerModel model) {

		Integer tempInteger = model.getTravelTimeCost() * model.getTotalTravelTime();

		Integer totalSum = model.getLumpSumCost() + model.getAirFare() + model.getTransportation() + tempInteger
				+ model.getEngineerRatePerHour();

		return TravelCostCustomer.builder().id(model.getId()).lumpSumCost(model.getLumpSumCost())
				.airFare(model.getAirFare()).transportation(model.getTransportation())
				.engineerRatePerHour(model.getEngineerRatePerHour()).totalTravelTime(model.getTotalTravelTime())
				.travelTimeCost(model.getTravelTimeCost()).totalSum(totalSum).build();

	}

	public TravelCostCustomerModel convertToModel(TravelCostCustomer entity) {

		Integer tempInteger = entity.getTravelTimeCost() * entity.getTotalTravelTime();

		Integer totalSum = entity.getLumpSumCost() + entity.getAirFare() + entity.getTransportation() + tempInteger
				+ entity.getEngineerRatePerHour();

		return TravelCostCustomerModel.builder().id(entity.getId()).lumpSumCost(entity.getLumpSumCost())
				.airFare(entity.getAirFare()).transportation(entity.getTransportation())
				.engineerRatePerHour(entity.getEngineerRatePerHour()).totalTravelTime(entity.getTotalTravelTime())
				.travelTimeCost(entity.getTravelTimeCost()).totalSum(totalSum).build();
	}

}
