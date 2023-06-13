package com.athmarine.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.MSTPromotionStrategy;
import com.athmarine.exception.VendorCompanyNotFoundException;
import com.athmarine.repository.MSTPromotionStrategyRepository;
import com.athmarine.request.MSTPromotionModel;
import com.athmarine.resources.AppConstant;

@Service
public class MSTPromotionService {

	@Autowired
	MSTPromotionStrategyRepository mSTPromotionStrategyRepository;

	public MSTPromotionModel createPromotionStrategy(MSTPromotionModel mstPromotionModel) {

		return convertToModel(mSTPromotionStrategyRepository.save(convertToEntity(mstPromotionModel)));

	}

	public List<MSTPromotionModel> updaePromotionStrategy(List<MSTPromotionModel> mstPromotionModel) {

		List<MSTPromotionModel> masterPromotionAddInList = new ArrayList<>();

		for (MSTPromotionModel masterPromotion : mstPromotionModel) {

			MSTPromotionStrategy masterPromotionFindId = findMasterPromotionById(masterPromotion.getId());
			masterPromotionFindId.setId(masterPromotion.getId());
			masterPromotionFindId.setStartedDate(masterPromotion.getStartedDate());
			masterPromotionFindId.setEndDate(masterPromotion.getEndDate());
			masterPromotionFindId.setVoucher(masterPromotion.getVoucher());
			masterPromotionFindId.setVoucher(masterPromotion.getVoucher());
			masterPromotionFindId.setPayMoney(masterPromotion.getPayMoney());
			masterPromotionAddInList.add(convertToModel(mSTPromotionStrategyRepository.save(masterPromotionFindId)));
		}
		return masterPromotionAddInList;

	}

	public List<MSTPromotionModel> getAllPromotionData() {

		return mSTPromotionStrategyRepository.findAll().stream()
				.map(equipmentManufacturer -> convertToModel(equipmentManufacturer)).collect(Collectors.toList());

	}

	public MSTPromotionStrategy findMasterPromotionById(Integer id) {

		if (id == null || id == 0) {

			throw new VendorCompanyNotFoundException(AppConstant.ErrorTypes.VENDOR_COMPANY_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.VENDOR_COMPANY_EXIST_ERROR_CODE,
					AppConstant.ErrorMessages.VENDOR_COMPANY_NOT_EXIST_MESSAGE);
		} else {
			return mSTPromotionStrategyRepository.findById(id)
					.orElseThrow(() -> new VendorCompanyNotFoundException(
							AppConstant.ErrorTypes.MASTER_PROMOTION_NOT_EXIST_ERROR,
							AppConstant.ErrorCodes.VENDOR_COMPANY_EXIST_ERROR_CODE,
							AppConstant.ErrorMessages.MASTER_PROMOTION_NOT_EXIST_MESSAGE));
		}

	}

	public MSTPromotionStrategy convertToEntity(MSTPromotionModel model) {

		return MSTPromotionStrategy.builder().id(model.getId()).startedDate(model.getStartedDate())
				.endDate(model.getEndDate()).payMoney(model.getPayMoney()).voucher(model.getVoucher())
				.status(model.getStatus()).build();
	}

	public MSTPromotionModel convertToModel(MSTPromotionStrategy entity) {

		return MSTPromotionModel.builder().id(entity.getId()).startedDate(entity.getStartedDate())
				.endDate(entity.getEndDate()).payMoney(entity.getPayMoney()).voucher(entity.getVoucher())
				.status(entity.getStatus()).build();
	}

}
