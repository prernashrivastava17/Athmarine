package com.athmarine.resources;

public interface RestMappingConstants {
	public interface OTPRequestUri {
		static final String OTP_BASE_URI = "/OTP";
		static final String GET_OTP_EMAIL = "/getEmailOTP";
		static final String VALIDATE_OTP_SMS = "/validateOTPSms";
		static final String VALIDATE_OTP_EMAIL = "/validateOTPEmail";
		static final String GET_OTP_SMS = "/getSmsOTP";

	}

	public interface RoleRequestUri {
		static final String ROLE_BASE_URI = "/role";
		static final String ADD_ROLE_URI = "/add";
		static final String UPDATE_ROLE_URI = "/update";
		static final String GET_ROLE_URI = "/get";
		static final String DELETE_ROLE_URI = "/delete";

	}

	public interface UserInterfaceUri {
		static final String USER_BASE_URI = "/user";
		static final String USER_ADD_URI = "/add";
		static final String USER_TEST_URI = "/email";
		static final String UPDATE_USER_URI = "/update";
		static final String GET_USER_URI = "/get";
		static final String USER_DELETE_URI = "/delete";
		static final String GET_USER_BY_UID_URI = "/getUserByUid";
		static final String GET_USER_BY_PHONE_URI = "/getUserByPhone";
		static final String GET_ALL_USER_LIST_URI = "/getAllUserList";
		static final String GET_ALL_USER_BY_EMAIL_URI = "/getUserByEmail";
		static final String GET_ALL_USER_EMAIL_URI = "/getUserEmail";
		static final String GET_ALL_USER_COMPANY_URI = "/getUserCompany";
		static final String UPDATE_USER_PHONE_NUMBER_URI = "/updatePhoneNumber";
		static final String UPDATE_REQUSTOR_URI = "/updateRequstorProfile";
		static final String UPDATE_USER_EMAIL_URI = "/updateEmail";
		static final String GET_USER_BY_ENCODED_URL_URI = "/getUserByEncodedUrl";
		static final String DELETE_USER_BY_EMAIL_URI = "/deleteUserByEmail";
		static final String VALIDATE_EMAIL_AND_PHONE_URI = "/validateEmailAndPhone";
		static final String EMAIL_VERIFICATION_URI = "/emailVerification";

	}

	public interface ModuleInterfaceUri {
		static final String MODULE_BASE_URI = "/module";
		static final String MODULE_ADD_URI = "/add";
		static final String MODULE_UPDATE__URI = "/update";
		static final String MODULE_DELETE_URI = "/delete";
		static final String GET_MODULE_URI = "/get";
	}

	public interface VendorCompanyInterfaceUri {

		static final String VENDOR_COMPANY_BASE_URI = "/vendor";
		static final String VENDOR_COMPANY_ADD_URI = "/add";
		static final String GET_VENDOR_COMPANY_URI = "/get";
		static final String GET_VENDOR_COMPANY_URI_COUNTRY = "/getCompanyCountry";
		static final String GET_VENDOR_COMPANY_BUISNESS_INFORMATION_URI = "/getBuisnessInfo";
		static final String VENDOR_COMPANY_DELETE_URI = "/delete";
		static final String VENDOR_COMPANY_UPDATE_BUISNESS_INFORMATION__URI = "/updateBuisnessInformation";
		static final String VENDOR_COMPANY_UPDATE__URI = "/update";
		static final String GENERATE_REFERRAL_CODE_URI = "/generateReferralCode";
		static final String VALIDATE_REFERRAL_CODE_URI = "/validateReferralCode";
		static final String GET_VENDOR_COMPANY_FINANCE = "/getFinaceEmailPhone";

	}

	public interface VendorEngineerInterfaceUri {
		static final String VENDOR_COMPANY_BASE_URI = "/vendor";

		static final String VENDOR_COMPANY_ENGINEER_CREATE_URI = "/createVendorEngineer";
		static final String VENDOR_COMPANY_ENGINEER_UPDATE_URI = "/updateVendorEngineer";
		static final String VENDOR_COMPANY_ENGINEER_GET_URI = "/getVendorEngineer";
		static final String VENDOR_COMPANY_ENGINEER_DELETE_URI = "/deleteVendorEngineer";

		static final String VENDOR_COMPANY_ENGINEER_URI = "/createVendorEngineer";

	}

