package com.athmarine.resources;

public interface AppConstant {

//	public interface Config {
//		public static final String APPLICATION_PROPERTY_SOURCE_PATH = "file:///C:/TransasProperty/application.properties";
//	}

	public interface Config {
		public static final String APPLICATION_PROPERTY_SOURCE_PATH = "file:///mnt/athmarine/properties/application.properties";
	}
	 
	 
	public interface RequestApi {

	}

	public interface OtpService {

		public static final Integer OTP_EXPIRY_TIME = 120;
		public static final String OTP_SUBJECT_LINE = "Ath-Marine: OTP Verification";

	}

	public interface TwilioSmsService {

		public static final String ADMAGISTER_SENDERID = "INNFOX";
		public static final String MESSAGE = "Your AthMarine Verification OTP is : {var1}";

	}

	public interface OtpSmsService {

		public static final String USERID = "MovingPIN2017";
		public static final String PASSWORD = "dial$842";
		public static final String ADMAGISTER_CHANNEL = "Trans";
		public static final String ADMAGISTER_SENDERID = "INNFOX";
		public static final Integer ADMAGISTER_FLASH_SMS = 0;
		public static final Integer ADMAGISTER_DCS = 0;
		public static final Integer ADMAGISTER_ROUTE = 11;
		public static final String ADMAGISTERURL = "https://admagister.net/api/mt/SendSMS";
		public static final String MESSAGE = "Your FoxMatrix(INNFOX) verification OTP is : {var1}";

	}

	public interface ErrorCodes {

