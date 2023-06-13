package com.athmarine.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.ContactUs;
import com.athmarine.exception.EmailExistException;
import com.athmarine.repository.ContactUsRepository;
import com.athmarine.request.ContactUsModel;
import com.athmarine.resources.AppConstant;

@Service
public class ContactUsService {

	@Autowired
	ContactUsRepository contactUsRepository;

	public boolean createContactUs(ContactUsModel model) {
		boolean response = false;
		if (model.isPrivacyPolicy() == false) {
			throw new EmailExistException(AppConstant.ErrorTypes.POLICY_ERROR,
					AppConstant.ErrorCodes.EMAIL_EXIST_ERROR_CODE, AppConstant.ErrorMessages.EMAIL_POLICY_MESSAGE);

		} else {
			convertToModel(contactUsRepository.save(convertToEntity(model)));
			response = true;
			return response;

		}

	}

	public List<ContactUsModel> getAllContactUs() {

		return contactUsRepository.findAll().stream().map(contactus -> convertToModel(contactus))
				.collect(Collectors.toList());
	}

	public ContactUs convertToEntity(ContactUsModel model) {

		return ContactUs.builder().id(model.getId()).name(model.getName()).email(model.getEmail())
				.subject(model.getSubject()).message(model.getMessage()).termAndConditionStatus(model.isPrivacyPolicy())
				.build();

	}

	public ContactUsModel convertToModel(ContactUs entity) {

		return ContactUsModel.builder().id(entity.getId()).name(entity.getName()).email(entity.getEmail())
				.subject(entity.getSubject()).message(entity.getMessage())
				.privacyPolicy(entity.isTermAndConditionStatus()).build();

	}

}