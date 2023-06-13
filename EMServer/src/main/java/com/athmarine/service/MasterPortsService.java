package com.athmarine.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.MSTCountry;
import com.athmarine.entity.MSTPorts;
import com.athmarine.entity.MSTState;
import com.athmarine.exception.MasterPortsServiceNotFoundException;
import com.athmarine.repository.MasterCountryRepository;
import com.athmarine.repository.MasterPortsRepository;
import com.athmarine.repository.MasterStateRepository;
import com.athmarine.request.MasterPortModel;
import com.athmarine.request.MasterPortsModel;
import com.athmarine.request.VendorServiceMasterPortsModel;
import com.athmarine.resources.AppConstant;

@Service
public class MasterPortsService {

	@Autowired
	MasterPortsRepository masterPortsRepository;

	@Autowired
	MasterStateRepository masterStateRepository;

	@Autowired
	MasterCountryRepository masterCountryRepository;

	@Autowired
	MasterStateService masterStateService;

	@Autowired
	MasterCountryService masterCountryService;

	public MSTPorts createMasterOtherPort(MasterPortModel port) {

		return masterPortsRepository.save(convertToEntity(port));
	}

	public List<MasterPortsModel> getAllPortsByCountryId(@NotNull Integer id) {

		MSTCountry mstCountry = masterCountryRepository.findById(id)
				.orElseThrow(() -> new MasterPortsServiceNotFoundException(
						AppConstant.ErrorTypes.MASTER_STATE_EXIST_ERROR, AppConstant.ErrorCodes.MASTER_STATE_ERROR_CODE,
						AppConstant.ErrorMessages.MASTER_STATE_NOT_EXTIS_MESSAGE));

		List<MasterPortsModel> other = mstCountry.getMSTPorts().stream().filter(entity -> entity.isVerified())
				.map(country -> convertToModel(country)).distinct().collect(Collectors.toList());
		MasterPortsModel masterPortsModel = new MasterPortsModel();
		masterPortsModel.setName("Others");
		masterPortsModel.setVerified(true);
		other.add(masterPortsModel);
		return other;
	}

	public List<MasterPortModel> getAllPortsNonVerified() {

		return masterPortsRepository.findAll().stream().filter(entity -> !entity.isVerified())
				.map(country -> convertToModels(country)).collect(Collectors.toList());
	}

	public List<MasterPortsModel> createMasterPort(List<MasterPortsModel> masterPortsModel) {

		List<MasterPortsModel> masterPortResponse = new ArrayList<MasterPortsModel>();

		for (Integer i = 0; i < masterPortsModel.size(); i++) {

			MSTState state = null;

			if (masterPortsModel.get(i).getMstateStateId() != null) {

				state = masterStateService.findByIds(masterPortsModel.get(i).getMstateStateId().getId());

			}

			MasterPortsModel findIdMasterPort = convertToModel(
					masterPortsRepository.save(convertToEntity(masterPortsModel.get(i), state)));

			masterPortResponse.add(findIdMasterPort);
		}

		return masterPortResponse;
	}

	public List<MasterPortModel> verifiedMasterPort() {

		return masterPortsRepository.findAll().stream().filter(entity -> entity.isVerified())
				.map(country -> convertToModels(country)).collect(Collectors.toList());

	}
	
	public MasterPortsModel updateMasterPort(MasterPortsModel masterPortsModel) {

		MasterPortsModel masterPorts = findById(masterPortsModel.getId());
		masterPortsModel.setId(masterPorts.getId());

		MSTState state = null;

		if (masterPortsModel.getMstateStateId() != null) {

			state = masterStateService.findByIds(masterPortsModel.getMstateStateId().getId());
		}
		masterPortsModel.setVerified(true);
		return convertToModel(masterPortsRepository.save(convertToEntityVerify(masterPortsModel, state)));
	}