		String INVALID_INPUT_ERROR_CODE = "102";
		String USER_DOES_NOT_EXISTS_ERROR_CODE = "103";
		String INVALID_OTP_ERROR_CODE = "104";
		String RESOURCE_NOT_FOUND = "404";
		String PASSWORD_MISMATCH_ERROR_CODE = "105";
		String EMAIL_EXIST_ERROR_CODE = "106";
		String MODULE_EXIST_ERROR_CODE = "107";
		String VENDOR_COMPANY_EXIST_ERROR_CODE = "404";
		String TOKEN_EXPIRED_ERROR_CODE = "108";
		String ROLE_MISMATCH_ERROR_CODE = "109";
		String RESOURCE_EXIST_ERROR_CODE = "110";
		String CUSTOMER_COMPANY_ERROR_CODE = "101";
		String CUSTOMER_APPROVER_ERROR_CODE = "111";
		String BIDDER_APPROVER_ERROR_CODE = "112";
		String VENDOR_APPROVERS_PRICE_RANGE_ERROR_CODE = "113";
		String MASTER_EQUIMENT_CATEGORY_ERROR_CODE = "114";
		String MASTER_COUNTRY_ERROR_CODE = "115";
		String MASTER_STATE_ERROR_CODE = "116";
		String SERVICE_ERROR_CODE = "117";
		String EQUIPMENT_ERROR_CODE = "118";
		String MANUFACTURER_ERROR_CODE = "119";
		String FILE_ERROR_CODE = "120";
		String SERVICE_REQUEST_ERROR_CODE = "121";
		String VENDOR_BIDDER_ERROR_CODE = "132";
		String VENDOR_ENGINEER_ERROR_CODE = "133";
		String BIDS_ERROR_CODE = "122";
		String VENDOR_APPROVER_ERROR_CODE = "134";
		String ADMIN_ERROR_CODE = "135";
		String ID_NULL_ERROR_CODE = "136";
		String PRIMARY_PHONE_ERROR_CODE = "333";
		String VENDOR_ENGINEER_ALREADY_EXIST_ERROR_CODE = "137";
		String VENDOR_BIDDER_ALREADY_EXIST_ERROR_CODE = "138";
		String VENDOR_APPROVER_PRICE_RANGE_ALREADY_ERROR_CODE = "139";
		String VENDOR_APPROVER_ALREADY_EXIST_ERROR_CODE = "140";
		String ADMIN_ALREADY_EXIST_ERROR_CODE = "141";
		String USER_ALREADY_EXIST_ERROR_CODE = "142";
		String ENGINEER_EQUIMENTS_EXTIS_CODE = "123";
		String VENDOR_COMPANY_HEAD_ALREADY_EXIST_ERROR_CODE = "143";
		String CUSTOMER_REQUESTER_NOT_EXIST_ERROR = "144";
		String CUSTOMER_HEAD_ALREADY_EXIST_ERROR = "145";
		String FILE_UPLOAD_ERROR_CODE = "146";
		String AVAILABLE_ON_ERROR_CODE = "147";
		String PHONE_NUMBER_ALREADY_EXIST_ERROR_CODE = "148";
		String PHONE_NUMBER_DOES_NOT_EXIST_ERROR_CODE = "149";
		String FAILED_TO_SEND_OTP_ON_MAIL_ERROR_CODE = "150";
		String FAILED_TO_SEND_OTP_ON_PHONE_ERROR_CODE = "151";
		String FAILED_TO_SEND_OTP_ERROR_CODE = "152";
		String FAILED_TO_SEND_TERM_AND_CONDITION_ERROR_CODE = "161";
		String CUSTOMER_REQUESTER_ALREADY_EXIST_ERROR_CODE = "153";
		String CITY_NOT_EXIST_ERROR_CODE = "154";
		String UPLOAD_FILE_EXCEPTION_CODE = "155";
		String PATH_NULL_FOR_FILE_ERROR_CODE = "155";
		String VENDOR_SERVICE_ERROR_CODE = "156";
		String SPARES_VENDOR_ERROR_CODE = "157";
		String TRAVEL_COST_VENDOR_ERROR_CODE = "158";
		String MISCELLANEOUS_VENDOR_ERROR_CODE = "159";
		String SERVICE_REQUEST_COMPLETED_EXIST_ERROR_CODE = "160";
		String WRONG_REFERRAL_CODE_ERROR_CODE = "161";
		String COMPANY_HEAD_DOES_NOT_EXIST_ERROR_CODE = "162";
		String ENGINEER_CHARGE_ERROR_CODE = "163";
		String BIDDER_ID_NULL_ERROR_CODE = "164";
		String PAYMENT_ALREADY_EXIST_ERROR_CODE="165";
		String INVALID_BID_AMOUNT_ERROR_CODE = "166";
		String PO_ALREADY_RAISED = "167";
		String PO_NOT_FOUND = "168";
		String STATUS_NOT_FOUND = "169";
		String INVOICE_NOT_FOUND = "170";
		String INTERESTED_REQUESTS_NOT_FOUND = "171";
		String VENDOR_HEAD_NOT_FOUND="172";
		String BIDDING_LIMIT_NOT_ASSIGNED = "173";
		String STATUS_IS_NULL = "174";
		String INVALID_SERVICE_REQUEST_ID = "175";
		String INVALID_BIDDER_ID = "176";
		String PAGE_NUMBER_NULL = "177";
		String INVOICE_ALREADY_PRESENT = "178";
		String APPROVER_ID_NOT_PRESENT = "179";
		String VOUCHERS_NOT_AVAILABLE = "180";
		String UPDATE_APPROVAL_ALREADY_PENDING = "181";
	}

	public interface StatusCodes {
		int SUCCESS = 0;
		int FAILURE = 1;
	}

	public interface ErrorTypes {

