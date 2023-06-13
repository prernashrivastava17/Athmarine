package com.athmarine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.Turnover;
import com.athmarine.repository.TurnoverRepository;
import com.athmarine.request.TurnoverModel;

@Service
public class TurnoverService {

	@Autowired
	public TurnoverRepository turnoverRepository;

	public TurnoverModel saveTurnoverDetails(TurnoverModel turnoverModel) {

		return convertToModel(turnoverRepository.save(convertToEntity(turnoverModel)));

	}

	public Turnover convertToEntity(TurnoverModel model) {

		return Turnover.builder().id(model.getId()).docname(model.getDocname()).docUpload(model.getDocUpload())
				.turnover(model.getTurnover()).year(model.getYear()).currency(model.getCurrency()).build();

	}

	public TurnoverModel convertToModel(Turnover entity) {

		return TurnoverModel.builder().id(entity.getId()).docname(entity.getDocname()).docUpload(entity.getDocUpload())
				.turnover(entity.getTurnover()).year(entity.getYear()).status(entity.getStatus()).currency(entity.getCurrency()).build();

	}
}