package com.athmarine.request;

import java.util.List;

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
public class EngineerEquipmentsResponseModel {

	private Integer id;

	private String experienceYear;

	private String experienceMonth;

	private String certified;

	private Integer status;

	private List<EngineerCertificateModel> engineerCertificates;

	private Integer equimentId;

	private Integer categoryId;

	private UserModel engineerId;

}
