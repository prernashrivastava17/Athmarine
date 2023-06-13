package com.athmarine.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.PhoneSubscription;
import com.athmarine.exception.ResourceNotFoundException;
import com.athmarine.exception.UserNotFoundException;
import com.athmarine.repository.PhoneSubscriptionRepository;
import com.athmarine.request.PhoneSubscriptionModel;
import com.athmarine.resources.AppConstant;

@Service
public class PhoneSubscriptionService {

	@Autowired
	PhoneSubscriptionRepository phoneSubscriptionRepository;

	public PhoneSubscriptionModel createPhoneSubscription(PhoneSubscriptionModel model) {

		return convertToModel(phoneSubscriptionRepository.save(convertToEntity(model)));
	}

	public List<PhoneSubscriptionModel> getAllPhoneSubscription() {

		return phoneSubscriptionRepository.findAll().stream().map(subscription -> convertToModel(subscription))
				.collect(Collectors.toList());
	}

	public PhoneSubscription findByIds(Integer id) {

		if (id == null) {

			throw new ResourceNotFoundException(AppConstant.ErrorTypes.USER_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.USER_DOES_NOT_EXISTS_ERROR_CODE,
					AppConstant.ErrorMessages.USER_EMPTY_EXIST_MESSAGE);
		}
		return phoneSubscriptionRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException(AppConstant.ErrorTypes.USER_NOT_EXIST_ERROR,
						AppConstant.ErrorCodes.USER_DOES_NOT_EXISTS_ERROR_CODE,
						AppConstant.ErrorMessages.USER_NOT_EXIST_MESSAGE));
	}

	public PhoneSubscriptionModel findById(Integer id) {
		if (id == null) {
			throw new ResourceNotFoundException(AppConstant.ErrorTypes.USER_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.USER_DOES_NOT_EXISTS_ERROR_CODE,
					AppConstant.ErrorMessages.USER_EMPTY_EXIST_MESSAGE);
		} else {
			return convertToModel(phoneSubscriptionRepository.findById(id)
					.orElseThrow(() -> new UserNotFoundException(AppConstant.ErrorTypes.USER_NOT_EXIST_ERROR,
							AppConstant.ErrorCodes.USER_DOES_NOT_EXISTS_ERROR_CODE,
							AppConstant.ErrorMessages.USER_NOT_EXIST_MESSAGE)));
		}
	}

	public PhoneSubscription convertToEntity(PhoneSubscriptionModel model) {

		return PhoneSubscription.builder().id(model.getId()).phoneNum(model.getPhoneNum()).status(model.getStatus())
				.build();

	}

	public PhoneSubscriptionModel convertToModel(PhoneSubscription entity) {

		return PhoneSubscriptionModel.builder().id(entity.getId()).phoneNum(entity.getPhoneNum())
				.status(entity.getStatus()).build();
	}

}
