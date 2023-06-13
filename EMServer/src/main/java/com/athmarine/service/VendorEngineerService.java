package com.athmarine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class VendorEngineerService {

	@Autowired

	MasterStateService masterStateService;

	@Autowired

	UserDetailsServiceImpl userDetailsService;
}