	public interface VendorBidderInterfaceUri {
		static final String VENDOR_COMPANY_BASE_URI = "/vendor";
		static final String VENDOR_COMPANY_CREATE_BIDDER_URI = "/createVendorBidder";
		static final String VENDOR_COMPANY_BIDDER_UPDATE_ALL_URI = "/updateAllVendorBidder";
		static final String VENDOR_COMPANY_BIDDER_UPDATE_URI = "/updateVendorBidder";
		static final String VENDOR_COMPANY_BIDDER_DELETE_URI = "/deleteVendorBidder";
		static final String VENDOR_COMPANY_BIDDER_GET_URI = "/getVendorBidder";
		static final String VENDOR_COMPANY_BIDDER_GET_ALL_URI = "/getAllVendorBidder";
		static final String VENDOR_COMPANY_BIDDER_GET_ALL_BY_APPROVER_ID_URI = "/getAllVendorBidderByApproverId";
		static final String VENDOR_COMPANY_ADD_BIDDER_URI = "/createVendorBidder";
		static final String GET_ALL_COMPANY_VENDOR_BIDDER_URI = "/getAllVendorComapnyBidder";

	}

	public interface VendorApproverInterfaceUri {
		static final String VENDOR_COMPANY_BASE_URI = "/vendor";
		static final String VENDOR_APPROVER_CREATE_URI = "/createVendorApprover";
		static final String VENDOR_APPROVER_UPDATE_URI = "/updateVendorApprover";
		static final String VENDOR_APPROVER_GET_URI = "/getVendorApprover";
		static final String VENDOR_APPROVER_DELETE_URI = "/deleteVendorApprover";
		static final String VENDOR_GET_COUNTS = "/getCounts";
		static final String VENDOR_GET_APPROVED_BIDS = "/getAllApprovedBids";
		static final String VENDOR_GET_TO_APPROVED_BIDS = "/getAllToApproveBids";
		static final String VENDOR_APPROVE_OR_DECLINE_BID = "/approveOrDeclineBid";
		static final String VENDOR_APPROVE_UPDATE_BID = "/updateBidByApprover";

	}

	public interface FinanceInterfaceUri {
		static final String FINANCE_BASE_URI = "/finance";
		static final String FINANCE_ADD_URI = "/add";
		static final String FINANCE_GET_URI = "/get";
		static final String FINANCE_UPDATE_URI = "/update";
		static final String FINANCE_DELETE_URI = "/delete";
		static final String FINANCE_GET_COUNTS = "/getCountsByStatus";
		static final String GET_INVOICES_RAISED = "/getInvoicesByStatus";
		static final String GET_PAYABLES = "/getPayableInvoices";
	}

	public interface FeedBackInterfaceUri {
		static final String FEEDBACK_BASE_URI = "/finance";
		static final String FEEDBACK_GET_VENDOR_COMAPANY_URI = "/getAllFeedBack";
		static final String FEEDBACK_GET_URI = "/get";
		static final String FEEDBACK_UPDATE_URI = "/update";
		static final String FEEDBACK_DELETE_URI = "/delete";
	}

	public interface LoginInterfaceUri {
		static final String LOGIN_BASE_URI = "/login";
		static final String RESET_PASSWORD_URI = "/resetPassword";
		static final String RESET_PASSWORD_VERIFY_TOKEN = "/verifyToken";
		static final String UPDATE_PASSWORD = "/updatePassword";
		static final String LOGOUT = "/logout";

	}

	public interface CurrencyApiInterfaceUri {

		static final String CURRENCY_API_BASE_URI = "/currency";
		static final String CURRENCY_API_GET_URI = "/api/get";

	}

	public interface CustomerCompanyInterfaceUri {
		static final String CUSTOMER_COMPANY_BASE_URI = "/customer";
		static final String CUSTOMER_COMPANY_ADD_URI = "/add";
		static final String CUSTOMER_COMPANY_GET_URI = "/get";
		static final String CUSTOMER_COMPANY_GET_BUSINESS_INFO_URI = "/getBuisnessInfo";
		static final String CUSTOMER_COMPANY_UPDATE_URI = "/update";
		static final String CUSTOMER_COMPANY_DELETE_URI = "/delete";
		static final String CUSTOMER_FINANCE_ADD_URI = "/addFinance";
		static final String CUSTOMER_HEAD_ADD_URI = "/addHead";
		static final String CUSTOMER_HEAD_GET_ALL_APPROVER_URI = "/getAllApprover";
		static final String CUSTOMER_HEAD_GET_ALL_HEAD_URI = "/getAllHead";
		static final String CUSTOMER_VALIDATE_REFERRAL_CODE_URI = "/validateReferralCode";

	}

