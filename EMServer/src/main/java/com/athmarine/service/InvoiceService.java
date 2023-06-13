package com.athmarine.service;

import com.athmarine.exception.InvoiceException;
import com.athmarine.resources.AppConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.Invoice;
import com.athmarine.entity.RegistrationStatus;
import com.athmarine.entity.User;
import com.athmarine.entity.VendorCompany;
import com.athmarine.repository.InvoiceRespository;
import com.athmarine.repository.VendorCompanyRepository;
import com.athmarine.request.InvoiceDetailsModel;
import com.athmarine.request.UserModel;

import java.util.Optional;

@Service
public class InvoiceService {

	@Autowired
	UserDetailsServiceImpl userDetailsServiceImpl;

	@Autowired
	InvoiceRespository invoiceRespository;

	@Autowired
	OTPService otpService;

	@Autowired
	VendorCompanyService vendorCompanyService;

	@Autowired
	VendorCompanyRepository vendorCompanyRepository;

	public InvoiceDetailsModel saveToInvoiceEntity(InvoiceDetailsModel model) {

		for (String listOfEmail : model.getEmailSendToFinance()) {
			String amount = Double.toString(model.getAmount());
			new Thread(new Runnable() {

				@Override
				public void run() {
					otpService.sendInvoiceToFinance(listOfEmail, amount);

				}
			}).start();
		}

		InvoiceDetailsModel invoiceDetailsModel = convertToModel(invoiceRespository.save(convertToEntity(model)));

		UserModel user = userDetailsServiceImpl.findById(model.getUser_id());

		VendorCompany vendorCompany = vendorCompanyService.findByIds(user.getCompanyId());
		vendorCompany.setRegistrationStatus(RegistrationStatus.Payment);
		vendorCompany.setRegisteredSuccessfully(true);
		vendorCompanyRepository.save(vendorCompany);

		return invoiceDetailsModel;

	}

	public Invoice convertToEntity(InvoiceDetailsModel model) {

		User userModel = userDetailsServiceImpl.findCompanyById(model.getUser_id());

		return Invoice.builder().id(model.getId()).invoiceNumber(model.getInvoiceNumber())
				.currencySymbol(model.getCurrencySymbol()).amount(model.getAmount())
				.totalEngineer(model.getTotalEngineer()).user(userModel).build();

	}

	public InvoiceDetailsModel convertToModel(Invoice entity) {

		UserModel userModel = userDetailsServiceImpl.findById(entity.getUser().getId());

		return InvoiceDetailsModel.builder().id(entity.getId()).invoiceNumber(entity.getInvoiceNumber())
				.currency(entity.getCurrency()).currencySymbol(entity.getCurrencySymbol())
				.amount(entity.getAmount()).email(userModel.getEmail()).user_id(userModel.getId())
				.totalEngineer(entity.getTotalEngineer()).build();
	}


	public InvoiceDetailsModel getInvoice(Integer id){

		Optional<Invoice> invoice = invoiceRespository.findById(id);

		if(!invoice.isPresent()){
			throw new InvoiceException(AppConstant.ErrorTypes.INVOICE_NOT_FOUND,
					AppConstant.ErrorCodes.INVOICE_NOT_FOUND,AppConstant.ErrorMessages.INVOICE_NOT_FOUND);
		}
		return convertToModel(invoice.get());
	}
}