		String INVALID_INPUT_ERROR = "Invalid Otp";
		String USER_NOT_EXIST_ERROR = "Does Not Exist";
		String INVALID_OTP_ERROR = "OTP Does Not Match";
		String RESOURCE_NOT_FOUND_ERROR = "Resource Not Found";
		String PASSWORD_MISMATCH_ERROR = "Password Mismatch";
		String EMAIL_EXIST_ERROR = "Email already exist";
		String POLICY_ERROR = "Policy Error";
		String MODULE_NOT_EXIST_ERROR = "Does Not Exist";
		String VENDOR_COMPANY_NOT_EXIST_ERROR = "Does Not Exist";
		String MASTER_PROMOTION_NOT_EXIST_ERROR = "Does Not Exist";
		String TOKEN_EXPIRED_ERROR = "TOKEN EXPIRED";
		String ROLE_MISMATCH_ERROR = "ROLE MISMATCH";
		String RESOURCE_EXIST_ERROR = "RESOURCE ALREADY EXIST";
		String CUSTOMER_COMPANY_NOT_EXIST_ERROR = "Does Not Exist";
		String CUSTOMER_APPROVER_NOT_EXIST_ERROR = "Does Not Exist";
		String BIDDER_APPROVER_NOT_EXIST_ERROR = "Does Not Exist";
		String VENDOR_APPROVERS_PRICE_RANGE_EXIST_ERROR = "Does Not Exist";
		String MASTER_EQUIMENT_CATEGORY_EXIST_ERROR = "Does Not Exist";
		String MASTER_COUNTRY_EXIST_ERROR = "Does Not Exist";
		String MASTER_STATE_EXIST_ERROR = "Does Not Exist";
		String USER_EXIST_ERROR = "User already extist";
		String SERVICE_NOT_EXIST_ERROR = "Does Not Exist";
		String ROLE_EMPTY_ERROR = "Role EMPTY";
		String EQUIPMENT_EXIST_ERROR = "Does Not Exist";
		String MANUFACTURER_EXIST_ERROR = "Does Not Exist";
		String FILE_EXIST_ERROR = "Does Not Exist";
		String SERVICE_REQUEST_EXIST_ERROR = "Request Not Found";
		String VENDOR_BIDDER_EXIST_ERROR = "Vendor Bidder Not Found";
		String VENDOR_ENGINEER_EXIST_ERROR = "Vendor Engineer Not Found";
		String BIDS_EXIST_ERROR = "Bid Not Found";
		String VENDOR_APPROVER_EXIST_ERROR = "Vendor Approver Not Found";
		String ADMIN_EXIST_ERROR = "Admin Not Found";
		String ID_NULL_EXIST_ERROR = "Id is Null";
		String PRIMARY_PHONE_EXIST_ERROR = "Does Not Same";
		String VENDOR_COMPANY_EXIST_ERROR = "Vendor Company Already Exist";
		String VENDOR_ENGINEER_ALREADY_EXIST_ERROR = "Vendor Engineer Already Exist";
		String VENDOR_BIDDER_ALREADY_EXIST_ERROR = "Vendor Bidder Already Exist";
		String VENDOR_APPROVER_PRICE_RANGE_ALREADY_EXIST_ERROR = "Vendor Approver Price Range Already Exist";
		String VENDOR_APPROVER_ALREADY_EXIST_ERROR = "Vendor Approver Already Exist";
		String ADMIN_ALREADY_EXIST_ERROR = "Admin Already Exist";
		String ENGINEER_EQUIMENTS_EXTIS_ERROR = "Engineer Equiments Does Not Exist";
		String VENDOR_COMPANY_HEAD_ALREADY_EXIST_ERROR = "Company Head Already Exist";
		String CUSTOMER_REQUESTER_NOT_EXIST_ERROR = "Customer Requester Does Not Exist";
		String CUSTOMER_HEAD_ALREADY_EXIST_ERROR = "Customer Head already Exist";
		String FILE_UPLOAD_ERROR = "Failed to upload File";
		String AVAILABLE_ON_ERROR = "Does not exist";
		String PHONE_NUMBER_ALREADY_EXIST_ERROR = "Phone Number Already Exist";
		String PHONE_NUMBER_DOES_NOT_EXIST_ERROR = "Phone Number Does Not Exist";
		String FAILED_TO_SEND_OTP_ON_MAIL_ERROR = "Failed to send OTP on Mail";
		String FAILED_TO_SEND_OTP_ON_PHONE_ERROR = "Failed to send OTP on Phone";
		String FAILED_TO_SEND_OTP_ERROR = "Failed to send OTP";
		String CUSTOMER_REQUESTER_ALREADY_EXIST_ERROR = "Customer Requester Already Exists";
		String CITY_NOT_EXIST_ERROR = "City does not Exist";
		String UPLOAD_FILE_EXCEPTION = "Failed to Upload File";
		String PATH_NULL_FOR_FILE = "Null path error";
		String SERVICES_NOT_FOUND_FOR_VENDOR_COMPANY = "Service not found";
		String FAILED_TO_SEND_CREDENTIALS_ON_MAIL_ERROR = "Failed to send Credentials on Mail";
		String FAILED_TO_SEND_TERM_AND_CONDITION = "Failed to send Term and Condition on Mail";
		String SPARES_VENDOR_EXIST_ERROR = "Spares Does Not Exist";
		String TRAVEL_COST_VENDOR_EXIST_ERROR = "TravelCost Does Not Exist";
		String MISCELLANEOUS_VENDOR_EXIST_ERROR = "Miscellaneous Does Not Exist";
		String SERVICE_REQUEST_COMPLETED_EXIST_ERROR = "Service Request Is Not Completed";
		String REFERRAL_CODE_INCORRECT_ERROR = "Wrong Referral Code";
		String COMPANY_HEAD_DOES_NOT_EXIST = "Invalid Company Head Id";
		String ENGINEER_CHARGE_EXIST_ERROR = "Engineer Chage Not Found";
		String BIDDER_ID_NULL_EXIST_ERROR = "Bidder Does Not Exist";
		String PAYMENT_ALREADY_EXIST_ERROR = "Payemnt Already Exist";
		String INVALID_BID_AMOUNT_ERROR = "Invalid Bid Amount";
		String PO_ALREADY_RAISED_EXCEPTION = "PO Already Raised";
		String PO_NOT_FOUND = "PO Not Found";
		String STATUS_NOT_FOUND = "Status Not Found";
		String INVOICE_NOT_FOUND = "Invoice Not Found";
		String INTERESTED_REQUESTS_NOT_FOUND = "Interested Requests Not Found";
		String VENDOR_HEAD_NOT_FOUND="Vendor Head does not exist";
		String BIDDING_LIMIT_NOT_ASSIGNED = "Bidding Limit Not Found";
		String STATUS_IS_NULL = "Status is null";
		String INVALID_SERVICE_REQUEST_ID = "Invalid Service Request Id";
		String INVALID_BIDDER_ID = "Invalid Bid Id";
		String PAGE_NUMBER_NULL = "Page no. is Null";
		String INVOICE_ALREADY_PRESENT = "Invoice Already Present";
		String APPROVER_ID_NOT_PRESENT = "Approver Id is not Present";
		String VOUCHERS_NOT_AVAILABLE = "No Vouchers Available";
		String UPDATE_APPROVAL_ALREADY_PENDING = "Update Approval Already Pending";
	}