	public interface ContactUsInterfaceUri {
		static final String CONTACT_US_BASE_URI = "/contact";
		static final String CONTACT_US_ADD_URI = "/addContactUs";

	}

	public interface CustomerFinanceInterfaceUri {
		static final String CUSTOMER_FINANCE_BASE_URI = "/customer";
		static final String CUSTOMER_FINANCE_ADD_URI = "/addFinance";
		static final String CUSTOMER_COMPANY_GET_URI = "/getByCompanyId";
		static final String CUSTOMER_COMPANY_UPDATE_URI = "/updateFinance";
		static final String CUSTOMER_COMPANY_DELETE_URI = "/delete";

	}
	
	public interface MasterPromotionStrategyInterfaceUri {
		static final String MASTER_PROMOTION_BASE_URI = "/masterPromotion";
		static final String MASTER_PROMOTION_ADD_URI = "/addMasterPromotion";
		static final String MASTER_PROMOTION_GET_ALL_URI = "/getAllMasterPromotion";
		static final String MASTER_PROMOTION_UPDATE_URI = "/updateMasterPromotion";

	}

	public interface FileInterfaceUri {
		static final String FILE_BASE_URI = "/file";
		static final String FILE_UPLOAD_URI = "/upload";
		static final String FILE_DOWNLOAD_URI = "/download";

	}

	public interface CustomerRequesterInterfaceUri {
		static final String CUSTOMER_COMPANY_BASE_URI = "/customer";
		static final String CUSTOMER_REQUESTER_ADD_URI = "/addRequester";
		static final String CUSTOMER_REQUESTER_UPDATE_URI = "/updateRequester";
		static final String CUSTOMER_REQUESTER_GET_ALL_REQUESTER_BY_COMPANY_ID_URI = "/getAllRequester";
		static final String CUSTOMER_REQUESTER_GET_ALL_REQUESTERS_BY_COMPANY_ID_NAVIGATION_URI = "/getAllRequestersByCompanyId";
		static final String CUSTOMER_REQUESTER_GET_URI = "/getRequester";
		static final String CUSTOMER_REQUESTER_PROFILE_GET_URI = "/getRequesterProfile";
		static final String CUSTOMER_REQUESTER_DELETE_URI = "/deleteRequester";
	}

	public interface CustomerApproverInterfaceUri {
		static final String CUSTOMER_APPROVER_BASE_URI = "/customer/approver";
		static final String CUSTOMER_APPROVER_ADD_URI = "/add";
		static final String CUSTOMER_APPROVER_GET_URI = "/get";
		static final String CUSTOMER_APPROVER_GET_ALL_URI = "/getByCompanyId";
		static final String CUSTOMER_APPROVER_GET_ALL_APPORVER_BY_HEAD_ID_URI = "/getAllByHeadId";
		static final String CUSTOMER_APPROVER_UPDATE_URI = "/update";
		static final String CUSTOMER_APPROVER_DELETE_URI = "/delete";
	}

	public interface BidderApproverInterfaceUri {
		static final String BIDDER_APPROVER_BASE_URI = "/bidder/approver";
		static final String BIDDER_APPROVER_ADD_URI = "/add";
		static final String BIDDER_APPROVER_GET_URI = "/get";
		static final String BIDDER_APPROVER_UPDATE_URI = "/update";
		static final String BIDDER_APPROVER_DELETE_URI = "/delete";
	}

	public interface VendorApproversPriceRangeInterfaceUri {
		static final String VENDOR_APPROVERS_PRICE_RANGE_BASE_URI = "/vendor/approver";
		static final String VENDOR_APPROVERS_PRICE_RANGE_ADD_URI = "/add";
		static final String VENDOR_APPROVERS_PRICE_RANGE_GET_URI = "/get";
		static final String VENDOR_APPROVERS_PRICE_RANGE_UPDATE_URI = "/update";
		static final String VENDOR_APPROVERS_PRICE_RANGE_DELETE_URI = "/delete";
	}

	public interface MasterEquipmentCategoryInterfaceUri {
		static final String MASTER_EQUIMENT_CATEGORY_BASE_URI = "/equipment/category";
		static final String MASTER_EQUIMENT_CATEGORY_ADD_URI = "/add";
		static final String MASTER_EQUIMENT_CATEGORY_GET_URI = "/get";
		static final String MASTER_EQUIMENT_CATEGORY_GET_ALL_URI = "/getAllEquipmentCategory";
		static final String MASTER_EQUIMENT_CATEGORY_GET_ALLS_URI = "/getAllEquipmentCategoryNonVerified";
		static final String MASTER_EQUIMENT_CATEGORY_UPDATE_URI = "/update";
		static final String MASTER_EQUIMENT_CATEGORY_DELETE_URI = "/delete";
	}

