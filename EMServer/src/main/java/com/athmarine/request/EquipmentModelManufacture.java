package com.athmarine.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentModelManufacture {

	private Integer id;

	private String name;

	private String code;

	private String isVerified;

	private Integer status;

	private List<ManufacturerModule> manufacturerModule;

}
