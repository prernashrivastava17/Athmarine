package com.athmarine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.SparesCustomer;
import com.athmarine.exception.InvalidInputException;
import com.athmarine.exception.SparesVendorException;
import com.athmarine.repository.SparesCustomerRepository;
import com.athmarine.request.SparesCustomerModel;
import com.athmarine.resources.AppConstant;

@Service
public class SparesCustomerService {

	@Autowired
	SparesCustomerRepository sparesCustomerRepository;

	public SparesCustomerModel createSparesCustomer(SparesCustomerModel model) {

		return convertToModel(sparesCustomerRepository.save(convertToEntity(model)));
	}

	public SparesCustomer findByIds(Integer id) {

		if (id == null) {

			throw new InvalidInputException(AppConstant.ErrorTypes.ID_NULL_EXIST_ERROR,
					AppConstant.ErrorCodes.ID_NULL_ERROR_CODE, AppConstant.ErrorMessages.ID_NULL_NOT_EXIST+"Spac");

		}
		return sparesCustomerRepository.findById(id)
				.orElseThrow(() -> new SparesVendorException(AppConstant.ErrorTypes.SPARES_VENDOR_EXIST_ERROR,
						AppConstant.ErrorCodes.SPARES_VENDOR_ERROR_CODE,
						AppConstant.ErrorMessages.SPARES_VENDOR_EXIST_MESSAGE));
	}

	public SparesCustomerModel findById(Integer id) {
		if (id == null) {
			throw new SparesVendorException(AppConstant.ErrorTypes.SPARES_VENDOR_EXIST_ERROR,
					AppConstant.ErrorCodes.SPARES_VENDOR_ERROR_CODE, AppConstant.ErrorMessages.ID_EMPTY_MESSAGE);
		} else {
			return convertToModel(sparesCustomerRepository.findById(id)
					.orElseThrow(() -> new SparesVendorException(AppConstant.ErrorTypes.SPARES_VENDOR_EXIST_ERROR,
							AppConstant.ErrorCodes.SPARES_VENDOR_ERROR_CODE,
							AppConstant.ErrorMessages.SPARES_VENDOR_EXIST_MESSAGE)));
		}
	}

	public SparesCustomer convertToEntity(SparesCustomerModel model) {

		Integer totalSum = model.getItemOne() + model.getItemTwo() + model.getItemThree() + model.getItemFour()
				+ model.getItemFive();

		return SparesCustomer.builder().id(model.getId()).itemOne(model.getItemOne()).itemTwo(model.getItemTwo())
				.itemThree(model.getItemThree()).itemFour(model.getItemFour()).itemFive(model.getItemFive())
				.totalSum(totalSum).build();

	}

	public SparesCustomerModel convertToModel(SparesCustomer entity) {

		Integer totalSum = entity.getItemOne() + entity.getItemTwo() + entity.getItemThree() + entity.getItemFour()
				+ entity.getItemFive();

		return SparesCustomerModel.builder().id(entity.getId()).itemOne(entity.getItemOne())
				.itemTwo(entity.getItemTwo()).itemThree(entity.getItemThree()).itemFour(entity.getItemFour())
				.itemFive(entity.getItemFive()).totalSum(totalSum).build();
	}

}
