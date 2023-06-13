package com.athmarine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.repository.BidderApproverRepository;

@Service
public class BidderApproverService {

	@Autowired
	BidderApproverRepository bidderApproverRepository;

	@Autowired
	UserDetailsServiceImpl userDetailsService;
}