	public interface EquipmentInterfaceUri {
		static final String EQUIMENT_CATEGORY_BASE_URI = "/equipment";
		static final String EQUIMENT_CATEGORY_CREATE_URI = "/createEquipment";
		static final String EQUIMENT_CATEGORY_GET_URI = "/getEquipmentName";
		static final String EQUIMENT_CATEGORY_GET_ALL_URI = "/getAllEquipmentName";
		static final String EQUIMENT_CATEGORY_UPDATE_URI = "/updateEquipmentName";
		static final String EQUIMENT_CATEGORY_DELETE_URI = "/deleteEquipmentName";
		static final String EQUIPMENT_GET_ALL_BY_CATEGORY = "/getAllEquipmentByCategory";
	}

	public interface EngineerEquimentsInterfaceUri {
		static final String ENGINEER_EQUIMENTS_BASE_URI = "/equipments";
		static final String ENGINEER_EQUIMENTS_CREATE_URI = "/createEngineer";
		static final String DEMO_ENGINEER_EQUIMENTS_CREATE_URI = "/democreateEngineer";
		static final String ENGINEER_EQUIMENTS_GET_URI = "/getAllEngineer";
		static final String DEMO_ENGINEER_EQUIMENTS_GET_URI = "/getDemoEngineer";
		static final String ENGINEER_EQUIMENTS_GETS_URI = "/getEngineer";
		static final String ENGINEER_EQUIMENTS_BY_UID_GET_URI = "/getEngineerByUid";
		static final String ENGINEER_EQUIMENTS_UPDATE_URI = "/updateEngineer";
		static final String ENGINEER_EQUIMENTS_DELETE_URI = "/deleteEngineer";

	}

	public interface MasterManufacturerInterfaceUri {
		static final String MASTER_MANUFACTURE_BASE_URI = "/manufacturer";
		static final String MASTER_MANUFACTURE_CREATE_URI = "/createEquipmentManufacturer";
		static final String MASTER_MANUFACTURE_GET_URI = "/getEquipmentManufacturer";
		static final String MASTER_MANUFACTURE_GET_ALL_URI = "/getAllEquipmentManufacturer";
		static final String MASTER_MANUFACTURE_UPDATE_URI = "/updateEquipmentManufacturer";
		static final String MASTER_MANUFACTURE_DELETE_URI = "/deleteEquipmentManufacturer";
		static final String MASTER_MANUFACTURE_GET_ALL_BY_EQUIPMENT_ID_URI = "/getEquipmentManufacturerByEquipmentId";

	}

	public interface MasterCountryInterfaceUri {
		static final String MASTER_COUNTRY_BASE_URI = "/master";
		static final String MASTER_COUNTRY_GET_URI = "/getCountryListId";
		static final String MASTER_COUNTRYS_GET_URI = "/getCountryList";
		static final String MASTER_CONTINENT_GET_URI = "/getContinentList";
		static final String GET_ALL_COUNTRY_DETAILS_URI = "/getCountryDetails";
		static final String MASTER_COUNTRY_CURRENCY_GET_URI = "/getCountryCurrencyList";

	}

	public interface MasterStateInterfaceUri {
		static final String MASTER_STATE_BASE_URI = "/master";
		static final String MASTER_STATE_GET_URI = "/getStateListId";
		static final String MASTER_STATE_LIST_URI = "/getStatelist";

	}

	public interface MasterCityInterfaceUri {
		static final String MASTER_CITY_BASE_URI = "/master";
		static final String MASTER_CITY_GET_URI = "/getCityListId";
		static final String MASTER_CITY_GET_ALL_URI = "/getCityListByCountryId";

	}

	public interface WorkingVendorInterfaceUri {
		static final String WORKING_VENDOR_BASE_URI = "/workingVendor";
		static final String WORKING_VENDOR_CREATE_URI = "/createWorkingVendor";
		static final String GET_WORKING_VENDOR_URI = "/getWorkingVendor";

	}

	public interface MasterPortsInterfaceUri {
		static final String MASTER_PORTS_BASE_URI = "/master";
		static final String MASTER_PORTS_GET_URI = "/getPortsListByStateId";
		static final String MASTER_PORTS_GETS_URI = "/getPortsListCountryId";
		static final String MASTER_PORTS_GETS_All_URI = "/getPortsNonVerified";
		static final String MASTER_PORTS_CREATE_URI = "/createMasterPort";
		static final String MASTER_PORTS_VERIFIED_URI = "/VerifiedMasterPort";
		static final String MASTER_PORTS_UPDATE_URI = "/updateMasterPort";
		static final String MASTER_PORTS_DELETE_URI = "/deleteMasterPort";

	}

