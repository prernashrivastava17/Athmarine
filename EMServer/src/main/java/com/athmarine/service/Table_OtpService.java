package com.athmarine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.Verification_Otp;
import com.athmarine.exception.EmailExistException;
import com.athmarine.repository.OtpRepository;
import com.athmarine.resources.AppConstant;

@Service
public class Table_OtpService {

	@Autowired
	OtpRepository otpRepository;

	public Verification_Otp findByPhone(String primaryPhone) {

		Verification_Otp phone = otpRepository.findByPhone(primaryPhone);

		return phone;

	}

	public Verification_Otp findByEmail(String email) {

		Verification_Otp email1;
		try {
			email1 = otpRepository.findByEmail(email);
		} catch (Exception e) {
			throw new EmailExistException(AppConstant.ErrorTypes.EMAIL_EXIST_ERROR,
					AppConstant.ErrorCodes.EMAIL_EXIST_ERROR_CODE, AppConstant.ErrorMessages.EMAIL_EXIST_MESSAGE);
		}

		return email1;

	}

}
