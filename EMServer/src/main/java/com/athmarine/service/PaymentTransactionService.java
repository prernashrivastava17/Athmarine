package com.athmarine.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.Currency;
import com.athmarine.entity.PaymentTransaction;
import com.athmarine.entity.RegistrationStatus;
import com.athmarine.entity.User;
import com.athmarine.entity.VendorCompany;
import com.athmarine.entity.Voucher;
import com.athmarine.exception.UserNotFoundException;
import com.athmarine.repository.CurrencyRepository;
import com.athmarine.repository.PaymentTransactionRepository;
import com.athmarine.repository.UserRepository;
import com.athmarine.repository.VendorCompanyRepository;
import com.athmarine.repository.VoucherRepository;
import com.athmarine.request.EngineerChargesModel;
import com.athmarine.request.PaymentDetailsModel;
import com.athmarine.request.TotalAmount;
import com.athmarine.request.UserModel;
import com.athmarine.request.VoucherModel;
import com.athmarine.resources.AppConstant;
import com.google.gson.Gson;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.EventRequest;
import com.stripe.model.StripeObject;
import com.stripe.net.ApiResource;

@SuppressWarnings("deprecation")
@Service
public class PaymentTransactionService {

	@Autowired
	PaymentTransactionRepository paymentTransactionRepository;

	@Autowired
	UserDetailsServiceImpl userDetailsServiceImpl;

	@Autowired
	UserRepository userRepository;

	@Autowired
	EngineerChargesService engineerChargesService;

	@Autowired
	VendorCompanyService vendorCompanyService;

	@Autowired
	VendorCompanyRepository vendorCompanyRepository;

	@Autowired
	CurrencyRepository currencyRepository;

	@Autowired
	public OTPService otpService;

	@Autowired
	VoucherRepository voucherRepository;

	@Autowired
	VoucherService voucherService;

	public static String key = "sk_test_51JWmGRHbXZa50bvbBU8WEe7tamsYfjkQsmxNtCEmxQamHlk42jjzbiHaoB5BlR9kLdjw7IhSafyhlToGFmmy6rbI00VDqXP4Ym";

	@Autowired
	public PaymentTransactionService() {

		Stripe.apiKey = "sk_test_51JWmGRHbXZa50bvbBU8WEe7tamsYfjkQsmxNtCEmxQamHlk42jjzbiHaoB5BlR9kLdjw7IhSafyhlToGFmmy6rbI00VDqXP4Ym";
	}

	
	public Customer createCustomer(String token, String email) throws StripeException {

		Map<String, Object> customerParams = new HashMap<String, Object>();
		customerParams.put("email", email);
		return Customer.create(customerParams);

	}

	public PaymentDetailsModel chargeNewCard(double amount, String token, String currency, PaymentDetailsModel model)
			throws StripeException {
		Stripe.apiKey = key;

		Map<String, Object> chargeParams = new HashMap<String, Object>();
		chargeParams.put("amount", (int) (amount) * 100);
		chargeParams.put("currency", currency);
		chargeParams.put("source", token);

		String requestJson = new Gson().toJson(model);

		String systemTransactionId = generateTransactionId();

		Map<String, String> initialMetadata = new HashMap<>();
		initialMetadata.put("systemTransactionId", systemTransactionId);
		chargeParams.put("metadata", initialMetadata);

		model.setSystem_transactionId(systemTransactionId);
		model.setRequestJson(requestJson);
		model.setTransactionStatus("pending");

		PaymentDetailsModel paymentDetailsModel = saveInPaymentTrasaction(model);

		Charge.create(chargeParams);
		return paymentDetailsModel;
	}