	public interface InvoiceInterfaceUri {
		static final String INVOICE_BASE_URI = "/invoice";
		static final String INVOICE_SAVE_URI = "/saveInvoice";
		static final String GET_INVOICE = "/getInvoiceById";
	}

	public interface PaymentInterfaceUri {
		static final String PAYMENT_BASE_URI = "/payment";
		static final String PAYMENT_CREATE_URI = "/createPayment";
		static final String PAYMENT_GET_URI = "/totalAmount";
		static final String GET_PAYMENT_DETAIL_URI = "/getPaymentDetail";
		static final String GET_PAYMENT_DETAILS_URI = "/getEngineerByCompanyId";
		static final String DOWNLOAD_INVOICE_DETAILS="/downloadInvoiceByCompanyID";
		static final String STRIPE_STATUS_BY_WEBHOOK="/webhook";
		static final String GET_TXN_DETAILS_BY_USER_ID_URI = "/getTxnDetailsByUserId";

	}

	public interface ServiceInterfaceUri {
		static final String SERVICE_BASE_URI = "/service";
		static final String SERVICE_CREATE_URI = "/createService";
		static final String DEMO_SERVICE_CREATE_URI = "/createDemoService";
		static final String SERVICE_GET_URI = "/getAllServiceData";
		static final String SERVICE_UPDARE_URI = "/updateService";
		static final String SERVICE_DELETE_URI = "/deleteServiceData";

	}

	public interface ServiceRequestInterfaceUri {
		static final String SERVICE_REQUEST_BASE_URI = "/serviceRequest";
		static final String SERVICE_REQUEST_CREATE_URI = "/createServiceRequest";
		static final String SERVICE_REQUEST_GET_URI = "/getServiceRequest";
		static final String SERVICE_REQUEST_GET_ALL_URI = "/getAllServiceRequest";
		static final String SERVICE_REQUEST_GET_ALL_NEW_URI = "/getNewAllServiceRequest";
		static final String SERVICE_REQUEST_GET_ALL_BY_STATUS_AND_REQUESTERID_URI = "/getServiceRequestsByStatusAndRequestId";
		static final String SERVICE_REQUEST_GET_ALL_BIDS_RECEIVED_URI = "/getAllBidReceived";
		static final String SERVICE_REQUEST_GET_ALL_COMPLETED_JOB_URI = "/getAllCompletedJob";
		static final String SERVICE_REQUEST_GET_ALL_APPROVER_PENDING_URI = "/getAllApproverPending";
		static final String SERVICE_REQUEST_GET_ALL_PO_RAISED_URI = "/getAllPoRaised";
		static final String UPDATE_ETA_AND_ETD_URI = "/updateETAAndETD";
		static final String SERVICE_REQUEST_DELETE_URI = "/deleteServiceRequest";
		static final String SERVICE_REQUEST_UPDATE_URI = "/updateService";
		static final String UPDATE_ACCEPT_BIDS = "/updateAcceptBid";
		static final String SERVICE_REQUEST_GET_INVOICE_PAID = "/getInvoicePaidRequest";
		static final String SERVICE_REQUEST_GET_WORK_IN_PROGRESS = "/getWorkInProgressRequest";
		static final String UPDATE_SERVICE_REQUEST_STATUS = "/updateServiceRequestStatus";
		static final String UPDATE_BID_WORK_IN_PROGRESS = "/updateWorkInProgressBid";
		static final String SERVICE_REQUEST_GET_INVOICE_RAISED = "/getBidInoviceRaised";
		static final String SERVICE_REQUEST_GET_PO_ACCEPTED = "/getBidPOAccepted";
		static final String UPDATE_PO_RAISED = "/updatePORaised";
		static final String UPDATE_PO_ACCEPTED = "/updatePOAccepted";
		static final String GET_ALL_COUNTS = "/getAllCounts";

		static final String SERVICE_REQUESTS_BY_STATUS = "/getAllServiceRequestsByStatus";
		static final String SERVICE_REQUEST_BY_ID = "/getServiceRequestByID";
		static final String CHANGE_REQUEST_STATUS = "/changeRequestStatusByBidderId";
		static final String GET_BIDDER_REQUESTS_BY_STATUS = "/getBidderRequestsByStatus";
	}