	public interface ErrorMessages {
		String USER_EMPTY_EXIST_MESSAGE = "User ID Is Empty";
		String VENDOR_SERVICE_EMPTY_EXIST_MESSAGE = "Service ID Is Empty";
		String USER_NOT_EXIST_MESSAGE = "User Does Not Exist";
		String NUMBER_NOT_EXIST_MESSAGE = "Number Does Not Exist";
		String INVALID_INPUT_MESSAGE = "Invalid OTP.";
		String INVALID_OTP_MESSAGE = "Invalid OTP.";
		String RESOURCE_NOT_FOUND_MESSAGE = "Resource Not Found";
		String PASSWORD_MISMATCH_MESSAGE = "Password Mismatch";
		String EMAIL_EXIST_MESSAGE = "Email already Exist";
		String EMAIL_POLICY_MESSAGE = "Policy Can't be False";
		String MODULE_NOT_EXIST_MESSAGE = "Module Does Not Exist";
		String VENDOR_COMPANY_NOT_EXIST_MESSAGE = "Vendor Company Does Not Exist";
		String VENDOR_SERVICE_NOT_EXIST_MESSAGE = "Service Does Not Exist";
		String MASTER_PROMOTION_NOT_EXIST_MESSAGE = "Master Promotion Does Not Exist";
		String TOKEN_EXPIRED_MESSAGE = "TOKEN EXPIRED";
		String ROLE_MISMATCH_MESSAGE = "ROLE MISMATCH";
		String RESOURCE_EXIST_MESSAGE = "RESOURCE ALREADY EXIST";
		String CUSTOMER_COMPANY_NOT_EXIST_MESSAGE = "Customer Company Does Not Exist";
		String CUSTOMER_APPROVER_NOT_EXIST_MESSAGE = "Customer Approver Does Not Exist";
		String BIDDER_APPROVER_NOT_EXTIS_MESSAGE = "Bidder Approver Does Not Exist";
		String VENDOR_APPROVERS_PRICE_RANGE_NOT_EXTIS_MESSAGE = "Vendor_Approver_Price_Range Does Not Exist";
		String VENDOR_APPROVERS_PRICE_RANGE_MAX_MESSAGE = "Maximum Value is greater than Minimum value";
		String VENDOR_APPROVERS_PRICE_RANGE_RESOURCE_EXIST_MESSAGE = "Vendor Approver Price_Range already exist";
		String MASTER_EQUIMENT_CATEGORY_NOT_EXTIS_MESSAGE = "Master Equiment Category Does Not Exist";
		String MASTER_CONTINENT_NOT_EXTIS_MESSAGE = "Continent Does Not Exist";
		String MASTER_COUNTRY_NOT_EXTIS_MESSAGE = "Country Does Not Exist";
		String MASTER_STATE_NOT_EXTIS_MESSAGE = "State Does Not Exist";
		String USER_NOT_MESSAGE = "User already Exist";
		String SERVICE_NOT_EXIST_MESSAGE = "Service Does Not Exist";
		String ROLE_EMPTY_MESSAGE = "Role Is Empty";
		String EQUIPMENT_NOT_EXIST = "Equipment Not Found";
		String EQUIPMENT_NOTS_EXIST = "Invalid engineer Id";
		String MANUFACTURER_NOT_EXIST = "Manufacturer Not Found";
		String FILE_NOT_EXIST = "File Not Found";
		String SERVICE_REQUEST_NOT_EXIST = "Service Request Not Found";
		String ID_EMPTY_MESSAGE = "ID should't be null";
		String PRIMARY_PHONE_MESSAGE = "Contact number or Email  ID is already in use";
		String PRIMARY_PHONE_MESSAGES = "Provided contact no. already registered with another company";
		String MASTER_PORTS_NOT_EXTIS_MESSAGE = "Ports Does Not Exist";
		String COMPANY_ID_EMPTY_MESSAGE = "CompanyId should't be null";
		String VENDOR_BIDDER_NOT_EXIST = "Vendor Bidder Does Not Exist";
		String VENDOR_ENGINEER_NOT_EXIST = "Vendor Engineer Does Not Exist";
		String BIDS_NOT_EXIST = "Bid Does Not Exist";
		String VENDOR_APPROVER_NOT_EXIST = "Vendor Approver Does Not Exist";
		String ADMIN_NOT_EXIST = "Admin Does Not Exist";
		String ID_NULL_NOT_EXIST = "ID Can't be NULL";
		String VENDOR_COMPANY_EXIST = "Vendor Company Already Exist";
		String VENDOR_ENGINEER_ALREADY_EXIST = "Vendor Engineer Already Exist";
		String VENDOR_BIDDER_ALREADY_EXIST = "Vendor Bidder Already Exist";
		String VENDOR_APPROVER_PRICE_RANGE_ALREADY_EXIST = "Vendor Approver Price Range Already Exist";
		String VENDOR_APPROVER_ALREADY_EXIST = "Vendor Approver Already Exist";
		String ADMIN_ALREADY_EXIST = "Admin Already Exist";
		String UNIQYE_NOT_EXIST_MESSAGE = "Unique Does Not Exist";
		String EMAIL_NOT_EXIST_MESSAGE = "Email Does Not Exist";
		String USER_ALREADY_EXIST = "User Already Exist";
		String ENGINEER_EQUIMENTS_EXTIS_MESSAGE = "Engineer Equiments Does Not Exist";
		String VENDOR_COMPANY_HEAD_ALREADY_EXIST = "Company Head Already Exist";
		String CUSTOMER_REQUESTER_NOT_EXIST_ERROR = "Customer Requester Does Not Exist";
		String CUSTOMER_HEAD_ALREADY_EXIST_ERROR = "Customer Head already Exist";
		String FILE_UPLOAD_ERROR = "Failed to upload File";
		String AVAILABLE_ON_EXIST_ERROR = "Does not exist";
		String PHONE_NUMBER_ALREADY_EXIST_ERROR = "Failed to send OTP on Phone.\\n Please enter valid number.";
		String PHONE_NUMBER_DOES_NOT_EXIST_ERROR = "Phone Number Does Not Exist";
		String FAILED_TO_SEND_OTP_ON_MAIL_ERROR = "Failed to send OTP on Mail";
		String FAILED_TO_SEND_OTP_ERROR = "Failed to send OTP";
		String FAILED_TO_SEND_TERM_AND_CONDITION_ERROR = "Failed to send Term and Condition";
		String FAILED_TO_SEND_OTP_ON_PHONE_ERROR = "Failed to send OTP on Phone";
		String CUSTOMER_REQUESTER_ALREADY_EXIST_ERROR = "Customer Requester Already Exists";
		String CITY_NOT_EXIST_ERROR = "City Does Not Exist";
		String PATH_NULL_FOR_FILE_ERROR_MESSAGE = "File Path provided for generating Pre sigend URL is null";
		String SERVICES_NOT_FOUND_FOR_VENDOR_COMPANY = "Service not found for the Comapny";
		String FAILED_TO_SEND_CREDENTIALS_ON_MAIL_ERROR = "Failed to send Credentials on Mail";
		String SPARES_VENDOR_EXIST_MESSAGE = "Spares Does Not Exist";
		String TRAVEL_COST_VENDOR_EXIST_MESSAGE = "Travel Cost Does Not Exist";
		String MISCELLANEOUS_VENDOR_EXIST_MESSAGE = "Miscellaneous Does Not Exist";
		String EMAIL_ALREADY_EXIST = "Email already Exist";
		String SERVICE_REQUEST_COMPLETED_EXIST = "Service Request Is Not Completed";
		String WRONG_REFERRAL_CODE_MESSAGE = "Invalid Referral Code Please enter valid ";
		String EMAIL_CREDENTIALS_MESSAGE = "Invalid User Credentials";
		String EXPIRED_OTP = "It seems like your OTP is expired. Please try to resend new OTP.";
		String COMPANY_HEAD_DOES_NOT_EXIST_MESSAGE = "Vendor Company Head Does Not Exist";
		String ENGINEER_CHARGE_ERROR_MESSAGE = "Engineer Charge Not Exist";
		String BIDDER_ID_NULL_NOT_EXIST = "Bidder Does Not Exist";
		String EMAIL_AND_PHONE_ALREADY_EXIST = "Email Already Exist";
		String PHONE_ALREADY_EXIST = "Phone Number Already Exist";
		String PAYMENT_ALREADY_EXIST_MESSAGE = "Payment Already Exist For This User";
		String CONTACT_NO_ALREADY_EXIST_MESSAGE = "Contact number or Email ID is already in use";
		String CONTACT_NO_EXIST_OR_INVALID = "Mobile number already exists or is invalid, please enter a different mobile number";
		String SERVICE_REQUESTS_NOT_FOUND= "No Any Service Request Found for this status, Please Provide Valid Status";
		String INVALID_BID_AMOUNT = "Invalid Bid Amount!. Bid Amount must be greater than or smaller than 25% of previous bids ";
		String PO_ALREADY_RAISED = "PO Already Raised for this Bid";
		String PO_NOT_FOUND = "PO not found for this Bid";
		String STATUS_NOT_FOUND = "Invalid Status!";
		String INVOICE_NOT_FOUND = "Invoice Not Found!";
		String INTERESTED_REQUESTS_NOT_FOUND = "Interested Requests Not Found!";
		String VENDOR_HEAD_NOT_FOUND="Vendor Head does not Exist";
		String BIDDING_LIMIT_NOT_ASSIGNED = "Bidding Limit Not Assigned To The Approver";
		String STATUS_IS_NULL = "Status Shouldn't Be Null";
		String INVALID_SERVICE_REQUEST_ID = "Service Request Id is Invalid";
		String INVALID_BIDDER_ID = "Bidder Id is Invalid";
		String PAGE_NUMBER_NULL = "Page No. Shouldn't be Null";
		String NEW_USERS_NOT_FOUND = "No New User Found For Approval";
		String INVOICE_ALREADY_PRESENT = "Invoice Already Created For this Service Request";
		String APPROVER_ID_NOT_PRESENT = "Approver Id Can't be Null";
		String BID_NOT_FOUND_FOR_APPROVAL = "Bid doesn't found for Approver's Approval Pending";
		String VOUCHERS_NOT_AVAILABLE = "No Vouchers Available";
		String UPDATE_APPROVAL_ALREADY_PENDING = "User Previous Update Is Already At Pending";
	}

