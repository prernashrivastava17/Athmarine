package com.athmarine.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.athmarine.entity.*;
import com.athmarine.exception.*;
import com.athmarine.repository.*;
import com.athmarine.request.*;
import com.athmarine.response.GetServiceRequestCount;
import com.athmarine.response.InvoiceResponseModel;
import com.athmarine.response.RequestBidsResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.athmarine.resources.AppConstant;

@Service
public class VendorFinanceService {

	@Autowired
	FinanceRepository financeRepository;

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	TurnoverService turnoverService;

	@Autowired
	public UserRepository userRepository;

	@Autowired
	public OTPService otpService;

	@Autowired
	VendorCompanyService vendorCompanyService;

	@Autowired
	VendorCompanyRepository vendorCompanyRepository;

	@Autowired
	BidsRepository bidsRepository;

	@Autowired
	InvoiceRespository invoiceRespository;

	@Autowired
	VoucherRepository voucherRepository;

	@Autowired
	ServiceRequestInvoiceRepository serviceRequestInvoiceRepository;

	@Autowired
	InvoiceService invoiceService;

	@Value("${pagination.size}")
	private Integer size;

	public List<FinanceModel> saveVendorFinance(List<UserFinanceTurnoverModel> model) {
		List<FinanceModel> response = new ArrayList<FinanceModel>();

		for (int i = 0; i < model.size(); i++) {
			UserModel financeUserModel = model.get(i).getUserModel();
			if (!financeUserModel.getSameASUser().equals("Yes")) {
				if (userRepository.existsByEmail(financeUserModel.getEmail())) {

					throw new EmailExistException(AppConstant.ErrorTypes.EMAIL_EXIST_ERROR, financeUserModel.getEmail(),
							" " + financeUserModel.getEmail() + AppConstant.ErrorMessages.EMAIL_EXIST_MESSAGE);

				} else if (userRepository.existsByPrimaryPhone(financeUserModel.getPrimaryPhone())) {

					throw new PhoneNumberExistException(AppConstant.ErrorTypes.PHONE_NUMBER_ALREADY_EXIST_ERROR,
							financeUserModel.getPrimaryPhone(), " " + financeUserModel.getPrimaryPhone()
									+ AppConstant.ErrorMessages.PHONE_NUMBER_ALREADY_EXIST_ERROR);

				}
			}
		}
		for (int j = 0; j < model.size(); j++) {
			UserModel userModel = model.get(j).getUserModel();
			FinanceModel financeModel = model.get(j).getFinanceModel();
			if (userModel.getSameASUser().equals("No")) {
				Integer count = userDetailsService.getAllFinancerByCompanyId(model.get(j).getUserModel().getCompanyId());
				List<TurnoverModel> turnoverModels = model.get(j).getTurnoverModels();

				String userEmail = userModel.getEmail();
				new Thread(new Runnable() {

					@Override
					public void run() {
						otpService.createEmailVerificationLink(userEmail);
					}
				}).start();

//			Integer count = userDetailsService.getUserCountByVendorCompany(userModel.getCompanyId(),
//					userModel.getRoleModel().getId());
				userModel = userDetailsService.createUser(userModel);
				financeModel.setId(null);
				financeModel.setUserId(userModel.getId());
				financeModel.setTurnover(turnoverModels);
				response.add(createFinance(financeModel));
				VendorCompany vendorCompany = vendorCompanyService
						.findByIds(model.get(j).getUserModel().getCompanyId());
				vendorCompany.setRegistrationStatus(RegistrationStatus.Finance);
				if(financeModel.getRegistrationstatus()==1)
					vendorCompany.setRegisteredSuccessfully(true);
				else
					vendorCompany.setRegisteredSuccessfully(false);
				vendorCompanyRepository.save(vendorCompany);

				userModel.setUid(userDetailsService.generateUniqueIdVendor(userModel.getId(), count + j + 1));
				userDetailsService.updateUserData(userModel);
			} else if (userModel.getSameASUser().equals("Yes")) {
				UserModel bidderModel = userModel;
				UserModel vendorRegistrationStatus = userDetailsService.updateSameUser(bidderModel);
				List<TurnoverModel> turnoverModels = model.get(j).getTurnoverModels();
				financeModel.setId(null);
				financeModel.setUserId(vendorRegistrationStatus.getId());
				financeModel.setTurnover(turnoverModels);
				response.add(createFinance(financeModel));
			
				VendorCompany vendorCompany = vendorCompanyService.findByIds(vendorRegistrationStatus.getCompanyId());
				vendorCompany.setRegistrationStatus(RegistrationStatus.Finance);
				if(financeModel.getRegistrationstatus()==1)
					vendorCompany.setRegisteredSuccessfully(true);
				else
					vendorCompany.setRegisteredSuccessfully(false);
				vendorCompanyRepository.save(vendorCompany);
			}
		}

		return response;
	}

