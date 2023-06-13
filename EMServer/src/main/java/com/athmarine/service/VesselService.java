package com.athmarine.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.Vessel;
import com.athmarine.exception.ResourceNotFoundException;
import com.athmarine.repository.VesselRepository;
import com.athmarine.request.VesselRequestModel;
import com.athmarine.resources.AppConstant;
import com.athmarine.response.VesselResponseModel;

@Service
public class VesselService {

	@Autowired
	VesselRepository vesselRepository;

	public Boolean createVessel(List<VesselRequestModel> modelList) {

		boolean succes = false;
		List<Vessel> vessel = modelList.stream().map(model -> convertToEntity(model)).collect(Collectors.toList());
		try {
			vesselRepository.saveAll(vessel);
			succes = true;
		} catch (Exception e) {
			succes = false;
		}
		return succes;
	}

	public List<Vessel> getAllVessel(Integer IMO, String shipName) {

		return vesselRepository.findAllByShipnameAndIMO(shipName, IMO);
	}

	public Vessel findByIds(Integer id) {

		if (id == null) {
			throw new ResourceNotFoundException(AppConstant.ErrorTypes.ID_NULL_EXIST_ERROR,
					AppConstant.ErrorCodes.ID_NULL_ERROR_CODE,
					"Available " + AppConstant.ErrorMessages.ID_NULL_NOT_EXIST);
		} else {
			return vesselRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException(AppConstant.ErrorTypes.AVAILABLE_ON_ERROR,
							AppConstant.ErrorCodes.AVAILABLE_ON_ERROR_CODE,
							AppConstant.ErrorMessages.AVAILABLE_ON_EXIST_ERROR));
		}
	}

	public List<VesselRequestModel> getAllVessel() {
		return vesselRepository.findAll().stream().map(vessel -> convertToRequestModel(vessel))
				.collect(Collectors.toList());
	}

	public Vessel convertToEntity(VesselRequestModel model) {

		return vesselRepository.save(Vessel.builder().shipname(model.getVesselName()).typeName(model.getVesselType())
				.IMO(model.getIMO()).build());

		/*return Vessel.builder().id(model.getId()).status(model.getStatus()).callSign(model.getCallSign())
				.country(model.getCountry()).DWT(model.getDWT()).flag(model.getFlag()).IMO(model.getIMO())
				.MMSI(model.getMMSI()).MT_URL(model.getMT_URL()).status(model.getStatus()).shipname(model.getShipname())
				.yearOfBuilt(model.getYearOfBuilt()).typeName(model.getTypeName()).shipId(model.getShipId()).build();*/

	}

	public VesselRequestModel convertToRequestModel(Vessel entity) {

		/*return VesselRequestModel.builder().id(entity.getId()).status(entity.getStatus()).callSign(entity.getCallSign())
				.country(entity.getCountry()).DWT(entity.getDWT()).flag(entity.getFlag()).IMO(entity.getIMO())
				.MMSI(entity.getMMSI()).MT_URL(entity.getMT_URL()).status(entity.getStatus())
				.shipname(entity.getShipname()).yearOfBuilt(entity.getYearOfBuilt()).typeName(entity.getTypeName())
				.shipId(entity.getShipId()).build();*/

		return null;
	}

	public VesselResponseModel convertToResponseModel(Vessel entity) {

		return VesselResponseModel.builder().id(entity.getId()).IMO(entity.getIMO())
				.vesselName(entity.getShipname()).vesselType(entity.getTypeName()).build();
	}

}