	public PaymentDetailsModel saveInPaymentTrasactionEntity(PaymentDetailsModel model) throws StripeException {
		User user = userDetailsServiceImpl.findByIds(model.getUser_id());
		VendorCompany vendorCompany = vendorCompanyService.findByIds(user.getCompanyId().getId());
		if (vendorCompany.getReferralCodeUsed() != null && !vendorCompany.getReferralCodeUsed().equals("")) {
			Integer referalCode = vendorCompanyService
					.getAllVendorCompanyByUserReferarralCode(vendorCompany.getReferralCodeUsed());
			VoucherModel voucherModel = new VoucherModel();
			voucherModel.setCompanyId(referalCode);
			voucherModel.setVoucherAmount(10);
			voucherModel.setRegistration(true);
			VoucherModel modleModel = voucherService.createVoucher(voucherModel);
			Integer count = voucherService.getAllVoucher();
			Voucher findVoucher = voucherService.findVoucherById(modleModel.getId());
			findVoucher.setVoucherNumber(voucherService.generateVoucher(count));
			findVoucher.setVoucherAmount(10);
			voucherRepository.save(findVoucher);
		}
		if (model.getMasterPromotionStragyId() == 1) {
			vendorCompany.setRegistrationStatus(RegistrationStatus.Payment);
			vendorCompany.setRegisteredSuccessfully(true);
			vendorCompanyRepository.save(vendorCompany);
			return model;
		} else if (model.getMasterPromotionStragyId() == 2) {

			UserModel userId = userDetailsServiceImpl.findById(model.getUser_id());
			createCustomer(model.getTokenStripe(), userId.getEmail());
			PaymentDetailsModel paymentDetailsModel = chargeNewCard(model.getAmount(), model.getTokenStripe(), model.getCurrency(), model);
			for (int i = 1; i < 3; i++) {
				VoucherModel voucherModel = new VoucherModel();
				voucherModel.setCompanyId(user.getCompanyId().getId());
				voucherModel.setVoucherAmount(50);
				voucherModel.setRegistration(true);
				VoucherModel modleModel = voucherService.createVoucher(voucherModel);
				Integer count = voucherService.getAllVoucher();
				Voucher findVoucher = voucherService.findVoucherById(modleModel.getId());
				findVoucher.setVoucherNumber(voucherService.generateVoucher(count));
				findVoucher.setVoucherAmount(50);
				voucherRepository.save(findVoucher);
			}
			return paymentDetailsModel;

		} else if (model.getMasterPromotionStragyId() == 3) {

			UserModel userId = userDetailsServiceImpl.findById(model.getUser_id());
			createCustomer(model.getTokenStripe(), userId.getEmail());
			PaymentDetailsModel paymentDetailsModel = chargeNewCard(model.getAmount(), model.getTokenStripe(), model.getCurrency(), model);
			VoucherModel voucherModel = new VoucherModel();
			voucherModel.setCompanyId(user.getCompanyId().getId());
			voucherModel.setVoucherAmount(50);
			voucherModel.setRegistration(true);
			VoucherModel modleModel = voucherService.createVoucher(voucherModel);
			Integer count = voucherService.getAllVoucher();
			Voucher findVoucher = voucherService.findVoucherById(modleModel.getId());
			findVoucher.setVoucherNumber(voucherService.generateVoucher(count));
			findVoucher.setVoucherAmount(50);
			voucherRepository.save(findVoucher);
			return paymentDetailsModel;

		} else if (model.getMasterPromotionStragyId() == 3) {

			UserModel userId = userDetailsServiceImpl.findById(model.getUser_id());
			createCustomer(model.getTokenStripe(), userId.getEmail());

			PaymentDetailsModel paymentDetailsModel = chargeNewCard(model.getAmount(), model.getTokenStripe(),
					model.getCurrency(), model);
			VoucherModel voucherModel = new VoucherModel();
			voucherModel.setCompanyId(user.getCompanyId().getId());
			voucherModel.setVoucherAmount(50);
			voucherModel.setRegistration(true);
			VoucherModel modleModel = voucherService.createVoucher(voucherModel);
			Integer count = voucherService.getAllVoucher();
			Voucher findVoucher = voucherService.findVoucherById(modleModel.getId());
			findVoucher.setVoucherNumber(voucherService.generateVoucher(count));
			findVoucher.setVoucherAmount(50);
			voucherRepository.save(findVoucher);
			return paymentDetailsModel;

		} else {
			UserModel userId = userDetailsServiceImpl.findById(model.getUser_id());
			createCustomer(model.getTokenStripe(), userId.getEmail());
			return chargeNewCard(model.getAmount(), model.getTokenStripe(), model.getCurrency(),model);
		}

	}