	public interface BidsInterfaceUri {
		static final String BIDS_BASE_URI = "/bid";
		static final String BIDS_SUBMIT_URI = "/submitBid";
		static final String BIDS_GET_ALL_URI = "/getAllBidsByServiseRequest";
		static final String BIDS_GET_ENGINEER_ALLOCATON_DETAILS_URI = "/getAllBidRequestDetails";
		static final String BIDS_UPDATE_BID_URI = "/updateSubmitedBid";
		static final String BIDS_GET_CUSTOMER_BID_URI = "/getCustomerBid";
		static final String BIDS_UPDATE_STATUS_URI = "/updateInterestedToNotInterested";
		static final String BIDS_DELETE_BID_URI = "/deleteBid";
		static final String BIDS_GET_URI = "/getVendorBid";
		static final String BIDS_GET_ENGINEERURI = "/getEngineersWithRankingByCompanyId";
		static final String BIDS_ALLOCATE_ENGINEERS = "/engineerAllocation";
		static final String BIDS_GET_MEDIAN_URI = "/getBidsMedian";
		static final String BIDS_GET_ALL_VENDOR_BIDS_URI = "/getAllVendorBid";
		static final String BIDS_GET_BIDS_QUOTATION_URI = "/getBidQuotation";
		static final String GET_ALL_VENDOR_BIDS = "/getAllVendorBidsByStatus";
		static final String WITHDRAW_BID = "/withdrawBid";
		static final String RAISE_PO = "/raisePO";
		static final String ACCEPT_PO = "/acceptPO";
		static final String DECLINE_PO = "/declinePO";
		static final String GET_ALL_RAISED_POs = "/getAllRaisedPOsByBidderId";
		static final String GET_ALL_ACCEPTED_POs = "/getAllAcceptedPOsByBidderId";
		static final String GET_ALL_WORK_IN_PROGRESS = "/getAllWorkInProgressByBidderId";
		static final String GET_PO_BY_ID="/getPOById";
		static final String GET_INVOICE_BY_INVOICE_ID="/getInvoiceDetailsById";
		static final String WITHDRAW_PO = "/withdrawPO";
		static final String RAISE_ADD_COST = "/raiseAdditionalCost";
		static final String RECEIVE_ADD_COST = "/receiveAdditionalCost";
		static final String BID_BY_ID = "/getBidById";
		static final String GET_ALL_RAISED_ADDITIONAL_COST_BIDS = "/getAllAdditionalCostRaisedBids";
		static final String REVOKE_ADD_COST = "/revokeAdditionalCost";
		static final String GET_ALL_JOBS_BY_BIDDERID = "/getAllJobsByBidderIdAndStatus";
		static final String GET_INVOICE_DETAILS = "/getInvoiceDetails";
		static final String DOWNLOAD_INVOICE = "/downloadInvoiceByInvoiceNumber";
		static final String RAISE_INVOICE = "/raiseInvoice";
		static final String GET_WITHDRAWN_POs = "/getAllWithdrawnPOs";
		static final String GET_LIVE_STATUS = "/getLiveStatus";
		static final String GET_HISTORY_COUNTS = "/getHistoryCounts";
		static final String BIDS_GET_ALL_ENGINEER_WITH_JOBS_URI = "/getAllEngineerJobDetails";
		static final String BIDS_GET_ENGINEER_JOB_DETAILS_URI = "/getEngineerJobDetail";
		static final String DOWNLOAD_PO = "/downloadPOByPOID";
	}

	public interface VendorAdminInterfaceUri {
		static final String VENDOR_ADMIN_BASE_URI = "/vendor";
		static final String VENDOR_ADMIN_CREATE_URI = "/createVendorAdmin";
		static final String VENDOR_ADMIN_UPDATE_URI = "/updateVendorAdmin";
		static final String VENDOR_ADMIN_GET_URI = "/getVendorAdmin";
		static final String VENDOR_ADMIN_DELETE_URI = "/deleteVendorAdmin";
		static final String VENDOR_ADMIN_GET_COUNTS = "/getAllCount";
		static final String VENDOR_ADMIN_GET_ACTIVE_USERS = "/getAllActiveUsers";
		static final String VENDOR_ADMIN_GET_ACTIVE_BIDDERS = "/getAllActiveBidders";
		static final String VENDOR_ADMIN_GET_ACTIVE_APPROVER = "/getAllActiveApprovers";
		static final String VENDOR_ADMIN_GET_ACTIVE_ENGINEERS = "/getAllActiveEngineers";
		static final String VENDOR_ADMIN_GET_ACTIVE_FINANCERS = "/getAllActiveFinancers";
		static final String VENDOR_ADMIN_GET_ACTIVE_HOC = "/getActiveHOC";
		static final String VENDOR_ADMIN_UPDATE_USER_ACCESS = "/updateUserAccess";
		static final String VENDOR_ADMIN_CREATE_NEW_USER = "/createNewUser";
		static final String VENDOR_ADMIN_CREATE_NEW_HOC = "/createNewHOC";
		static final String VENDOR_ADMIN_UPDATE_USER = "/updateUser";
		static final String VENDOR_ADMIN_GET_UPDATED_DETAILS = "/getUpdatedFields";
		static final String VENDOR_ADMIN_GET_UPDATE_DECLINE_REASON = "/getUpdateDeclineReason";
		static final String VENDOR_ADMIN_GET_USER_BY_ID = "/getUserById";
	}