	public interface Commons {
		String REQUEST_PART_FILE = "file";
		String SLASH = "/";
	}

	public interface DeleteStatus {
		int STATUS_ACTIVE = 0;
		int STATUS_DELETE = 1;
	}

	public interface SecretKey {
		public static final String SECRET_KEY = "`'+fao$##j\nknlkvla";
	}

	public interface RoleValues {
		public static final String ROLE_FINANCE = "Finance";
		public static final String ROLE_APPROVER = "Approver";
		public static final String ROLE_ADMIN = "Admin";
		public static final String ROLE_BIDDER = "Bidder";
		public static final String ROLE_BIDDER_APPROVER = "Bidder_Approver";
		public static final String ROLE_ENGINEER = "Engineer";
		public static final String ROLE_HEAD = "Head";
		public static final String ROLE_REQUESTER = "Requester";

	}

	public interface ResetPasswordService {
		public static final Integer RESET_PASSWORD_EXPIRY_TIME = 120;
		public static final String RESET_PASSWORD_SUBJECT_LINE = "Ath-Marine: RESET PASSWORD EMAIL ";
		public static final String RESET_PASSWORD_LINK = "http://transas-qa.innovationm.com/user/verifyToken";

	}

	public interface VerifyUser {
		public static final Integer VERIFY_USER_EXPIRY_TIME = 120;
		public static final String VERIFY_USER_SUBJECT_LINE = "Ath-Marine: USER VERIFY ACCOUNT ";
		public static final String VERIFY_USER_LINK = "http://transas-qa.innovationm.com/verify-web-otp";

	}

