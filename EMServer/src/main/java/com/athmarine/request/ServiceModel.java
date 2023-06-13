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
public class ServiceModel {

	private Integer id;

	//private String equipmentExperience;

	private int status;
	
	//private MasterEquipmentCategoryModel masterEquipmentCategoryModel;

	// private ManufacturerModule manufacturerModule;

	private List<EquipmentModel> equipmentModel;

	// private List<MasterStateModel> stateId;

	// private MasterCountryModel countryId;

	private UserModel companyId;

	//private DepartmentNameModel departmentId;

	private List<MasterPortsModel> portId;

   // private MasterCountryModel countryId;

}
