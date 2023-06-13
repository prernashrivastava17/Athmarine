package com.athmarine.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.Make;
import com.athmarine.repository.MakeRepository;
import com.athmarine.request.MakeModel;

@Service
public class MakeService {

	@Autowired
	public MakeRepository makeRepository;
	
	public Make createMake(MakeModel model)
	{
		return makeRepository.save(convertToEntity(model));
	}

	public List<MakeModel> getAllMake() {
		List<MakeModel> otherList= makeRepository.findAll().stream()
				.filter(entity -> entity.isVerified())
				.sorted(Comparator.comparing(Make::getName))
		.map(make -> convertToModel(make)).collect(Collectors.toList());
		MakeModel objectOfMakeModel= new MakeModel();
		objectOfMakeModel.setName("Others");
		objectOfMakeModel.setVerified(true);
		otherList.add(objectOfMakeModel);
		return otherList;
		

	}

	public Make convertToEntity(MakeModel model) {

		return Make.builder().id(model.getId()).name(model.getName()).isVerified(model.isVerified()).status(model.getStatus()).build();

	}

	public MakeModel convertToModel(Make entity) {

		return MakeModel.builder().id(entity.getId()).name(entity.getName()).status(entity.getStatus())
				.isVerified(entity.isVerified())
				.build();

	}

}