	public PaymentDetailsModel saveInPaymentTrasaction(PaymentDetailsModel model) {
		User user = userDetailsServiceImpl.findByIds(model.getUser_id());
		PaymentTransaction paymentTransaction = paymentTransactionRepository.findByUserandStatus(user);
		if (paymentTransaction != null) {
			throw new UserNotFoundException(AppConstant.ErrorTypes.PAYMENT_ALREADY_EXIST_ERROR,
					AppConstant.ErrorCodes.PAYMENT_ALREADY_EXIST_ERROR_CODE,
					AppConstant.ErrorMessages.PAYMENT_ALREADY_EXIST_MESSAGE);

		} else {
			for (String listOfEmail : model.getEmailSendToFinance()) {
				String amount = Double.toString(model.getAmount());
				new Thread(new Runnable() {

					@Override
					public void run() {
						otpService.sendInvoiceToFinance(listOfEmail, amount);

					}
				}).start();

			}
			PaymentDetailsModel paymentDetailsModel = convertToModel(
					paymentTransactionRepository.save(convertToEntity(model)));
			VendorCompany vendorCompany = vendorCompanyService.findByIds(user.getCompanyId().getId());
			vendorCompany.setRegistrationStatus(RegistrationStatus.Payment);
			vendorCompany.setRegisteredSuccessfully(true);
			vendorCompanyRepository.save(vendorCompany);
			return paymentDetailsModel;

		}

	}

	public TotalAmount totalAmount(Integer id) {

		Integer totalEngineer = userDetailsServiceImpl.getAllEngineerByCompanyId(id);

		EngineerChargesModel charge = engineerChargesService.findEngineerChargesById();

		Integer charges = 0;

		Integer rem = 0;

		Integer replaceValeOfTotalEngineer = totalEngineer;

		if (totalEngineer <= charge.getBaseEngeerCount()) {
			charges = charge.getBasePrice();

		} else if (totalEngineer <= charge.getExtraAboveEngeerCount()) {
			rem = replaceValeOfTotalEngineer % charge.getBaseEngeerCount();
			charges = charge.getBasePrice() + rem * charge.getExtraAbove();
		} else if (totalEngineer <= charge.getExtraEngeerCount()) {
			rem = replaceValeOfTotalEngineer - charge.getBaseEngeerCount();
			charges = charge.getBasePrice() + rem * charge.getExtraPrice();

		} else if (totalEngineer <= charge.getMaxChargeEngeerCount()) {
			rem = replaceValeOfTotalEngineer - charge.getBaseEngeerCount();
			charges = charge.getBasePrice() + charge.getMaxCharge() * rem;
		} else {
			rem = replaceValeOfTotalEngineer - charge.getBaseEngeerCount();
			charges = charge.getBasePrice() + rem * charge.getMaxCharge();
		}

		VendorCompany vendorCompany = vendorCompanyService.findByIds(id);
		UserModel userModel = userDetailsServiceImpl.findById(id);
		TotalAmount totalAmount = new TotalAmount();

		totalAmount.setTotalEngineer(totalEngineer);
		totalAmount.setTotalAmount(Double.valueOf(charges));
		totalAmount.setInvoiceNumber(InvoiceUniqueIdGenerated());
		totalAmount.setAddress(vendorCompany.getAddress());
		totalAmount.setName(userModel.getName());
		return convertToModelTotalEngineerAndAmount(totalAmount);
	}

	public String InvoiceUniqueIdGenerated() {
		Integer count = vendorCompanyService.getAllVendorCompanyCount();
		StringBuffer invoiceNo = new StringBuffer("");
		invoiceNo.append("PINV");
		invoiceNo.append(LocalDateTime.now().getYear() % 2000);
		if (LocalDateTime.now().getMonthValue() < 10) {
			invoiceNo.append("0" + LocalDateTime.now().getMonthValue());
		} else {
			invoiceNo.append(LocalDateTime.now().getMonthValue());
		}

		if (LocalDateTime.now().getDayOfMonth() < 10) {
			invoiceNo.append("0" + LocalDateTime.now().getDayOfMonth());
		} else {
			invoiceNo.append(LocalDateTime.now().getDayOfMonth());
		}

		invoiceNo.append(String.format("%03d", count));
		return invoiceNo.toString();
	}

