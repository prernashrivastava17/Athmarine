package com.athmarine.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.Feedback;
import com.athmarine.entity.User;
import com.athmarine.entity.VendorCompany;
import com.athmarine.repository.FeedbackRepository;
import com.athmarine.response.FeedbackResponseModel;
import com.sun.istack.NotNull;

@Service
public class FeedbackService {

	@Autowired
	FeedbackRepository feedbackRepository;

	@Autowired
	VendorCompanyService vendorCompanyService;

	@Autowired
	UserDetailsServiceImpl userDetailsServiceImpl;

	public FeedbackResponseModel getVendorCompanyFeedback(@NotNull Integer id) {

		VendorCompany vendorCompany = vendorCompanyService.findByIds(id);

		return convertToModel(feedbackRepository.findAllByVendorCompany(vendorCompany));

	}

	public FeedbackResponseModel getEngineerFeedback(@NotNull Integer id) {

		User engineer = userDetailsServiceImpl.findByIds(id);

		return convertToModel(feedbackRepository.findAllByEngineer(engineer));

	}

	public FeedbackResponseModel convertToModel(List<Feedback> entity) {

		List<String> discription = entity.stream().map(feedback -> feedback.getComment()).collect(Collectors.toList());
		double rating = entity.stream().mapToDouble(rate -> rate.getRating()).average().orElse(Float.NaN);
		return FeedbackResponseModel.builder().discription(discription).rating(rating).build();
	}
}
