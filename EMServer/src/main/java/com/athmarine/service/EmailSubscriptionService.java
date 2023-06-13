package com.athmarine.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.EmailSubscription;
import com.athmarine.exception.ResourceNotFoundException;
import com.athmarine.exception.UserNotFoundException;
import com.athmarine.repository.EmailSubscriptionRepository;
import com.athmarine.request.EmailSubscriptionModel;
import com.athmarine.resources.AppConstant;

@Service
public class EmailSubscriptionService {

	@Autowired
	EmailSubscriptionRepository emailSubscriptionRepository;

	@Autowired
	UserDetailsServiceImpl userDetailsServiceImpl;

	public EmailSubscriptionModel createEmailSubscription(EmailSubscriptionModel model) {

		return convertToModel(emailSubscriptionRepository.save(convertToEntity(model)));
	}

	public List<EmailSubscriptionModel> getAllEmailSubscription() {

		return emailSubscriptionRepository.findAll().stream().map(subscription -> convertToModel(subscription))
				.collect(Collectors.toList());
	}

	public EmailSubscription findByIds(Integer id) {

		if (id == null) {

			throw new ResourceNotFoundException(AppConstant.ErrorTypes.USER_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.USER_DOES_NOT_EXISTS_ERROR_CODE,
					AppConstant.ErrorMessages.USER_EMPTY_EXIST_MESSAGE);

		}
		return emailSubscriptionRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException(AppConstant.ErrorTypes.USER_NOT_EXIST_ERROR,
						AppConstant.ErrorCodes.USER_DOES_NOT_EXISTS_ERROR_CODE,
						AppConstant.ErrorMessages.USER_NOT_EXIST_MESSAGE));
	}

	public EmailSubscriptionModel findById(Integer id) {
		if (id == null) {
			throw new ResourceNotFoundException(AppConstant.ErrorTypes.USER_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.USER_DOES_NOT_EXISTS_ERROR_CODE,
					AppConstant.ErrorMessages.USER_EMPTY_EXIST_MESSAGE);
		} else {
			return convertToModel(emailSubscriptionRepository.findById(id)
					.orElseThrow(() -> new UserNotFoundException(AppConstant.ErrorTypes.USER_NOT_EXIST_ERROR,
							AppConstant.ErrorCodes.USER_DOES_NOT_EXISTS_ERROR_CODE,
							AppConstant.ErrorMessages.USER_NOT_EXIST_MESSAGE)));
		}
	}

	public EmailSubscription convertToEntity(EmailSubscriptionModel model) {

		return EmailSubscription.builder().id(model.getId()).email(model.getEmail()).status(model.getStatus()).build();

	}

	public EmailSubscriptionModel convertToModel(EmailSubscription entity) {

		return EmailSubscriptionModel.builder().id(entity.getId()).email(entity.getEmail()).status(entity.getStatus())
				.build();
	}

}