	public PaymentDetailsModel getPaymentDetail(Integer userId) {

		User transaction = userDetailsServiceImpl.findByIds(userId);

		return convertToModel(paymentTransactionRepository.findByUser(transaction));
	}

	public TotalAmount convertToModelTotalEngineerAndAmount(TotalAmount totalAmount) {
		return TotalAmount.builder().totalEngineer(totalAmount.getTotalEngineer())
				.totalAmount(totalAmount.getTotalAmount()).name(totalAmount.getName()).address(totalAmount.getAddress())
				.invoiceNumber(totalAmount.getInvoiceNumber()).build();
	}

	public PaymentTransaction convertToEntity(PaymentDetailsModel model) {

		User userModel = userDetailsServiceImpl.findCompanyById(model.getUser_id());

		return PaymentTransaction.builder().id(model.getId()).transactionId(model.getTransactionId())
				.system_transactionId(model.getSystem_transactionId()).currency(model.getCurrency())
				.receiptUrl(model.getReceiptUrl()).currency(model.getCurrency())
				.currencySymbol(model.getCurrencySymbol()).amount(model.getAmount()).paymentMode("ONLINE")
				.transactionLocalDate(LocalDateTime.now()).email(userModel.getEmail()).user(userModel)
				.transactionStatus(model.getTransactionStatus()).receiptUrl(model.getReceiptUrl())
				.request(model.getRequestJson()).build();

	}

	public PaymentDetailsModel convertToModel(PaymentTransaction entity) {

		UserModel userModel = userDetailsServiceImpl.findById(entity.getUser().getId());

		return PaymentDetailsModel.builder().id(entity.getId()).transactionId(entity.getTransactionId())
				.system_transactionId(entity.getSystem_transactionId()).currency(entity.getCurrency())
				.currencySymbol(entity.getCurrencySymbol()).amount(entity.getAmount()).paymentMode("ONLINE")
				.transactionLocalDate(entity.getTransactionLocalDate()).email(userModel.getEmail())
				.transactionStatus(entity.getTransactionStatus()).user_id(userModel.getId()).build();

	}