	public interface TermsAndConditions {
		public static final Integer TERMS_AND_CONDITIONS_EXPIRY_TIME = 120;
		public static final String TERMS_AND_CONDITIONS_SUBJECT_LINE = "Ath-Marine: TERMS AND CONDITIONS ";
		public static final String TERMS_AND_CONDITIONS_LINK = "http://transas-qa.innovationm.com/terms-and-conditions";

	}

	public interface VerifyRequester {
		public static final Integer VERIFY_REQUESTER_EXPIRY_TIME = 120;
		public static final String VERIFY_REQUESTER_SUBJECT_LINE = "Ath-Marine: REQUESTER RESET PASSWORD EMAIL ";
		public static final String VERIFY_REQUESTER_LINK = "http://transas-qa.innovationm.com/OTP/validateOTPEmail";

	}

	public interface VerifyApprover {
		public static final Integer VERIFY_APPROVER_EXPIRY_TIME = 120;
		public static final String VERIFY_APPROVER_SUBJECT_LINE = "Ath-Marine: APPROVER RESET PASSWORD EMAIL ";
		public static final String VERIFY_APPROVER_LINK = "http://transas-qa.innovationm.com/OTP/validateOTPEmail";

	}

	public interface VerifyTermAndCondition {

		public static final String VERIFY_TERM_AND_CONDITION_LINK = "http://transas-qa.innovationm.com/terms-and-conditions";
	}

}