	public interface CustomerAdminInterfaceUri {
		static final String CUSTOMER_ADMIN_BASE_URI = "/customer";
		static final String CUSTOMER_ADMIN_CREATE_URI = "/createCustomerAdmin";
		static final String CUSTOMER_ADMIN_UPDATE_URI = "/updateCustomerAdmin";
		static final String CUSTOMER_ADMIN_GET_URI = "/getCustomerAdmin";
		static final String CUSTOMER_ADMIN_DELETE_URI = "/deleteCustomerAdmin";

	}

	public interface VendorCompanyHeadInterfaceUri {
		static final String VENDOR_HEAD_BASE_URI = "/vendor";
		static final String VENDOR_HEAD_CREATE_URI = "/createVendorCompanyHead";
		static final String VENDOR_HEAD_UPDATE_URI = "/updateVendorCompanyHead";
		static final String VENDOR_HEAD_GET_URI = "/getVendorCompanyHead";
		static final String VENDOR_HEAD_GET_ALL_COUNT_URI="/getAllCounts";
		static final String VENDOR_HEAD_GET_PO_RECEIVED_URI="/getAllPO";
		static final String VENDOR_HEAD_GET_RAISED_BIDS_URI="/getAllRaisedBids";
		static final String VENDOR_HEAD_GET_INTERESTED_REQUEST_URI="/getAllInterestedRequest";
		static final String VENDOR_HEAD_GET_INVOICE_PAID_URI="/getAllInvoicePaid";
		static final String VENDOR_HEAD_GET_INVOICE_RAISED_URI="/getAllRaisedInvoice";
		static final String VENDOR_HEAD_GET_COMPLETED_JOB_URI="/getAllCompetedJobs";
		static final String VENDOR_HEAD_GET_ALL_USERS_URI="/ getAllUsers";
		static final String VENDOR_HEAD_GET_All_BIDS_FOR_APPROVAL_URI="/ getAllBidsByHeadId";
		static final String VENDOR_HEAD_UPDATE_BID_BY_HOC_URI="/ updateBidByHoC";
		static final String VENDOR_HEAD_UPDATE_BID_STATUS_URI="/ updateBidStatus";
		static final String VENDOR_HEAD_GET_ALL_USERS_FOR_ACCESS_REQUEST_URI="/ getAllUsersforAccessRequestByHeadId";
		static final String VENDOR_HEAD_CHANGE_ACCESS_REQUEST_STATUS_URI="/ ChangeStatusOfAccessibility";
		static final String VENDOR_HEAD_GET_ALL_NEW_USERS_FOR_APPROVAL="/getAllNewUsersForApproval";
		static final String VENDOR_HEAD_CHANGE_USER_APPROVAL_STATUS="/changeUserApprovalStatus";
		static final String VENDOR_HEAD_UPDATE_USER_ACCESS="/updateUserAccessStatus";
	}

	public interface EmailSubscriptionInterfaceUri {
		static final String EMAIL_SUBSCRIPTION_BASE_URI = "/emailSubscription";
		static final String CREATE_EMAIL_SUBSCRIPTION_URI = "/createEmailSubscription";
		static final String GET_EMAIL_SUBSCRIPTION_URI = "/getAllEmailSubscription";

	}

	public interface PhoneSubscriptionInterfaceUri {
		static final String PHONE_SUBSCRIPTION_BASE_URI = "/phoneSubscription";
		static final String CREATE_PHONE_SUBSCRIPTION_URI = "/createPhoneSubscription";
		static final String GET_PHONE_SUBSCRIPTION_URI = "/getAllPhopneSubscription";

	}