	// Method To Generate Invoice PDF
	public VendorCompany downloadInvoiceByCompanyID(Integer id, HttpServletResponse response) {

		// load details based on Company Id
		VendorCompany vendorCompany = vendorCompanyService.findByIds(id);
		TotalAmount amount = new TotalAmount();
		amount = totalAmount(id);
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=Invoice.pdf");

		com.itextpdf.text.Document document = new com.itextpdf.text.Document();

		try {

			PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
			// Creating page
			document.open();

			// Styling fonts as per details
			Font font = new Font(Font.FontFamily.HELVETICA, 13);
			Font invoiceFont = new Font(Font.FontFamily.HELVETICA, 24);
			Font TotalAmontFont = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD);
			Font note = new Font(Font.FontFamily.HELVETICA, 10);
			Font Heading = new Font(Font.FontFamily.HELVETICA, 20);

			BaseColor contentColor = WebColors.getRGBColor("#4f2729");
			font.setColor(contentColor);
			BaseColor invoiceColor = WebColors.getRGBColor("#575551");
			invoiceFont.setColor(invoiceColor);
			TotalAmontFont.setColor(contentColor);
			BaseColor NoteColor = WebColors.getRGBColor("#ba2e27");
			note.setColor(NoteColor);
			BaseColor HeadingColor = WebColors.getRGBColor("#575551");
			Heading.setColor(HeadingColor);

			String path = "https://transas.s3.ap-south-1.amazonaws.com/TransasImages/invoice_main_page_ath_marine_page_1.jpg";
			Image image = Image.getInstance(path);
			image.scaleAbsolute(PageSize.A4);
			image.setAbsolutePosition(0, 0);
			document.add(image);
			// Adding Invoice Heading
			Paragraph invoice = new Paragraph("INVOICE", invoiceFont);
			invoice.setAlignment(Element.ALIGN_RIGHT);
			invoice.setIndentationRight(25);
			invoice.setSpacingBefore(80f);
			document.add(invoice);

			// Adding Invoice Number
			Paragraph InvoiceNumber = new Paragraph("Invoice Number: " + amount.getInvoiceNumber(), font);
			InvoiceNumber.setAlignment(Element.ALIGN_RIGHT);
			InvoiceNumber.setIndentationRight(25);
			document.add(InvoiceNumber);

			// Adding Date
			LocalDate localDate = LocalDate.now();
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
			String date1 = localDate.format(dateTimeFormatter);
			Paragraph date = new Paragraph("Date: " +  date1, font);
			date.setAlignment(Element.ALIGN_RIGHT);
			date.setIndentationRight(25);
			document.add(date);

			// Adding Customer Details
			Paragraph CustomerDetails = new Paragraph("Customer Details", Heading);
			CustomerDetails.setAlignment(Element.ALIGN_LEFT);
			CustomerDetails.setIndentationLeft(43);
			CustomerDetails.setSpacingBefore(20f);
			CustomerDetails.setSpacingAfter(10f);
			document.add(CustomerDetails);

			// Adding Name
			Paragraph Name = new Paragraph("Name: " + amount.getName(), font);
			Name.setAlignment(Element.ALIGN_LEFT);
			Name.setIndentationLeft(43);
			document.add(Name);

			// Adding Address
			Paragraph Address = new Paragraph("Address: " + amount.getAddress(), font);
			Address.setAlignment(Element.ALIGN_LEFT);
			Address.setIndentationLeft(43);
			document.add(Address);

			// Adding Description
			Paragraph Description = new Paragraph("Description", Heading);
			Description.setAlignment(Element.ALIGN_LEFT);
			Description.setIndentationLeft(43);
			Description.setSpacingBefore(20f);
			Description.setSpacingAfter(10f);
			document.add(Description);

			// Adding RegistrationCharges
			Paragraph RegistrationCharges = new Paragraph("Registration Charges " + "", font);
			RegistrationCharges.setAlignment(Element.ALIGN_LEFT);
			RegistrationCharges.setIndentationLeft(43);
			document.add(RegistrationCharges);

			// Adding TotalEngineers
			Chunk glue = new Chunk(new VerticalPositionMark());

			Paragraph TotalEngineers = new Paragraph("Number Of Engineers:", font);
			TotalEngineers.add(new Chunk(glue));
			TotalEngineers.add(amount.getTotalEngineer().toString());
			TotalEngineers.setIndentationLeft(43);
			TotalEngineers.setIndentationRight(25);
			document.add(TotalEngineers);

			// Adding Total Amount

			Paragraph TotalAmount = new Paragraph("Total Amount (USD)", font);
			TotalAmount.add(new Chunk(glue));
			TotalAmount.add(amount.getTotalAmount().toString());
			TotalAmount.setIndentationRight(25);
			TotalAmount.setIndentationLeft(43);
			TotalAmount.setSpacingBefore(20);
			document.add(TotalAmount);

			// Adding Payment Favour
			Paragraph PaymentFavour = new Paragraph("Payment in Favour of Ath Marine Pte. Ltd.", Heading);
			PaymentFavour.setAlignment(Element.ALIGN_LEFT);
			PaymentFavour.setIndentationLeft(43);
			PaymentFavour.setSpacingBefore(20f);
			PaymentFavour.setSpacingAfter(10f);
			document.add(PaymentFavour);

			// Adding Remited To
			Paragraph RemitedTo = new Paragraph("Remited To: " + "", font);
			RemitedTo.setAlignment(Element.ALIGN_LEFT);
			RemitedTo.setIndentationLeft(43);
			document.add(RemitedTo);

			Paragraph AthBank = new Paragraph("United Overseas Bank Limited (UOB)" + "", font);
			AthBank.setAlignment(Element.ALIGN_LEFT);
			AthBank.setIndentationLeft(43);
			document.add(AthBank);

			Paragraph AthBankAddress = new Paragraph("9 Crist Road, NYC Cater" + "", font);
			AthBankAddress.setAlignment(Element.ALIGN_LEFT);
			AthBankAddress.setIndentationLeft(43);
			document.add(AthBankAddress);

			Paragraph AthAccNo = new Paragraph("ACC NO: 321-23421-213" + "", font);
			AthAccNo.setAlignment(Element.ALIGN_LEFT);
			AthAccNo.setIndentationLeft(43);
			document.add(AthAccNo);

			Paragraph AthSwiftCode = new Paragraph("SWIFT Code: HASD2SD" + "", font);
			AthSwiftCode.setAlignment(Element.ALIGN_LEFT);
			AthSwiftCode.setIndentationLeft(43);
			document.add(AthSwiftCode);

			Paragraph AthNote = new Paragraph("*All bank changes to be borne by Payer*" + "", note);
			AthNote.setAlignment(Element.ALIGN_LEFT);
			AthNote.setIndentationLeft(43);
			document.add(AthNote);

			// adding Footer
			Paragraph Footer1 = new Paragraph("E. & O. E.", font);
			Footer1.setAlignment(Element.ALIGN_CENTER);
			Footer1.setSpacingBefore(110f);
			document.add(Footer1);

			Paragraph Footer2 = new Paragraph("(This is a computer generated invoice, no signature required)", note);
			Footer2.setAlignment(Element.ALIGN_CENTER);
			document.add(Footer2);

			document.close();
			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return vendorCompany;
	}


