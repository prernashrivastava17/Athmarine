package com.athmarine.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.athmarine.entity.Invoice;
import com.athmarine.entity.ServiceRequestInvoice;
import com.athmarine.exception.InvoiceException;
import com.athmarine.exception.PurchaseOrderException;
import com.athmarine.repository.InvoiceRespository;
import com.athmarine.repository.ServiceRequestInvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.VendorCompany;
import com.athmarine.entity.Voucher;
import com.athmarine.exception.VendorCompanyNotFoundException;
import com.athmarine.repository.VoucherRepository;
import com.athmarine.request.VendorCompanyModel;
import com.athmarine.request.VoucherModel;
import com.athmarine.resources.AppConstant;

@Service
public class VoucherService {

	@Autowired
	VoucherRepository voucherRepository;

	@Autowired
	VendorCompanyService vendorCompanyService;

	@Autowired
	ServiceRequestInvoiceRepository serviceRequestInvoiceRepository;

	public VoucherModel createVoucher(VoucherModel voucherModel) {

		return convertToModel(voucherRepository.save(convertToEntity(voucherModel)));

	}

	public Integer getAllVoucher() {
		return voucherRepository.getAllVoucher();

	}
	
	public String generateVoucher(Integer count) {
		
		StringBuffer voucher = new StringBuffer("");
		voucher.append("ATH");
		voucher.append(LocalDateTime.now().getYear() % 2000);
		voucher.append(String.format("%06d", count));
		return voucher.toString();
		
	}

	public Voucher findVoucherById(Integer id) {

		if (id == null || id == 0) {

			throw new VendorCompanyNotFoundException(AppConstant.ErrorTypes.VENDOR_COMPANY_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.VENDOR_COMPANY_EXIST_ERROR_CODE,
					AppConstant.ErrorMessages.VENDOR_COMPANY_NOT_EXIST_MESSAGE);
		} else {
			return voucherRepository.findById(id)
					.orElseThrow(() -> new VendorCompanyNotFoundException(
							AppConstant.ErrorTypes.MASTER_PROMOTION_NOT_EXIST_ERROR,
							AppConstant.ErrorCodes.VENDOR_COMPANY_EXIST_ERROR_CODE,
							AppConstant.ErrorMessages.MASTER_PROMOTION_NOT_EXIST_MESSAGE));
		}

	}

	public Voucher convertToEntity(VoucherModel model) {
		VendorCompany company = vendorCompanyService.findByIds(model.getCompanyId());

		return Voucher.builder().id(model.getId()).voucherNumber(model.getVoucherNumber())
				.voucherAmount(model.getVoucherAmount()).isRegistration(model.isRegistration()).companyId(company)
				.status(model.getStatus()).build();
	}

	public VoucherModel convertToModel(Voucher entity) {

		VendorCompanyModel company = vendorCompanyService.findById(entity.getCompanyId().getId());
		return VoucherModel.builder().id(entity.getId()).voucherNumber(entity.getVoucherNumber())
				.voucherAmount(entity.getVoucherAmount()).isRegistration(entity.isRegistration())
				.companyId(company.getId()).status(entity.getStatus()).build();
	}


	//-----------Method to Get All Vouchers By Company Id------------//

	public List<VoucherModel> getVouchersByCompanyId(Integer companyId){

		vendorCompanyService.findByIds(companyId);

		List<Voucher> vouchers = voucherRepository.findAllByCompanyId(companyId);

		if(vouchers.size()<1){
			throw new InvoiceException(AppConstant.ErrorTypes.VOUCHERS_NOT_AVAILABLE,
					AppConstant.ErrorCodes.VOUCHERS_NOT_AVAILABLE,AppConstant.ErrorMessages.VOUCHERS_NOT_AVAILABLE);
		}
		return vouchers.stream().map(this::convertToModel).collect(Collectors.toList());
	}


	//----------Method to Redeem Voucher---------------//

	public String redeemVoucher(Integer voucherId, Integer invoiceId){

		Voucher voucher = findVoucherById(voucherId);

		Optional<ServiceRequestInvoice> serviceRequestInvoice = serviceRequestInvoiceRepository.findById(invoiceId);

		if (!serviceRequestInvoice.isPresent()) {
			throw new PurchaseOrderException(AppConstant.ErrorTypes.INVOICE_NOT_FOUND,
					AppConstant.ErrorCodes.INVOICE_NOT_FOUND, AppConstant.ErrorMessages.INVOICE_NOT_FOUND);
		}

		ServiceRequestInvoice invoice = serviceRequestInvoice.get();

		invoice.setVoucherNumber(voucher.getVoucherNumber());
		invoice.setVoucherDiscount(voucher.getVoucherAmount());

		serviceRequestInvoiceRepository.save(invoice);

		voucher.setRedeemed(true);
		voucherRepository.save(voucher);

		return "Voucher Has Been Successfully Redeemed";

	}

}