	public interface AvailableOnInterfacUri {
		static final String AVAILABLE_ON_BASE_URI = "/availableOn";
		static final String AVAILABLE_ON_ADD_URI = "/add";
		static final String AVAILABLE_ON_GET_URI = "/get";

	}

	public interface DepartmentNameInterfacUri {
		static final String DEPARTMENT_NAME_BASE_URI = "/departmentName";
		static final String DEPARTMENT_NAME_ADD_URI = "/add";
		static final String DEPARTMENT_NAME_GET_URI = "/get";
		static final String DEPARTMENT_NAME_VERIFY_URI = "/get";

	}

	public interface MiscellaneousVendorInterfacUri {
		static final String MISCELLANEOUS_VENDOR_BASE_URI = "/miscellaneousVendor";
		static final String MISCELLANEOUS_VENDOR_ADD_URI = "/create/MiscellaneousVendor";
		static final String GET_MISCELLANEOUS_VENDOR_URI = "/get/MiscellaneousVendor";

	}

	public interface SparesVendorInterfacUri {
		static final String SPARES_VENDOR_BASE_URI = "/vendorSpares";
		static final String SPARES_VENDOR_ADD_URI = "/create/vendorSpares";
		static final String GET_SPARES_VENDOR_URI = "/get/vendorSpares";

	}

	public interface TravelCostVendorInterfacUri {
		static final String TRAVEL_COST_VENDOR_BASE_URI = "/travelCostVendor";
		static final String TRAVEL_COST_VENDOR_VENDOR_ADD_URI = "/create/travelCostVendor";
		static final String GET_TRAVEL_COST_VENDOR_URI = "/get/getVendorTravelCost";

	}

	public interface EngineerCertificateInterfaceUri {
		static final String ENGINEER_CERTIFICATE_BASE_URI = "/engineerCertificate";
		static final String ENGINEER_CERTIFICATE_GET_URI = "/get";

	}

	public interface MakeInterfaceUri {
		static final String MAKE_BASE_URI = "/make";
		static final String MAKE_GET_ALL_URI = "/getAllMake";
		static final String MAKE_CREATE_URI = "/create";

	}

	public interface VesselInterfacUri {
		static final String VESSEL_BASE_URI = "/vessel";
		static final String VESSEL_ADD_URI = "/add";
		static final String VESSEL_GET_URI = "/getVesselList";
		static final String VESSEL_GET_ALL_SHIP_NAME_AND_IMO_URI = "/getAllShipNameAndIMO";

	}

	public interface DashBoardInterfacUri {
		static final String DASHBOARD_BASE_URI = "/dashboard";
		static final String REQUESTER_DASHBOARD_URI = "/requester";
	}

	public interface CommissionInterfaceUri {
		static final String COMMISSION_BASE_URI = "/commission";
		static final String COMMISSION_GET_ALL_URI = "/getAllCommission";

	}

	public interface TwilioInterfaceUri {
		static final String TWILIO_BASE_URI = "/TWILIO";
		static final String TWILIO_MESSAGE_URI = "/sendMessage";

	}

	public interface EngineerChargesInterfaceUri {
		static final String ENGINEER_CHARGES_BASE_URI = "/chages";
		static final String ENGINEER_CHARGES_CREATE_URI = "/createEngineerCharges";
		static final String ENGINEER_CHARGES_GET_URI = "/getEngineerCharges";
		static final String ENGINEER_CHARGES_UPDATE_URI = "/updateEngineerCharges";

	}

	public interface VoucherInterfaceUri {
		static final String VOUCHER_BASE_URI = "/voucher";
		static final String GET_VOUCHERS_BY_COMPANY_ID = "/getVouchersByCompanyID";
		static final String REDEEM_VOUCHER = "/redeemVoucher";

	}
	
	
	public interface EngineerInterfaceUri{
		static final String ENGINEER_JOB_DETAILS_BASE_URI = "/engineerJobDetails";
		static final String ENGINEER_JOB_DETAILS_GET_COUNTS_URI ="/getAllCountsByEngineerId";
		static final String ENGINEER_JOB_STATUS_CHANGE_URI="/updateStatusToInProgress";
		static final String ENGINEER_JOB_SERVICE_REPORT_UPLOAD="/prepareServiceReport";
		static final String GET_ENGINEER_JOB_SERVICE_REPORT="/getServiceReport";
		static final String ENGINEER_GET_ALL_JOB_BY_STATUS="/getAllJobsByStatus";
		static final String GET_ENGINEER_JOB_CALENDAR_DETAILS="/getEngineerJobCalendar";
	}
	
	
}