package com.athmarine.request;

import com.athmarine.entity.EngineerCertificates;
import com.athmarine.entity.Equipment;
import com.athmarine.entity.MSTEquipmentCategory;
import com.athmarine.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EngineerEquipmentModels {

	private Integer id;

	private String experienceYear;

	private String experienceMonth;

	private String certified;

	User engineerId;

	Equipment equipment;

	MSTEquipmentCategory category;

	EngineerCertificates certificate;

	private int status;
}