	public FinanceModel createFinance(FinanceModel financeModel) {
		if (financeModel == null) {
			throw new ResourceNotFoundException(AppConstant.ErrorTypes.RESOURCE_NOT_FOUND_ERROR,
					AppConstant.ErrorCodes.RESOURCE_NOT_FOUND, AppConstant.ErrorMessages.RESOURCE_NOT_FOUND_MESSAGE);
		}
		if (!checkAlreadyExists(financeModel.getUserId())) {
			FinanceModel saveFinanceModel = convertToModel(financeRepository.save(convertToEntity(financeModel)));
			return saveFinanceModel;

		} else
			throw new ResourceAlreadyExistsException(AppConstant.ErrorTypes.RESOURCE_EXIST_ERROR,
					AppConstant.ErrorCodes.RESOURCE_EXIST_ERROR_CODE, AppConstant.ErrorMessages.RESOURCE_EXIST_MESSAGE);

	}


	public List<UserFinanceTurnoverModel> getAllFinanceByCompnayId(int id) {
		if (vendorCompanyService.checkAlreadyExists(id)) {
			List<UserFinanceTurnoverModel> financeModels = new ArrayList<UserFinanceTurnoverModel>();
			List<UserModel> userList = userDetailsService.getAllFinanceByCompanyId(id);
			for (int i = 0; i < userList.size(); i++) {
				Finance finance = findByIds(userList.get(i).getId());
				financeModels.add(convertToResponseModel(finance));
			}
			return financeModels;

		} else {
			throw new VendorCompanyNotFoundException(AppConstant.ErrorTypes.VENDOR_COMPANY_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.VENDOR_COMPANY_EXIST_ERROR_CODE,
					AppConstant.ErrorMessages.VENDOR_COMPANY_NOT_EXIST_MESSAGE);
		}
	}

