package com.athmarine.request;

import java.util.Date;
import java.util.List;

import com.athmarine.entity.ServiceRequestStatus;
import com.athmarine.entity.Vessel;
import com.athmarine.resources.ServiceTimeCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ServiceRequestModel {

	private Integer id;

	private Integer requesterId;

	private String requestUID;

	private ServiceRequestStatus requestStatus;

	private VesselRequestModel vessel;

	private MasterCountryModel country;

	private MasterPortModel port;

	private String locationType;

	private Date serviceRequestOn;

	private Date ETA;

	private Date ETD;

	private String preferredCurrency;

	private MasterEquipmentCategoryModel equipmentCategory;

	private String equipmentName;

	private String manufacturer;

	private String model;

	private String issueType;

	private int quotationTime;

	private ServiceTimeCategory serviceTimeCategory;

	private String description;

	private List<ServiceRequestDocumentModel> serviceRequestDocumentModels;

	/*

	private List<String> documents;

	private MasterStateModel states;



	 private Integer requestorComapny;


	private List<String> email;

	private String service_required_timing;

	private String currency_Acceptable;



	private String uniqueIdServiceRequestor;

	private ServiceRequestStatus requestStatus;

	private String serviceDescription;

	private int QuotationTime;*/



}
