package com.athmarine.response;

import java.util.Date;
import java.util.List;

import com.athmarine.entity.ServiceRequestStatus;
import com.athmarine.request.*;
import com.athmarine.resources.ServiceTimeCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ServiceRequestResponseModel {

	private Integer id;

	private String requestUID;

	private UserModel userModel;

	private String requestStatus;

	private Integer bidsSubmitted;

	private VesselResponseModel vessel;

	private MasterCountryModel country;

	private MasterPortModel port;

	private String location;

	private Date ETA;

	private Date ETD;

	private Date serviceRequestOn;

	private String preferredCurrency;

	private MasterEquipmentCategoryModel category;

	private String equipmentName;

	private String manufacturer;

	private String model;

	private String issueType;

	private int quotationTime;

	private ServiceTimeCategory serviceTimeCategory;

	private long bidRemainingTime;

	private List<ServiceRequestDocumentModel> serviceRequestDocumentModels;

	private String description;


	/*private int status;


	private List<String> documents;



	private MasterStateModel states;


	private Integer requestorComapny;

	private Integer requesterId;

	private List<String> email;

	private String service_required_timing;


	private List<UserListResponseModel> vendorEngineer;

	private UserListResponseModel finalVendorComapny;
	
	private String serviceDescription;

	private int QuotationTime;
	
	private int vendorInterested;*/

}
