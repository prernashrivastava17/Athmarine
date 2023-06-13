package com.athmarine.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.CountryDetails;
import com.athmarine.repository.CountryDetailsRepository;

@Service
public class CountryDetailsService {

	@Autowired
	CountryDetailsRepository countryDetailsRepository;

	public List<CountryDetails> getAllCountryDetails() {
		return countryDetailsRepository.findAllByOrderByCallingCodeAsc();
	}
}
