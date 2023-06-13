package com.athmarine.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.Finance;
import com.athmarine.entity.Turnover;
import com.athmarine.exception.ResourceAlreadyExistsException;
import com.athmarine.exception.ResourceNotFoundException;
import com.athmarine.repository.FinanceRepository;
import com.athmarine.request.FinanceModel;
import com.athmarine.request.TurnoverModel;
import com.athmarine.request.UserFinanceTurnoverModel;
import com.athmarine.request.UserModel;
import com.athmarine.resources.AppConstant;

@Service
public class FinanceService {

	@Autowired
	FinanceRepository financeRepository;

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	MasterCountryService masterCountryService;

	@Autowired
	TurnoverService turnoverService;

	public FinanceModel saveFinanceDetails(UserFinanceTurnoverModel model) {
		String token = UUID.randomUUID().toString();
		UserModel userModel = model.getUserModel();
		FinanceModel financeModel = model.getFinanceModel();

		List<TurnoverModel> turnoverModels = model.getTurnoverModels();

		userModel.setPassword(token);
		userModel = userDetailsService.createUser(userModel);
		financeModel.setId(null);
		financeModel.setUserId(userModel.getId());
		financeModel.setTurnover(turnoverModels);
		return createFinance(financeModel);
	}

	public FinanceModel createFinance(FinanceModel financeModel) {
		if (financeModel == null) {
			throw new ResourceNotFoundException(AppConstant.ErrorTypes.RESOURCE_NOT_FOUND_ERROR,
					AppConstant.ErrorCodes.RESOURCE_NOT_FOUND, AppConstant.ErrorMessages.RESOURCE_NOT_FOUND_MESSAGE);
		}
		if (!checkAlreadyExists(financeModel.getUserId())) {
			return convertToModel(financeRepository.save(convertToEntity(financeModel)));

		} else
			throw new ResourceAlreadyExistsException(AppConstant.ErrorTypes.RESOURCE_EXIST_ERROR,
					AppConstant.ErrorCodes.RESOURCE_EXIST_ERROR_CODE, AppConstant.ErrorMessages.RESOURCE_EXIST_MESSAGE);

	}

	public FinanceModel getFinanceDetails(int id) {
		return convertToModel(financeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.ErrorTypes.RESOURCE_NOT_FOUND_ERROR,
						AppConstant.ErrorCodes.RESOURCE_NOT_FOUND,
						AppConstant.ErrorMessages.RESOURCE_NOT_FOUND_MESSAGE)));
	}

	public FinanceModel updateFinanceDetails(FinanceModel financeModel) {

		FinanceModel finanace = findById(financeModel.getId());

		financeModel.setId(finanace.getId());

		return convertToModel(financeRepository.save(convertToEntity(financeModel)));

	}

	public boolean checkAlreadyExists(int id) {
		return financeRepository.existsById(id);

	}

	public FinanceModel deleteFinanceDetails(Integer id) {

		FinanceModel finance = findById(id);

		finance.setStatus(AppConstant.DeleteStatus.STATUS_DELETE);

		return convertToModel(financeRepository.save(convertToEntity(finance)));

	}

	private FinanceModel findById(Integer id) {

		if (id == null) {
			throw new ResourceNotFoundException(AppConstant.ErrorTypes.ID_NULL_EXIST_ERROR,
					AppConstant.ErrorCodes.ID_NULL_ERROR_CODE, AppConstant.ErrorMessages.ID_EMPTY_MESSAGE);
		} else {

			return convertToModel(financeRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException(AppConstant.ErrorTypes.RESOURCE_NOT_FOUND_ERROR,
							AppConstant.ErrorCodes.RESOURCE_NOT_FOUND,
							AppConstant.ErrorMessages.RESOURCE_NOT_FOUND_MESSAGE)));
		}
	}

	private Finance convertToEntity(FinanceModel model) {

		List<Turnover> turnoverList = model.getTurnover().stream()
				.map(turnover -> turnoverService.convertToEntity(turnover)).collect(Collectors.toList());

		return Finance.builder().id(model.getId()).user(userDetailsService.findUserEntityById(model.getUserId()))
				.turnover(turnoverList).currency(model.getCurrency()).status(model.getStatus()).build();

	}

	private FinanceModel convertToModel(Finance entity) {

		List<TurnoverModel> turnoverList = entity.getTurnover().stream()
				.map(turnover -> turnoverService.convertToModel(turnover)).collect(Collectors.toList());

		return FinanceModel.builder().id(entity.getId()).userId(entity.getId()).turnover(turnoverList)
				.currency(entity.getCurrency()).status(entity.getStatus()).build();

	}
}