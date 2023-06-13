package com.athmarine.response;

import java.util.Date;
import java.util.List;

import com.athmarine.entity.Role;
import com.athmarine.request.*;
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
public class HocResponseModel {
	
	private Date poReceivedDate;
	
	private Date serviceDate;
	
	private Integer poId;
	
	private String customerName;
	
	private String vasselName;
	
	private String EquipmentCategory;
	
	private Integer serviceRequestId;
	
	private Integer bidId;
	
	private Integer poValue;
	
	private String bidUid;
	
	private String serviceRequestUid;
	
	private String bidValue;
	
	private String portName;
	
	private String interestShownBy;
	
	private String poUid;
	
	private String serviceReport;
	
	private Date invoicePaidAt;
	
	private String invoiceValue;
	
	private Integer invoiceId;
	
	private Date invoiceRaisedDate;
	
	private String invoiceStatus;
	
	private Date completedJobDate;

	private Integer userId;

	private String userUID;

	private String userName;

	private String userEmail;

	private String userPhone;

	private List<RoleModels> userRoleAssigned;

	private Boolean userStatus;

	private String userAccessType;

	private String type;
	
	
	
	
//	private ServiceRequestResponseModel serviceRequestResponseModel;
//	
//	private PurchaseOrderResponse purchaseOrderResponse;
//	
//	private BidsModel bidsModel;
//	
//	private InvoiceResponseModel invoiceResponseModel;

}