	public UserFinanceTurnoverModel getFinanceDetails(int id) {
		return convertToResponseModel(financeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.ErrorTypes.RESOURCE_NOT_FOUND_ERROR,
						AppConstant.ErrorCodes.RESOURCE_NOT_FOUND,
						AppConstant.ErrorMessages.RESOURCE_NOT_FOUND_MESSAGE)));
	}

	public FinanceModel updateFinanceDetails(FinanceModel financeModel) {

		FinanceModel finanace = findById(financeModel.getId());

		financeModel.setId(finanace.getId());

		return convertToModel(financeRepository.save(convertToEntity(financeModel)));

	}

	public boolean checkAlreadyExists(int id) {
		return financeRepository.existsById(id);

	}

	public FinanceModel deleteFinanceDetails(Integer id) {

		FinanceModel finance = findById(id);

		finance.setStatus(AppConstant.DeleteStatus.STATUS_DELETE);

		return convertToModel(financeRepository.save(convertToEntity(finance)));

	}

	private FinanceModel findById(Integer id) {

		if (id == null) {
			throw new ResourceNotFoundException(AppConstant.ErrorTypes.ID_NULL_EXIST_ERROR,
					AppConstant.ErrorCodes.ID_NULL_ERROR_CODE, AppConstant.ErrorMessages.ID_EMPTY_MESSAGE);
		} else {

			return convertToModel(financeRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException(AppConstant.ErrorTypes.RESOURCE_NOT_FOUND_ERROR,
							AppConstant.ErrorCodes.RESOURCE_NOT_FOUND,
							AppConstant.ErrorMessages.RESOURCE_NOT_FOUND_MESSAGE)));
		}
	}

	private Finance findByIds(Integer id) {

		if (id == null) {
			throw new ResourceNotFoundException(AppConstant.ErrorTypes.ID_NULL_EXIST_ERROR,
					AppConstant.ErrorCodes.ID_NULL_ERROR_CODE, AppConstant.ErrorMessages.ID_EMPTY_MESSAGE);
		} else {

			return financeRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException(AppConstant.ErrorTypes.RESOURCE_NOT_FOUND_ERROR,
							AppConstant.ErrorCodes.RESOURCE_NOT_FOUND,
							AppConstant.ErrorMessages.RESOURCE_NOT_FOUND_MESSAGE));
		}
	}

	private Finance convertToEntity(FinanceModel model) {

		List<Turnover> turnoverList = model.getTurnover().stream()
				.map(turnover -> turnoverService.convertToEntity(turnover)).collect(Collectors.toList());

		return Finance.builder().id(model.getId()).user(userDetailsService.findUserEntityById(model.getUserId()))
				.turnover(turnoverList).currency(model.getCurrency()).status(model.getStatus()).type("Vendor").build();

	}

	private UserFinanceTurnoverModel convertToResponseModel(Finance entity) {
		List<TurnoverModel> turnoverList = entity.getTurnover().stream()
				.map(turnover -> turnoverService.convertToModel(turnover)).collect(Collectors.toList());
		FinanceModel financeModel = new FinanceModel();
		financeModel.setCurrency(entity.getCurrency());
		financeModel.setId(entity.getId());
		financeModel.setStatus(entity.getStatus());

		return UserFinanceTurnoverModel.builder().id(entity.getId()).financeModel(financeModel)
				.userModel(userDetailsService.convertToModel(entity.getUser())).turnoverModels(turnoverList).build();

	}

	private FinanceModel convertToModel(Finance entity) {

		List<TurnoverModel> turnoverList = entity.getTurnover().stream()
				.map(turnover -> turnoverService.convertToModel(turnover)).collect(Collectors.toList());

		return FinanceModel.builder().id(entity.getId()).userId(entity.getId()).turnover(turnoverList)
				.currency(entity.getCurrency()).status(entity.getStatus()).build();

	}

	//--------Method to get counts by status----------//

    public GetServiceRequestCount getAllCountsByStatus(int financeId) {

		UserModel user = userDetailsService.findById(financeId);

		VendorCompany vendorCompany = vendorCompanyService.findByIds(user.getCompanyId());

		List<User> bidders = userRepository.getAllBiddersByCompanyId(vendorCompany.getId());

		if(bidders.size()<1){
			throw new UserNotFoundException(AppConstant.ErrorTypes.VENDOR_BIDDER_EXIST_ERROR,
					AppConstant.ErrorCodes.VENDOR_BIDDER_ERROR_CODE,
					AppConstant.ErrorMessages.VENDOR_BIDDER_NOT_EXIST);
		}

		List<Bids> bids = bidsRepository.findAllBiddersBids(bidders);

		int invoiceRaisedCount  = serviceRequestInvoiceRepository.countAllByBids(bids);

		int invoicePaid = serviceRequestInvoiceRepository.countByBidAndInvoiceStatus(bids,
				ServiceRequestStatus.INVOICE_PAID.name());

		List<User> users = userDetailsService.getAllUserByVendorCompanyId(vendorCompany.getId());

		int payables = users.stream().mapToInt(u -> invoiceRespository.countByUserAndIsPaymentStatus(u,false)).sum();

		int vouchers = voucherRepository.countAllByCompanyId(vendorCompany.getId());

		return GetServiceRequestCount.builder().invoiceRaised(invoiceRaisedCount)
				.invoicePaid(invoicePaid).payables(payables).vouchers(vouchers).build();
    }

	//----------Method to get all Invoices By Status--------//

	public List<RequestBidsResponseModel> getAllInvoicesByStatus(Integer financeId,String invoiceStatus, Integer page){

		if(page==null){
			throw new PaginationException(AppConstant.ErrorTypes.PAGE_NUMBER_NULL,
					AppConstant.ErrorCodes.PAGE_NUMBER_NULL,AppConstant.ErrorMessages.PAGE_NUMBER_NULL);
		}

		UserModel user = userDetailsService.findById(financeId);

		VendorCompany vendorCompany = vendorCompanyService.findByIds(user.getCompanyId());

		List<User> bidders = userRepository.getAllBiddersByCompanyId(vendorCompany.getId());

		if(bidders.size()<1){
			throw new UserNotFoundException(AppConstant.ErrorTypes.VENDOR_BIDDER_EXIST_ERROR,
					AppConstant.ErrorCodes.VENDOR_BIDDER_ERROR_CODE,
					AppConstant.ErrorMessages.VENDOR_BIDDER_NOT_EXIST);
		}

		List<Bids> bids = bidsRepository.findAllBiddersBids(bidders);

		List<ServiceRequestInvoice> serviceRequestInvoices = new ArrayList<>();

		if(invoiceStatus.equalsIgnoreCase(ServiceRequestStatus.INVOICE_RAISED.name())) {
			serviceRequestInvoices = serviceRequestInvoiceRepository.findAllByBids(bids, PageRequest.of(page,size));
		}
		else if(invoiceStatus.equalsIgnoreCase(ServiceRequestStatus.INVOICE_PAID.name())){
			serviceRequestInvoices = serviceRequestInvoiceRepository.findByBidAndInvoiceStatus(bids,
					ServiceRequestStatus.INVOICE_PAID.name(), PageRequest.of(page,size));
		}
		else{
			throw new ServiceRequestStatusException(AppConstant.ErrorTypes.STATUS_NOT_FOUND,
					AppConstant.ErrorCodes.STATUS_NOT_FOUND, AppConstant.ErrorMessages.STATUS_NOT_FOUND);
		}

		if(serviceRequestInvoices.size()<1){
			throw new InvoiceException(AppConstant.ErrorTypes.INVOICE_NOT_FOUND,
					AppConstant.ErrorCodes.INVOICE_NOT_FOUND,AppConstant.ErrorMessages.INVOICE_NOT_FOUND);
		}

		return serviceRequestInvoices.stream().map(invoice -> RequestBidsResponseModel.builder().serviceRequestOn(invoice.getServiceRequest().getServiceRequestOn())
				.serviceRequestId(invoice.getServiceRequest().getId()).requestUID(invoice.getServiceRequest().getRequestUID())
				.poUID(invoice.getPurchaseOrder().getPoUID()).vessel(invoice.getServiceRequest().getVessel().getShipname())
				.category(invoice.getServiceRequest().getEquipmentCategory().getName())
				.invoiceStatus(invoice.getInvoiceUID()).invoiceId(invoice.getId())
				.invoiceValue(invoice.getTotalCost()).build()).collect(Collectors.toList());
	}



	//----------Method to get All Payable Invoices---------//


	public List<InvoiceDetailsModel> getPayableInvoices(Integer financeId, Integer page)
	{
		if(page==null){
			throw new PaginationException(AppConstant.ErrorTypes.PAGE_NUMBER_NULL,
					AppConstant.ErrorCodes.PAGE_NUMBER_NULL,AppConstant.ErrorMessages.PAGE_NUMBER_NULL);
		}

		UserModel user = userDetailsService.findById(financeId);

		VendorCompany vendorCompany = vendorCompanyService.findByIds(user.getCompanyId());

		List<User> users = userDetailsService.getAllUserByVendorCompanyId(vendorCompany.getId());

		List<Invoice> invoices = users.stream().map(u -> invoiceRespository.findByUserAndIsPaymentStatus(u,false))
				.flatMap(Collection::stream).collect(Collectors.toList());

		List<InvoiceDetailsModel> invoiceDetailsModels = invoices.stream().map(invoice -> invoiceService.convertToModel(invoice)).collect(Collectors.toList());

		if(invoiceDetailsModels.size()<1){
			throw new InvoiceException(AppConstant.ErrorTypes.INVOICE_NOT_FOUND,
					AppConstant.ErrorCodes.INVOICE_NOT_FOUND,AppConstant.ErrorMessages.INVOICE_NOT_FOUND);
		}

		if(((page*size)+size)<invoiceDetailsModels.size()){
			return invoiceDetailsModels.subList(page * size, (page * size) + size);
		}
		else if((page*size)<invoiceDetailsModels.size()){
			return invoiceDetailsModels.subList(page * size, invoiceDetailsModels.size());
		}
		else{
			return invoiceDetailsModels;
		}
	}
}