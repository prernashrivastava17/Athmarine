package com.athmarine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.SparesVendor;
import com.athmarine.entity.User;
import com.athmarine.exception.InvalidInputException;
import com.athmarine.exception.SparesVendorException;
import com.athmarine.repository.SparesVendorRepository;
import com.athmarine.request.SparesVendorModel;
import com.athmarine.request.UserModel;
import com.athmarine.resources.AppConstant;

@Service
public class SparesVendorService {

	@Autowired
	SparesVendorRepository sparesVendorRepository;

	@Autowired
	UserDetailsServiceImpl userDetailsServiceImpl;

	public SparesVendorModel createSparesVendor(SparesVendorModel model) {

		return convertToModel(sparesVendorRepository.save(convertToEntity(model)));
	}

	public SparesVendorModel getVendorSparesById(Integer bidderId) {

		User user = userDetailsServiceImpl.findByIds(bidderId);

		return convertToModel(sparesVendorRepository.getSparesVendorBybidderId(user)
				.orElseThrow(() -> new InvalidInputException(AppConstant.ErrorTypes.BIDDER_ID_NULL_EXIST_ERROR,
						AppConstant.ErrorCodes.BIDDER_ID_NULL_ERROR_CODE,
						AppConstant.ErrorMessages.BIDDER_ID_NULL_NOT_EXIST)));
	}

	public SparesVendor findByIds(Integer id) {

		if (id == null) {

			throw new InvalidInputException(AppConstant.ErrorTypes.ID_NULL_EXIST_ERROR,
					AppConstant.ErrorCodes.ID_NULL_ERROR_CODE, AppConstant.ErrorMessages.ID_NULL_NOT_EXIST);

		}
		return sparesVendorRepository.findById(id)
				.orElseThrow(() -> new SparesVendorException(AppConstant.ErrorTypes.SPARES_VENDOR_EXIST_ERROR,
						AppConstant.ErrorCodes.SPARES_VENDOR_ERROR_CODE,
						AppConstant.ErrorMessages.SPARES_VENDOR_EXIST_MESSAGE));
	}

	public SparesVendorModel findById(Integer id) {
		if (id == null) {
			throw new SparesVendorException(AppConstant.ErrorTypes.SPARES_VENDOR_EXIST_ERROR,
					AppConstant.ErrorCodes.SPARES_VENDOR_ERROR_CODE, AppConstant.ErrorMessages.ID_EMPTY_MESSAGE);
		} else {
			return convertToModel(sparesVendorRepository.findById(id)
					.orElseThrow(() -> new SparesVendorException(AppConstant.ErrorTypes.SPARES_VENDOR_EXIST_ERROR,
							AppConstant.ErrorCodes.SPARES_VENDOR_ERROR_CODE,
							AppConstant.ErrorMessages.SPARES_VENDOR_EXIST_MESSAGE)));
		}
	}

	public SparesVendor convertToEntity(SparesVendorModel model) {

		User bidderId = userDetailsServiceImpl.findByIds(model.getBidderId());

		Integer totalSum = model.getItemOne() + model.getItemTwo() + model.getItemThree() + model.getItemFour()
				+ model.getItemFive();

		return SparesVendor.builder().id(model.getId()).itemOne(model.getItemOne()).itemTwo(model.getItemTwo())
				.itemThree(model.getItemThree()).itemFour(model.getItemFour()).itemFive(model.getItemFive())
				.totalSum(totalSum).bidderId(bidderId).build();

	}

	public SparesVendorModel convertToModel(SparesVendor entity) {

		UserModel bidderId = userDetailsServiceImpl.findById(entity.getBidderId().getId());

		Integer totalSum = entity.getItemOne() + entity.getItemTwo() + entity.getItemThree() + entity.getItemFour()
				+ entity.getItemFive();

		return SparesVendorModel.builder().id(entity.getId()).itemOne(entity.getItemOne()).itemTwo(entity.getItemTwo())
				.itemThree(entity.getItemThree()).itemFour(entity.getItemFour()).itemFive(entity.getItemFive())
				.totalSum(totalSum).bidderId(bidderId.getId()).build();
	}

}
