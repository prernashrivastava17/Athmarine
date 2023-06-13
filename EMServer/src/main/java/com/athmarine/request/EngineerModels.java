package com.athmarine.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonInclude(Include.NON_NULL)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EngineerModels {

	private String experienceYear;

	private String experienceMonth;

	private String certified;

	private EquipmentModel equipmentModel;

	private MasterEquipmentCategoryModel categoryModel;

	private List<EngineerCertificateModel> certificate;

	private MakeModel make;

	private String experience;

}