	//Webhook implementation
		public Object Webhook(String json, HttpServletRequest request)
		{
			/* Staging Webhook endpoint secret */
//			String endpointSecret = "whsec_Fq7FpyGsSoA0YeQMjx5G6gnxPgsg53Lv";

			/* QA Webhook endpoint secret */
			String endpointSecret = "whsec_b44ls1QYvbIUoqQA19yVBmXEuQx1F0T8";

			/* Local Webhook endpoint secret */
//			String endpointSecret = "whsec_a67b75a96544da062ef3165f6f1141f93bf75baf32ca6e687ae55ef3a0d7d634";

			String sigHeader = request.getHeader("Stripe-Signature");

			Event event = null;

			try {
				event = com.stripe.net.Webhook.constructEvent(json, sigHeader, endpointSecret);
			} catch (SignatureVerificationException e) {
				return "Invalid Signature";
			}

			EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
			StripeObject stripeObject = null;
			if (dataObjectDeserializer.getObject().isPresent()) {
				stripeObject = dataObjectDeserializer.getObject().get();
			}

			Charge charge = null;

			if (!event.getType().equals("customer.created")) {

				assert stripeObject != null;

				charge = ApiResource.GSON.fromJson(stripeObject.toJson(), Charge.class);

				EventRequest eventRequest = event.getRequest();

				Map<String, String> metadata = charge.getMetadata();

				String systemTransactionId = metadata.get("systemTransactionId");

				PaymentTransaction paymentTransaction = paymentTransactionRepository
						.findTxnBySystemTransactionId(systemTransactionId);

				paymentTransaction.setTransactionId(charge.getBalanceTransaction());
				paymentTransaction.setTransactionStatus(charge.getStatus());
				paymentTransaction.setReceiptUrl(charge.getReceiptUrl());
				paymentTransaction.setRequestId(eventRequest.getId());
				paymentTransaction.setIdempotencyKey(eventRequest.getIdempotencyKey());
				paymentTransaction.setResponse(charge.toString());

				Optional<Currency> currency = currencyRepository.findByCurrency(charge.getCurrency());
				Currency currency1 = null;
				if (currency.isPresent()) {
					currency1 = currency.get();
				}

				paymentTransaction.setCurrencySymbol(currency1.getCurrencySymbol());
				paymentTransaction.setLastFour(charge.getPaymentMethodDetails().getCard().getLast4());
				paymentTransactionRepository.save(paymentTransaction);

			}

			return charge;
		}

		public String generateTransactionId() {

			Integer count = paymentTransactionRepository.getAllPayment();
			StringBuffer systemTransactionId = new StringBuffer("");
			systemTransactionId.append("TXN");
			systemTransactionId.append(String.format("%08d", count));
			return systemTransactionId.toString();
		}

		public List<PaymentDetailsModel> getTxnDetailsByUser(int userId) {
			User user = userDetailsServiceImpl.findByIds(userId);

			List<PaymentDetailsModel> paymentDetailsModels = paymentTransactionRepository.findTransactionsByUser(user)
					.stream().map(payment -> convertToModel(payment)).collect(Collectors.toList());
			return paymentDetailsModels;

		}

}
