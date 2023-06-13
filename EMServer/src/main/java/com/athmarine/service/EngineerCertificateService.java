package com.athmarine.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.EngineerCertificates;
import com.athmarine.repository.EngineerCertificateRepository;
import com.athmarine.request.EngineerCertificateModel;

@Service
public class EngineerCertificateService {

	@Autowired
	EngineerCertificateRepository certificateRepository;

	@Autowired
	EngineerEquimentService engineerEquimentService;

	public EngineerCertificateModel convertToModel(EngineerCertificates entity) {
		return EngineerCertificateModel.builder().id(entity.getId()).certificates(entity.getCertificates()).build();

	}

	public EngineerCertificates convertToEntity(EngineerCertificateModel model) {
		return EngineerCertificates.builder().id(model.getId()).certificates(model.getCertificates()).build();

	}

	public List<EngineerCertificates> getEngineerCertificate(int id) {
		return certificateRepository.findAllByEnggequip(engineerEquimentService.findByIds(id));
	}

}