	public MasterPortsModel deleteMasterPort(@NotBlank int id) {

		MasterPortsModel masterPortsModel = findById(id);

		masterPortsModel.setStatus(AppConstant.DeleteStatus.STATUS_DELETE);

		MSTState state = null;

		if (masterPortsModel.getMstateStateId() != null) {

			state = masterStateService.findByIds(masterPortsModel.getMstateStateId().getId());
		}

		return convertToModel(masterPortsRepository.save(convertToEntity(masterPortsModel, state)));
	}

	public MasterPortsModel findById(Integer id) {

		if (id == null) {
			throw new MasterPortsServiceNotFoundException(AppConstant.ErrorTypes.MASTER_COUNTRY_EXIST_ERROR,
					AppConstant.ErrorCodes.MASTER_COUNTRY_ERROR_CODE, AppConstant.ErrorMessages.ID_EMPTY_MESSAGE);
		} else {
			return convertToModel(masterPortsRepository.findById(id)
					.orElseThrow(() -> new MasterPortsServiceNotFoundException(
							AppConstant.ErrorTypes.MASTER_COUNTRY_EXIST_ERROR,
							AppConstant.ErrorCodes.MASTER_COUNTRY_ERROR_CODE,
							AppConstant.ErrorMessages.MASTER_COUNTRY_NOT_EXTIS_MESSAGE)));
		}

	}

	public MSTPorts findByIds(Integer id) {
		if (id == null) {

			throw new MasterPortsServiceNotFoundException(AppConstant.ErrorTypes.MASTER_STATE_EXIST_ERROR,
					AppConstant.ErrorCodes.MASTER_STATE_ERROR_CODE, AppConstant.ErrorMessages.ID_EMPTY_MESSAGE);

		}
		return masterPortsRepository.findById(id)
				.orElseThrow(() -> new MasterPortsServiceNotFoundException(
						AppConstant.ErrorTypes.MASTER_STATE_EXIST_ERROR, AppConstant.ErrorCodes.MASTER_STATE_ERROR_CODE,
						AppConstant.ErrorMessages.MASTER_PORTS_NOT_EXTIS_MESSAGE));
	}

	public MSTPorts convertToEntity(MasterPortModel model) {
		MSTCountry countryModel = masterCountryService.findByCountryId(model.getCountryId());

		return MSTPorts.builder().id(model.getId()).name(model.getName()).status(model.getStatus())
				.country(countryModel).isVerified(model.isVerified()).build();

	}

	public MSTPorts convertToEntity(MasterPortsModel model, MSTState mstState) {

		return MSTPorts.builder().id(model.getId()).name(model.getName()).status(model.getStatus())
				.isVerified(false).build();

	}

	public MSTPorts convertToEntity(VendorServiceMasterPortsModel model, MSTState mstState) {

		return MSTPorts.builder().id(model.getId()).name(model.getName()).status(model.getStatus())
				.isVerified(false).build();

	}

	public MSTPorts convertToEntityVerify(MasterPortsModel model, MSTState mstState) {

		return MSTPorts.builder().id(model.getId()).name(model.getName()).status(model.getStatus())
				.isVerified(model.isVerified()).build();

	}

	public MasterPortsModel convertToModel(MSTPorts entity) {

		return MasterPortsModel.builder().id(entity.getId()).name(entity.getName()).status(entity.getStatus())

				.countryModel(masterCountryService.findById(entity.getCountry().getId()))
				.isVerified(entity.isVerified()).build();

	}

	public MasterPortModel convertToModels(MSTPorts entity) {

		return MasterPortModel.builder().id(entity.getId()).name(entity.getName()).status(entity.getStatus())
				.isVerified(entity.isVerified()).build();

	}

	public VendorServiceMasterPortsModel convertToPortsModels(MSTPorts entity) {

		return VendorServiceMasterPortsModel.builder().id(entity.getId()).name(entity.getName())

				.status(entity.getStatus()).isVerified(entity.isVerified())
				.countryModel(masterCountryService.findById(entity.getCountry().getId()))
				.build();

	}

	public MasterPortModel getVendorPortByVendorServiceId(Integer vendorServiceId) {
		MSTPorts masterPort = findByIds(vendorServiceId);
		return convertToModels(masterPort);
	}

}
