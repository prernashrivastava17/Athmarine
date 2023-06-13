package com.athmarine.service;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.VendorApproversPriceRange;
import com.athmarine.exception.ResourceAlreadyExistsException;
import com.athmarine.exception.RoleMismatchException;
import com.athmarine.exception.UserNotFoundException;
import com.athmarine.repository.VendorApproversPriceRangeRepository;
import com.athmarine.request.UserModel;
import com.athmarine.request.VendorApproversPriceRangeModel;
import com.athmarine.resources.AppConstant;

@Service
public class VendorApproversPriceRangeService {

	@Autowired
	VendorApproversPriceRangeRepository vendorApproversPriceRangeRepository;

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	public VendorApproversPriceRangeModel saveVendorApproversPriceRangeDetails(
			@Valid VendorApproversPriceRangeModel vendorApproversPriceRangeModel) {

		UserModel vendorApproversPriceRange = userDetailsService
				.findById(vendorApproversPriceRangeModel.getApprovedId());
		if (vendorApproversPriceRange.getRoleModel() != null) {
			if (vendorApproversPriceRangeModel.getMaxValue() > vendorApproversPriceRangeModel.getMinValue()) {
				if (!checkAlreadyExists(vendorApproversPriceRangeModel.getApprovedId())) {
					return convertToModel(
							vendorApproversPriceRangeRepository.save(convertToEntity(vendorApproversPriceRangeModel)));
				} else
					throw new ResourceAlreadyExistsException(
							AppConstant.ErrorTypes.VENDOR_APPROVER_PRICE_RANGE_ALREADY_EXIST_ERROR,
							AppConstant.ErrorCodes.VENDOR_APPROVER_PRICE_RANGE_ALREADY_ERROR_CODE,
							AppConstant.ErrorMessages.VENDOR_APPROVER_PRICE_RANGE_ALREADY_EXIST);

			}

			else
				throw new ResourceAlreadyExistsException(
						AppConstant.ErrorTypes.VENDOR_APPROVERS_PRICE_RANGE_EXIST_ERROR,
						AppConstant.ErrorCodes.VENDOR_APPROVERS_PRICE_RANGE_ERROR_CODE,
						AppConstant.ErrorMessages.VENDOR_APPROVERS_PRICE_RANGE_MAX_MESSAGE);

		} else {
			throw new RoleMismatchException(AppConstant.ErrorTypes.ROLE_MISMATCH_ERROR,
					AppConstant.ErrorCodes.ROLE_MISMATCH_ERROR_CODE, AppConstant.ErrorMessages.ROLE_MISMATCH_MESSAGE);
		}
	}

	public VendorApproversPriceRangeModel getVendorApproversPriceRangeDetails(@NotNull int id) {
		return convertToModel(vendorApproversPriceRangeRepository.findById(id).orElseThrow(
				() -> new UserNotFoundException(AppConstant.ErrorTypes.VENDOR_APPROVERS_PRICE_RANGE_EXIST_ERROR,
						AppConstant.ErrorCodes.VENDOR_APPROVERS_PRICE_RANGE_ERROR_CODE,
						AppConstant.ErrorMessages.VENDOR_APPROVERS_PRICE_RANGE_NOT_EXTIS_MESSAGE)));
	}

	public VendorApproversPriceRangeModel updateVendorApproversPriceRange(
			VendorApproversPriceRangeModel vendorApproversPriceRangeModel) {

		VendorApproversPriceRangeModel vendorApproversPrice = findById(vendorApproversPriceRangeModel.getId());

		vendorApproversPriceRangeModel.setId(vendorApproversPrice.getId());

		if (vendorApproversPriceRangeModel.getMaxValue() > vendorApproversPriceRangeModel.getMinValue()) {
			return convertToModel(
					vendorApproversPriceRangeRepository.save(convertToEntity(vendorApproversPriceRangeModel)));

		} else
			throw new ResourceAlreadyExistsException(AppConstant.ErrorTypes.VENDOR_APPROVERS_PRICE_RANGE_EXIST_ERROR,
					AppConstant.ErrorCodes.VENDOR_APPROVERS_PRICE_RANGE_ERROR_CODE,
					AppConstant.ErrorMessages.VENDOR_APPROVERS_PRICE_RANGE_MAX_MESSAGE);
	}

	public VendorApproversPriceRangeModel deleteVendorApproversPriceRangeDetails(@NotBlank int id) {

		VendorApproversPriceRangeModel vendorApproversPriceRange = findById(id);

		vendorApproversPriceRange.setStatus(AppConstant.DeleteStatus.STATUS_DELETE);

		return convertToModel(vendorApproversPriceRangeRepository.save(convertToEntity(vendorApproversPriceRange)));
	}

	public VendorApproversPriceRangeModel findById(@NotBlank int id) {
		return convertToModel(vendorApproversPriceRangeRepository.findById(id).orElseThrow(
				() -> new UserNotFoundException(AppConstant.ErrorTypes.VENDOR_APPROVERS_PRICE_RANGE_EXIST_ERROR,
						AppConstant.ErrorCodes.VENDOR_APPROVERS_PRICE_RANGE_ERROR_CODE,
						AppConstant.ErrorMessages.VENDOR_APPROVERS_PRICE_RANGE_NOT_EXTIS_MESSAGE)));
	}

	public VendorApproversPriceRange findByIds(@NotBlank int id) {
		return vendorApproversPriceRangeRepository.findById(id).orElseThrow(
				() -> new UserNotFoundException(AppConstant.ErrorTypes.VENDOR_APPROVERS_PRICE_RANGE_EXIST_ERROR,
						AppConstant.ErrorCodes.VENDOR_APPROVERS_PRICE_RANGE_ERROR_CODE,
						AppConstant.ErrorMessages.VENDOR_APPROVERS_PRICE_RANGE_NOT_EXTIS_MESSAGE));
	}

	public boolean checkAlreadyExists(int id) {
		return vendorApproversPriceRangeRepository.existsById(id);

	}

	private VendorApproversPriceRange convertToEntity(VendorApproversPriceRangeModel model) {

		return VendorApproversPriceRange.builder().id(model.getId())
				.approverId(userDetailsService.findUserEntityById(model.getApprovedId())).minValue(model.getMinValue())
				.maxValue(model.getMaxValue()).status(model.getStatus()).build();

	}

	private VendorApproversPriceRangeModel convertToModel(VendorApproversPriceRange entity) {

		return VendorApproversPriceRangeModel.builder().id(entity.getId())
				.approvedId(userDetailsService.convertToModel(entity.getApproverId()).getId())
				.minValue(entity.getMinValue()).maxValue(entity.getMaxValue()).status(entity.getStatus()).build();

	}

}
