package com.athmarine.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.athmarine.exception.AdminNotFoundException;
import com.athmarine.request.MSTPromotionModel;
import com.athmarine.resources.RestMappingConstants;
import com.athmarine.response.BaseApiResponse;
import com.athmarine.response.ResponseBuilder;
import com.athmarine.service.MSTPromotionService;

@RestController
@RequestMapping(path = RestMappingConstants.MasterPromotionStrategyInterfaceUri.MASTER_PROMOTION_BASE_URI)
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MasterPromotionStrategyController {

	@Autowired
	MSTPromotionService masterPromotionService;

	@PostMapping(RestMappingConstants.MasterPromotionStrategyInterfaceUri.MASTER_PROMOTION_ADD_URI)
	public ResponseEntity<?> createPromotionStrategy(@RequestBody MSTPromotionModel model) {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(masterPromotionService.createPromotionStrategy(model));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);

	}

	@PutMapping(RestMappingConstants.MasterPromotionStrategyInterfaceUri.MASTER_PROMOTION_UPDATE_URI)
	public ResponseEntity<?> updaePromotionStrategy(@RequestBody List<MSTPromotionModel> model)
			throws AdminNotFoundException {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(masterPromotionService.updaePromotionStrategy(model));
		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

	@GetMapping(RestMappingConstants.MasterPromotionStrategyInterfaceUri.MASTER_PROMOTION_GET_ALL_URI)
	public ResponseEntity<?> getAllMasterPromotion() {

		BaseApiResponse baseApiResponse = ResponseBuilder
				.getSuccessResponse(masterPromotionService.getAllPromotionData());

		return new ResponseEntity<BaseApiResponse>(baseApiResponse, HttpStatus.OK);
	}

}
