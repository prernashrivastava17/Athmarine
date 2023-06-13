package com.athmarine.service;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import javax.validation.constraints.Null;

import com.athmarine.entity.*;
import com.athmarine.exception.*;
import com.athmarine.repository.*;
import com.athmarine.request.*;
import com.athmarine.response.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import org.apache.xmlbeans.impl.xb.xsdschema.Attribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.athmarine.resources.AppConstant;
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

@Service
public class BidsService {

	@Autowired
	public BidsRepository bidsRepository;

	@Autowired
	public UserDetailsServiceImpl userDetailsService;

	@Autowired
	public ServiceRequestService serviceRequestService;

	@Autowired
	MiscellaneousVendorService miscellaneousVendorService;

	@Autowired
	TravelCostVendorService travelCostVendorService;

	@Autowired
	WorkingService workingService;

	@Autowired
	EngineerEquimentService engineerEquimentService;

	@Autowired
	WorkingVendorRepository workingVendorRepository;

	@Autowired
	MiscellaneousVendorRepository miscellaneousVendorRepository;

	@Autowired
	TravelCostVendorRepository travelCostVendorRepository;

	@Autowired
	EngineerBidRelationRepository engineerBidRelationRepository;

	@Autowired
	BidderRequestRelationRepository requestRelationRepository;

	@Autowired
	EngineerRankingRepository rankingRepository;

	@Autowired
	PurchaseOrderRepository purchaseOrderRepository;

	@Autowired
	ServiceRequestRepository serviceRequestRepository;

	@Autowired
	ServiceRequestInvoiceRepository serviceRequestInvoiceRepository;

	@Autowired
	PaymentTransactionService paymentTransactionService;

	@Autowired
	ApproverBidRelationRepository approverBidRelationRepository;

	@Autowired
	BidderRequestRelationRepository bidderRequestRelationRepository;

	@Autowired
	EngineerService engineerService;

	@Autowired
	HeadOfCompanyRepository headOfCompanyRepository;

	@Value("${pagination.size}")
	private Integer size;


	public boolean checkAlreadyExists(int id) {
		return bidsRepository.existsById(id);

	}

	public String createBids(BidsModel bidsModel) throws Exception {
		// Adding Bid details
		Bids bids = bidsRepository.save(convertToEntity(bidsModel));

		if(bids.getBidsStatus().equals(ServiceRequestStatus.APPROVER_PENDING.name())){
			approverBidRelationRepository.save(ApproverBidRelation.builder().bidId(bids.getId())
					.bidStatus(bids.getBidsStatus()).build());
		}

		// Adding Engineers to bid
		addEngineerIdInBidEngineerRelation(bidsModel, bids.getId());

		User userBidder = userDetailsService.findByIds(bidsModel.getBidderId());
		// Get all engineers using company id and add to ranking table with ranking
		AssignInitialRankingToEngineers(userBidder.getCompanyId().getId());
		// Assigning ranking with respected to selected engineer and update their
		// ranking
		AssignRankingToExistingEngineers(bidsModel);
		// Adding Working details

		addWorkingTravelOtherDetails(bidsModel, bids.getId());

		requestRelationRepository.save(BidderRequestRelation.builder().bidderId(bids.getUserId().getId())
				.requestId(bids.getServiceRequest().getId()).BidId(bids.getId()).requestStatus(bids.getBidsStatus())
				.build());

		return "Bid Submit Successfully";
	}

	private void addWorkingTravelOtherDetails(BidsModel bidsModel, Integer id) {
		for (WorkingVendorModel work : bidsModel.getWorkingVendorModel()) {
			work.setBidId(id);

			workingService.createWorkingVendor(work);

		}
		// adding travel details
		for (TravelCostVendorModel travel : bidsModel.getTravelCostVendorModel()) {
			travel.setBidId(id);
			travelCostVendorService.createTravelCostVendor(travel);

		}

		// Adding Other details
		for (MiscellaneousVendorModel miscellaneousVendorModel : bidsModel.getMiscellaneousVendorModel()) {

			miscellaneousVendorModel.setBidId(id);
			miscellaneousVendorService.createMiscellaneousVendor(miscellaneousVendorModel);

		}

	}

	private void addEngineerIdInBidEngineerRelation(BidsModel bidsModel, Integer id) throws Exception {

		int count = bidsModel.getProposedEngineers().size();

		if (count < 1)
			throw new Exception("Please Select an Engineer");

		ServiceRequest serviceRequest = serviceRequestRepository.findByRequestId(bidsModel.getServiceRequestId());

		Calendar calendar = Calendar.getInstance();

		calendar.setTime(serviceRequest.getServiceRequestOn());

		calendar.add(Calendar.HOUR_OF_DAY, bidsModel.getExpectedHoursNeeded());

		List<BidEngineerRelation> engineerList = engineerBidRelationRepository.findByBidId(id);
		if (engineerList.size() == 0) {
			for (ProposedEngineers list : bidsModel.getProposedEngineers()) {
				engineerBidRelationRepository.save(BidEngineerRelation.builder().bidId(id).status(true)
						.engineerId(list.getEngineerId()).serviceOn(serviceRequest.getServiceRequestOn())
						.serviceEndOn(calendar.getTime()).engineerStatus(list.getStatus())
						.serviceRequest(serviceRequest)
						.build());
			}
		} else {
			for (int i = 0; i < engineerList.size(); i++) {
				BidEngineerRelation relation = engineerList.get(i);

				engineerBidRelationRepository.save(BidEngineerRelation.builder().id(relation.getId()).bidId(id)
						.status(true).engineerId(bidsModel.getProposedEngineers().get(i).getEngineerId())
						.serviceOn(serviceRequest.getServiceRequestOn()).serviceEndOn(calendar.getTime())
						.engineerStatus(bidsModel.getProposedEngineers().get(i).getStatus())
						.serviceRequest(serviceRequest).build());
			}
		}

	}

	private void AssignRankingToExistingEngineers(BidsModel bidsModel) {

		List<EngineerRanking> ranking = rankingRepository.findAllEngineerRankingByCompanyId(bidsModel.getCompanyId());

		for (EngineerRanking engineerRanking : ranking) {

			for (ProposedEngineers engineerlist : bidsModel.getProposedEngineers()) {
				if (engineerRanking.getEngineerId().equals(engineerlist.getEngineerId())) {

					Float existingRank = Float.parseFloat(engineerRanking.getRanking());
					Integer RequirdEngineer = bidsModel.getNoOfEngineer();
					Integer proposedEngineer = bidsModel.getProposedEngineers().size();
					Float result = (float) RequirdEngineer / proposedEngineer;
					String rank = String.valueOf(result + existingRank);
					engineerRanking.setRanking(rank);
					rankingRepository.save(engineerRanking);
				}

			}
		}

	}

	private void AssignInitialRankingToEngineers(Integer id) {

		List<EngineerEquimentsModel> engineerEquimentsModels = engineerEquimentService
				.getEngineerEquipmentByCompanyId(id);

		for (EngineerEquimentsModel engineerEquimentsModel : engineerEquimentsModels) {

			EngineerRanking rank = rankingRepository.findByEngineerId(engineerEquimentsModel.getUserModel().getId());
			if (rank == null) {
				rankingRepository.save(EngineerRanking.builder().companyId(id)
						.engineerId(engineerEquimentsModel.getUserModel().getId()).ranking("0").build());
			}
		}

	}

	public BidsModel getBidById(Integer id) {
		if (id == null) {
			throw new BidsNotFoundException(AppConstant.ErrorTypes.BIDS_EXIST_ERROR,
					AppConstant.ErrorCodes.BIDS_ERROR_CODE, AppConstant.ErrorMessages.BIDS_NOT_EXIST);
		}

		Bids bids = findByIds(id);
		List<WorkingVendor> workingVendor = workingVendorRepository.findAllByBids(bids);
		List<MiscellaneousVendor> miscellaneousVendors = miscellaneousVendorRepository.findAllMiscellaneousByBids(bids);
		List<TravelCostVendor> travelCostVendors = travelCostVendorRepository.findAllTravelByBids(bids);
		List<BidEngineerRelation> bidEngineerRelations = engineerBidRelationRepository.findByBidId(bids.getId());

		return BidsModel.builder().id(bids.getId()).bidUid(bids.getBidUID()).bidderId(bids.getUserId().getId()).companyId(bids.getCompanyId())
				.expectedHoursNeeded(bids.getExpectedHrsNeeded()).noOfEngineer(bids.getNoOfEngineer())
				.status(bids.isStatus()).bidStatus(bids.getBidsStatus())
				.serviceRequestId(bids.getServiceRequest().getId()).totalAmountGet(bids.getTotalAmountGet())
				.totalProposedAmount(bids.getTotalProposedAmount()).warranty(bids.getWarranty())
				.totalTax(bids.getTotalTax())
				.workingVendorModel(workingVendor.stream().map(working -> workingService.convertToModels(working))
						.collect(Collectors.toList()))
				.miscellaneousVendorModel(miscellaneousVendors.stream()
						.map(other -> miscellaneousVendorService.convertToModel(other)).collect(Collectors.toList()))
				.travelCostVendorModel(travelCostVendors.stream()
						.map(travel -> travelCostVendorService.convertToModel(travel)).collect(Collectors.toList()))
				.proposedEngineers(bidEngineerRelations.stream().map(engineer -> convertToProposedModel(engineer))
						.collect(Collectors.toList()))
				.build();
	}

	private ProposedEngineers convertToProposedModel(BidEngineerRelation bidEngineerRelation) {

		return ProposedEngineers.builder().engineerId(bidEngineerRelation.getEngineerId()).build();
	}

	public EngineerResponseModel getEngineer(Integer id, Integer requestId) {

		List<EngineerEquimentsModel> engineerEquimentsModels = engineerEquimentService
				.getEngineerEquipmentByCompanyId(id);

		for (EngineerEquimentsModel engineerEquimentsModel : engineerEquimentsModels) {
			EngineerRanking rank = rankingRepository.findByEngineerId(engineerEquimentsModel.getUserModel().getId());
			if (rank == null) {
				rankingRepository.save(EngineerRanking.builder().companyId(id)
						.engineerId(engineerEquimentsModel.getUserModel().getId()).ranking("0").build());
			}
		}

		List<EngineerRanking> details = rankingRepository.findAllEngineerRankingByCompanyId(id);
		List<EngineerRankingResponseModel> ranking=converToRankModel(details);
		engineerEquimentsModels.forEach(engineer->{
			ranking.forEach(rank->{
				if(rank.getEngineerId().equals(engineer.getUserModel().getId()))
				{
					rank.setEngineerName(engineer.getUserModel().getName());
				}
			});
		});
		// Check For allocated Logic
		List<BidEngineerRelation> engineerRelations = engineerBidRelationRepository.findByEngineerStatus("ASSIGNED");

		ServiceRequest serviceRequest = serviceRequestRepository.findByRequestId(requestId);
		if (serviceRequest==null)
			throw  new ServiceRequestNotFoundException(AppConstant.ErrorTypes.SERVICE_REQUEST_EXIST_ERROR,AppConstant.ErrorCodes.SERVICE_REQUEST_ERROR_CODE
			,AppConstant.ErrorMessages.SERVICE_REQUEST_NOT_EXIST);

		for (EngineerRankingResponseModel rank : ranking) {
			for (BidEngineerRelation engineer : engineerRelations) {
				if (rank.getEngineerId().equals(engineer.getEngineerId())) {

					if (serviceRequest.getServiceRequestOn().getDate() >= engineer.getServiceOn().getDate()
							&& serviceRequest.getServiceRequestOn().getMonth() >= engineer.getServiceOn().getMonth()
							&& serviceRequest.getServiceRequestOn().getYear() >= engineer.getServiceOn().getYear()) {
						if (serviceRequest.getServiceRequestOn().getDate() <= engineer.getServiceEndOn().getDate()
								&& serviceRequest.getServiceRequestOn().getMonth() <= engineer.getServiceEndOn()
										.getMonth()
								&& serviceRequest.getServiceRequestOn().getYear() <= engineer.getServiceEndOn()
										.getYear()) {
							rank.setColor("Red");

						}
					} else {
						Float Erank = Float.parseFloat(rank.getRanking());
						if (Erank < 1)
							rank.setColor("Green");
						else
							rank.setColor("Orange");
					}

				}
			}
			if (rank.getColor() == null) {
				Float Erank = Float.parseFloat(rank.getRanking());
				if (Erank < 1)
					rank.setColor("Green");
				else
					rank.setColor("Orange");
			}

		}

		return EngineerResponseModel.builder().engineerEquimentsModels(engineerEquimentsModels)
				.engineerRankings(ranking).build();
	}

	private List<EngineerRankingResponseModel> converToRankModel(List<EngineerRanking> ranking) {
		List<EngineerRankingResponseModel> responseModels= new ArrayList<EngineerRankingResponseModel>();
		ranking.forEach( rank->{
			responseModels.add(EngineerRankingResponseModel.builder()
							.id(rank.getId())
							.engineerId(rank.getEngineerId())
							.color(rank.getColor())
							.companyId(rank.getCompanyId())
							.ranking(rank.getRanking())
					.build());
		});
		return responseModels;
	}

	public Bids findByIds(Integer id) {
		if (id == null) {

			throw new BidsNotFoundException(AppConstant.ErrorTypes.BIDS_EXIST_ERROR,
					AppConstant.ErrorCodes.BIDS_ERROR_CODE, "Bids " + AppConstant.ErrorMessages.ID_EMPTY_MESSAGE);

		}
		return bidsRepository.findById(id)
				.orElseThrow(() -> new BidsNotFoundException(AppConstant.ErrorTypes.BIDS_EXIST_ERROR,
						AppConstant.ErrorCodes.BIDS_ERROR_CODE, AppConstant.ErrorMessages.BIDS_NOT_EXIST));
	}

	public Bids convertToEntity(BidsModel bidModel) throws Exception {

		List<Bids> bidsList = bidsRepository.findByBidderAndRequestId(bidModel.getBidderId(),
				serviceRequestService.findByIds(bidModel.getServiceRequestId()).getId(),
				ServiceRequestStatus.BIDS_IN_PROGRESS.name());

		if (bidsList.size() > 0) {
			for (Bids b : bidsList) {
				checkBidValidOrNot(bidModel.getTotalProposedAmount(), b.getTotalProposedAmount());
			}
		}

		User userBidder = userDetailsService.findByIds(bidModel.getBidderId());

		ServiceRequest serviceRequest = serviceRequestService.findByIds(bidModel.getServiceRequestId());

		String bidStatus = null;
		if(bidModel.getBidStatus()!=null) {
			if (bidModel.getBidStatus().equals(ServiceRequestStatus.HOC_APPROVAL_PENDING.name())) {
				bidStatus = bidModel.getBidStatus();
			}
			else if (bidModel.getBidStatus().equalsIgnoreCase("APPROVER_PENDING")) {
				bidStatus = ServiceRequestStatus.BIDS_IN_PROGRESS.toString();
//				approverBidRelationRepository.changeBidStatusById(bidModel.getId(),"Approved",null);
			}
		}
		else if (bidModel.getTotalAmountGet() <= Integer.parseInt(userBidder.getBiddingLimit().replaceAll(",", ""))) {
				bidStatus = ServiceRequestStatus.BIDS_IN_PROGRESS.toString();
		}
		else {
				bidStatus = ServiceRequestStatus.APPROVER_PENDING.toString();
		}

		String uidBid = bidModel.getBidUid();
		if (uidBid == null)
			uidBid = generateUId(userBidder.getCompanyId().getId(), serviceRequest.getId());

		return Bids.builder().id(bidModel.getId()).bidUID(uidBid).userId(userBidder)
				.companyId(userBidder.getCompanyId().getId()).serviceRequest(serviceRequest).bidsStatus(bidStatus)
				.status(bidModel.isStatus()).noOfEngineer(bidModel.getNoOfEngineer())
				.totalAmountGet(bidModel.getTotalAmountGet()).totalProposedAmount(bidModel.getTotalProposedAmount())
				.expectedHrsNeeded(bidModel.getExpectedHoursNeeded()).warranty(bidModel.getWarranty())
				.additionalCostStatus(ServiceRequestStatus.NOT_RAISED.name()).build();
	}

	private String generateUId(Integer companyId, Integer requestId) {

		List<Bids> sequenceNumber = bidsRepository.findSequenceNumber(requestId, companyId);
		StringBuilder uId = new StringBuilder();
		uId.append("BID");
		uId.append(LocalDateTime.now().getYear() % 2000);
		Random Number = new Random();
		Integer sevenDigitNumber = Number.nextInt(9999999);
		uId.append(sevenDigitNumber);
		uId.append("-");
		uId.append(String.format("%02d", sequenceNumber.size() + 1));
		return uId.toString();
	}

	// ------------Method to get all bids by bidder id and status------------//

	public List<RequestBidsResponseModel> getAllVendorBidsByStatus(Integer id, ServiceRequestStatus requestStatus,
																   Integer page) {

		if(page==null){
			throw new PaginationException(AppConstant.ErrorTypes.PAGE_NUMBER_NULL,
					AppConstant.ErrorCodes.PAGE_NUMBER_NULL,AppConstant.ErrorMessages.PAGE_NUMBER_NULL);
		}

		if(requestStatus==null){
			throw new ServiceRequestStatusException(AppConstant.ErrorTypes.STATUS_IS_NULL,
					AppConstant.ErrorCodes.STATUS_IS_NULL, AppConstant.ErrorMessages.STATUS_IS_NULL);
		}

		User user = userDetailsService.findByIds(id);

		serviceRequestService.updateAllRequestsStatus(user);

		List<Bids> bids = new ArrayList<>();
		if(requestStatus.equals(ServiceRequestStatus.WITHDRAWN)) {
			bids = bidsRepository.findByBidderId(id, requestStatus.name(), false, PageRequest.of(page,size));
		}
		else {
			bids = bidsRepository.findByBidderId(id, requestStatus.name(), true, PageRequest.of(page,size));
		}

		if (bids.size() > 0) {
			return bids.stream().map(this::convertToRequestBidsResponseModel).collect(Collectors.toList());
		} else {
			throw new BidsNotFoundException(AppConstant.ErrorTypes.BIDS_EXIST_ERROR,
					AppConstant.ErrorCodes.BIDS_ERROR_CODE, AppConstant.ErrorMessages.BIDS_NOT_EXIST);
		}
	}

	// -------------Method to convert Bids to RequestBidsResponseModel-----------------//

	public RequestBidsResponseModel convertToRequestBidsResponseModel(Bids bid) {

		String declineReason = null;

		if(bid.getBidsStatus().equals(ServiceRequestStatus.DECLINED.name())){
			declineReason = approverBidRelationRepository.findByBidId(bid.getId()).getDeclineReason();
		}
		return RequestBidsResponseModel.builder().bidId(bid.getId()).bidUID(bid.getBidUID())
				.requestUID(bid.getServiceRequest().getRequestUID())
				.vessel(bid.getServiceRequest().getVessel().getShipname())
				.category(bid.getServiceRequest().getEquipmentCategory().getName())
				.country(bid.getServiceRequest().getCountry().getName())
				.port(bid.getServiceRequest().getPort().getName()).eta(bid.getServiceRequest().getETA())
				.etd(bid.getServiceRequest().getETD()).serviceRequestOn(bid.getServiceRequest().getServiceRequestOn())
				.remainingTime(serviceRequestService.getRemainingTimeForBidding(
						bid.getServiceRequest().getQuotationTime(), bid.getServiceRequest().getCreatedAt()))
				.declineReason(declineReason).build();
	}

	// -----------Method to Withdraw a bid--------------//

	public String withdrawBid(Integer bidId) {
		Bids bid = findByIds(bidId);
		bid.setBidsStatus(ServiceRequestStatus.WITHDRAWN.name());
		bid.setStatus(false);
		bidsRepository.save(bid);

		BidderRequestRelation bidderRequestRelation = requestRelationRepository.findByBidId(bidId);

		if (bidderRequestRelation != null)
			requestRelationRepository.delete(bidderRequestRelation);

		return "Bid Has Been Withdrawn";
	}

	// ------------Method to check If Bid Amount is valid or not--------------//

	public void checkBidValidOrNot(Integer newBidAmount, Integer previousBidAmount) throws Exception {

		float twentyFivePercent = previousBidAmount * 0.25f;

		if (newBidAmount < (previousBidAmount + twentyFivePercent)) {
			if (newBidAmount > (previousBidAmount - twentyFivePercent)) {
				throw new BidsNotFoundException(AppConstant.ErrorTypes.INVALID_BID_AMOUNT_ERROR,
						AppConstant.ErrorCodes.INVALID_BID_AMOUNT_ERROR_CODE,
						AppConstant.ErrorMessages.INVALID_BID_AMOUNT);
			}
		}
	}

	// ------------Method to Calculate Fare Value------------//

	public void calculateFareValue(Integer requestId) {

		List<Bids> bids = bidsRepository.findBidsByRequestIdAndBidStatus(requestId,
				ServiceRequestStatus.BIDS_IN_PROGRESS.name());

		int[] workingList = new int[]{0,0,0,0,0,0,0,0,0};
		int[] travelList = new int[]{0,0,0,0,0,0,0,0,0};
		int[] otherList = new int[]{0,0,0,0,0,0,0,0,0,0};

		for (Bids bid : bids) {

			List<WorkingVendor> workingVendorList = bid.getWorkingVendor();

			for(int i=0; i<workingList.length;i++)
			{
				workingList[i] += workingVendorList.get(i).getProposedAmount();
			}


			List<TravelCostVendor> travelCostVendorList = bid.getTravelCostVendor();

			for(int i=0; i<travelList.length;i++)
			{
				travelList[i] += travelCostVendorList.get(i).getProposedAmount();
			}


			List<MiscellaneousVendor> miscellaneousVendorList = bid.getMiscellaneousVendor();

			for(int i=0; i<otherList.length; i++)
			{
				otherList[i] += miscellaneousVendorList.get(i).getProposedAmount();
			}
		}


		for (Bids bid : bids) {

			List<WorkingVendor> workingVendorList = bid.getWorkingVendor();

			for(int i=0; i<workingList.length;i++)
			{
				workingVendorList.get(i).setFareValue(workingList[i]/bids.size());
			}

			workingVendorRepository.saveAll(workingVendorList);

			List<TravelCostVendor> travelCostVendorList = bid.getTravelCostVendor();

			for(int i=0; i<travelList.length;i++)
			{
				travelCostVendorList.get(i).setFareValue(travelList[i]/bids.size());
			}

			travelCostVendorRepository.saveAll(travelCostVendorList);

			List<MiscellaneousVendor> miscellaneousVendorList = bid.getMiscellaneousVendor();

			for(int i=0; i<otherList.length;i++)
			{
				miscellaneousVendorList.get(i).setFareValue(otherList[i]/bids.size());
			}

			miscellaneousVendorRepository.saveAll(miscellaneousVendorList);
		}

		bids.forEach(bid -> {
			bid.setTotalFareValue((workingList[8]/bids.size())+(travelList[8]/bids.size())
					+(otherList[9]/bids.size()));
			bidsRepository.save(bid);
		});
	}

	// -----------Method to raise PO---------------//

	public String raisePO(Integer bidId) {

		Bids bid = findByIds(bidId);

		if (purchaseOrderRepository.findByBid(bid) != null) {
			throw new PurchaseOrderException(AppConstant.ErrorTypes.PO_ALREADY_RAISED_EXCEPTION,
					AppConstant.ErrorCodes.PO_ALREADY_RAISED, AppConstant.ErrorMessages.PO_ALREADY_RAISED);
		}

		User user = userDetailsService.findByIds(bid.getUserId().getId());

		bid.setBidsStatus(ServiceRequestStatus.PO_RAISED.name());

		bidsRepository.save(bid);

		PurchaseOrder purchaseOrder = purchaseOrderRepository.save(PurchaseOrder.builder().bid(bid).serviceRequest(bid.getServiceRequest())
				.currency(bid.getServiceRequest().getPreferredCurrency())
				.poStatus(ServiceRequestStatus.PO_RAISED.name()).poUID(generatePOUID()).build());
		ServiceRequest serviceRequest=serviceRequestService.findByIds(bid.getServiceRequest().getId());
		UserModel hoc = userDetailsService.getUserHeadByCompanyId(user.getCompanyId().getId());
		headOfCompanyRepository.save(HeadOfCompany.builder().headId(hoc.getId())
				.statusId(purchaseOrder.getId()).status("PO_RECEIVED")
				.serviceOn(serviceRequest.getServiceRequestOn())
				.build());

		return "PO Raised Successfully!";

	}

	// ----------Method to accept PO--------//

	public String acceptPO(Integer poId) {

		Optional<PurchaseOrder> purchaseOrder = purchaseOrderRepository.findById(poId);
		if (!purchaseOrder.isPresent()) {
			throw new PurchaseOrderException(AppConstant.ErrorTypes.PO_NOT_FOUND, AppConstant.ErrorCodes.PO_NOT_FOUND,
					AppConstant.ErrorMessages.PO_NOT_FOUND);
		}

		PurchaseOrder pOrder = purchaseOrder.get();

		Bids bid = pOrder.getBid();

		bid.setBidsStatus(ServiceRequestStatus.PO_ACCEPTED.name());
		bidsRepository.save(bid);

		pOrder.setPoStatus(ServiceRequestStatus.PO_ACCEPTED.name());
		purchaseOrderRepository.save(pOrder);
		return "PO Accepted Successfully!";
	}

	// ----------Method to decline PO--------//

	public String declinePO(Integer poId) {

		Optional<PurchaseOrder> purchaseOrder = purchaseOrderRepository.findById(poId);

		if (!purchaseOrder.isPresent()) {
			throw new PurchaseOrderException(AppConstant.ErrorTypes.PO_NOT_FOUND, AppConstant.ErrorCodes.PO_NOT_FOUND,
					AppConstant.ErrorMessages.PO_NOT_FOUND);
		}

		PurchaseOrder pOrder = purchaseOrder.get();

		Bids bid = pOrder.getBid();

		bid.setBidsStatus(ServiceRequestStatus.PO_DECLINED.name());
		bid.setStatus(false);
		bidsRepository.save(bid);

		pOrder.setPoStatus(ServiceRequestStatus.PO_DECLINED.name());
		pOrder.setStatus(false);
		purchaseOrderRepository.save(pOrder);

		return "PO Declined Successfully!";
	}

	// ---------Method to generate PO UID-------------//

	public String generatePOUID() {
		StringBuilder uid = new StringBuilder();
		uid.append("PO");
		uid.append(LocalDateTime.now().getYear() % 2000);
		Random rnd = new Random();
		int number = rnd.nextInt(999999);
		uid.append(String.format("%06d", number));
		return uid.toString();
	}

	public void updatePOs() {

		List<PurchaseOrder> purchaseOrders = purchaseOrderRepository
				.findByPoStatusAndStatus(ServiceRequestStatus.PO_RAISED.name(), true);

		purchaseOrders.forEach(purchaseOrder -> {

			long leftOutTime = getPOLeftOutTime(purchaseOrder.getCreatedAt());

			long twentyFourHrsInMillis = 24 * 60 * 60 * 1000;

			if (leftOutTime >= twentyFourHrsInMillis) {
				purchaseOrder.setPoStatus(ServiceRequestStatus.TIMED_OUT.name());
				purchaseOrderRepository.save(purchaseOrder);
			}
		});
	}

	// --------Method to get left out time for accepting PO-----------//

	public long getPOLeftOutTime(Date createdAt) {

		Date currentDate = new Date();

		return Math.abs(currentDate.getTime() - createdAt.getTime());
	}

	// ----------Method to GET All Raised POs By Bidder Id--------//

	public List<RequestBidsResponseModel> getAllRaisedPOs(Integer bidderId, Integer page) {

		if(page==null){
			throw new PaginationException(AppConstant.ErrorTypes.PAGE_NUMBER_NULL,
					AppConstant.ErrorCodes.PAGE_NUMBER_NULL,AppConstant.ErrorMessages.PAGE_NUMBER_NULL);
		}

		updatePOs();

		User user = userDetailsService.findByIds(bidderId);

		List<Bids> bids = bidsRepository.findByUserIdAndBidsStatusAndStatus(user, ServiceRequestStatus.PO_RAISED.name(),
				true,PageRequest.of(page,size));

		if(bids.size()<1){
			throw new PurchaseOrderException(AppConstant.ErrorTypes.PO_NOT_FOUND,
					AppConstant.ErrorCodes.PO_NOT_FOUND, AppConstant.ErrorMessages.PO_NOT_FOUND);
		}

		return bids.stream().map(bid -> {

			PurchaseOrder purchaseOrder = purchaseOrderRepository.findByBid(bid);

			long leftOutTime = getPOLeftOutTime(purchaseOrder.getCreatedAt());

			long twentyFourHrsInMillis = 24 * 60 * 60 * 1000;

			return RequestBidsResponseModel.builder().requestUID(bid.getServiceRequest().getRequestUID())
					.poId(purchaseOrder.getId()).poUID(purchaseOrder.getPoUID())
					.vessel(bid.getServiceRequest().getVessel().getShipname())
					.category(bid.getServiceRequest().getEquipmentCategory().getName())
					.country(bid.getServiceRequest().getCountry().getName())
					.port(bid.getServiceRequest().getPort().getName()).etd(bid.getServiceRequest().getETD())
					.eta(bid.getServiceRequest().getETA()).remainingTime(twentyFourHrsInMillis - leftOutTime)
					.serviceRequestOn(bid.getServiceRequest().getServiceRequestOn()).build();

		}).collect(Collectors.toList());
	}

	public String updateInterestedToNotInterested(BidderRequestRelationModel model) {

		BidderRequestRelation bidderRequestRelation = requestRelationRepository
				.findByBidderIdAndRequestId(model.getBidderId(), model.getRequestId(), "INTERESTED");
		if (bidderRequestRelation == null)
			throw new ServiceRequestNotFoundException(AppConstant.ErrorCodes.SERVICE_REQUEST_ERROR_CODE,
					AppConstant.ErrorMessages.SERVICE_REQUEST_NOT_EXIST,
					AppConstant.ErrorTypes.SERVICE_REQUEST_EXIST_ERROR);
		
			bidderRequestRelation.setRequestStatus(ServiceRequestStatus.NOT_INTERESTED.toString());
			requestRelationRepository.save(bidderRequestRelation);
		
		return "Update Successfully";

	}

	// -----------Method to update Bid--------------//
	public String updateSubmitedBid(BidsModel bidsModel) throws Exception {

		BidsModel previousBid = getBidById(bidsModel.getId());

		if(!bidsModel.getServiceRequestId().equals(previousBid.getServiceRequestId())){
			throw new ServiceRequestNotFoundException(AppConstant.ErrorTypes.INVALID_SERVICE_REQUEST_ID,
					AppConstant.ErrorCodes.INVALID_SERVICE_REQUEST_ID,AppConstant.ErrorMessages.INVALID_SERVICE_REQUEST_ID);
		}

		if(!bidsModel.getBidderId().equals(previousBid.getBidderId())){
			throw new UserNotFoundException(AppConstant.ErrorTypes.INVALID_BIDDER_ID,
					AppConstant.ErrorCodes.INVALID_BIDDER_ID,AppConstant.ErrorMessages.INVALID_BIDDER_ID);
		}

		Bids bid = bidsRepository.save(convertToEntity(bidsModel));
		updateEngineers(previousBid, bidsModel);
		addWorkingTravelOtherDetails(bidsModel, bid.getId());

		BidderRequestRelation requestRelation = requestRelationRepository.findByBidId(bid.getId());
		requestRelationRepository.save(BidderRequestRelation.builder().id(requestRelation.getId())
				.bidderId(bid.getUserId().getId()).requestId(bid.getServiceRequest().getId()).BidId(bid.getId())
				.requestStatus(bid.getBidsStatus()).build());
		return "Bid Updated Successfully";
	}

	private void updateEngineers(BidsModel previousBid, BidsModel bidsModel) throws Exception {

		addEngineerIdInBidEngineerRelation(bidsModel, bidsModel.getId());

		minusRankingOfPreviousEngineers(previousBid);
		AssignRankingToExistingEngineers(bidsModel);

	}

	private void minusRankingOfPreviousEngineers(BidsModel previousBid) {
		List<EngineerRanking> ranking = rankingRepository.findAllEngineerRankingByCompanyId(previousBid.getCompanyId());

		for (EngineerRanking engineerRanking : ranking) {

			for (ProposedEngineers engineerlist : previousBid.getProposedEngineers()) {
				if (engineerRanking.getEngineerId().equals(engineerlist.getEngineerId())) {

					Float existingRank = Float.parseFloat(engineerRanking.getRanking());
					Integer RequirdEngineer = previousBid.getNoOfEngineer();
					Integer proposedEngineer = previousBid.getProposedEngineers().size();
					Float result = (float) RequirdEngineer / proposedEngineer;
					String rank = String.valueOf(existingRank - result);
					engineerRanking.setRanking(rank);
					rankingRepository.save(engineerRanking);
				}

			}
		}

	}

	// ------------Method to Get PO Details By PO Id-------------//

	public PurchaseOrderResponse getPOById(Integer id) {

		PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id)
				.orElseThrow(() -> new PurchaseOrderException(AppConstant.ErrorTypes.PO_NOT_FOUND,
						AppConstant.ErrorCodes.PO_NOT_FOUND, AppConstant.ErrorMessages.PO_NOT_FOUND));

		return PurchaseOrderResponse.builder().id(purchaseOrder.getId()).poUID(purchaseOrder.getPoUID())
				.customerPoNo(purchaseOrder.getCustomerPoNo())
				.vesselName(purchaseOrder.getServiceRequest().getVessel().getShipname())
				.equipmentCategory(purchaseOrder.getServiceRequest().getEquipmentCategory().getName())
				.equipmentName(purchaseOrder.getServiceRequest().getEquipmentName())
				.serviceRequestOn(purchaseOrder.getServiceRequest().getServiceRequestOn())
				.contactPerson(purchaseOrder.getContactPersonName()).currency(purchaseOrder.getCurrency())
				.customer(userDetailsService.convertToModel(purchaseOrder.getServiceRequest().getRequester()))
				.vendor(userDetailsService.convertToModel(purchaseOrder.getBid().getUserId()))
				.bids(convertToBidsModel(purchaseOrder.getBid()))
				.requestId(purchaseOrder.getServiceRequest().getId()).build();
	}

	// ---------Method to convert Bids to BidsModel----------//

	public BidsModel convertToBidsModel(Bids bids) {

		return BidsModel.builder().id(bids.getId()).bidUid(bids.getBidUID()).bidderId(bids.getUserId().getId())
				.serviceRequestId(bids.getServiceRequest().getId())
				.workingVendorModel(convertToWorkingVendorModel(bids.getWorkingVendor()))
				.travelCostVendorModel(convertToTravelCostVendorModel(bids.getTravelCostVendor()))
				.miscellaneousVendorModel(convertToMiscellaneousVendorModel(bids.getMiscellaneousVendor()))
				.totalProposedAmount(bids.getTotalProposedAmount()).totalAmountGet(bids.getTotalAmountGet())
				.expectedHoursNeeded(bids.getExpectedHrsNeeded()).noOfEngineer(bids.getNoOfEngineer())
				.warranty(bids.getWarranty()).totalTax(bids.getTotalTax()).totalAddCost(bids.getTotalAddCost())
				.totalAddCostGet(bids.getTotalAddCostGet()).build();
	}

	// --------Method to convert WorkingVendor to WorkingVendorModel----------//

	public List<WorkingVendorModel> convertToWorkingVendorModel(List<WorkingVendor> workingVendors) {

		return workingVendors.stream().map(w -> workingService.convertToModels(w)).collect(Collectors.toList());
	}

	// --------Method to convert TravelCOstVendor to
	// TravelCostVendorModel----------//

	public List<TravelCostVendorModel> convertToTravelCostVendorModel(List<TravelCostVendor> travelCostVendors) {

		return travelCostVendors.stream().map(t -> travelCostVendorService.convertToModel(t))
				.collect(Collectors.toList());
	}

	// --------Method to convert MiscellaneousVendor to
	// MiscellaneousVendorModel----------//

	public List<MiscellaneousVendorModel> convertToMiscellaneousVendorModel(
			List<MiscellaneousVendor> miscellaneousVendors) {

		return miscellaneousVendors.stream().map(m -> miscellaneousVendorService.convertToModel(m))
				.collect(Collectors.toList());
	}

	// ----------Method to GET All Jobs---------------//

	public List<RequestBidsResponseModel> getAllJobs(Integer bidderId, String serviceRequestStatus,
													 Integer page) {

		if(page==null){
			throw new PaginationException(AppConstant.ErrorTypes.PAGE_NUMBER_NULL,
					AppConstant.ErrorCodes.PAGE_NUMBER_NULL,AppConstant.ErrorMessages.PAGE_NUMBER_NULL);
		}

		User user = userDetailsService.findByIds(bidderId);

		if (!checkStatusExists(serviceRequestStatus)) {
			throw new ServiceRequestStatusException(AppConstant.ErrorTypes.STATUS_NOT_FOUND,
					AppConstant.ErrorCodes.STATUS_NOT_FOUND, AppConstant.ErrorMessages.STATUS_NOT_FOUND);
		}

		if (serviceRequestStatus.equals(ServiceRequestStatus.WORK_IN_PROGRESS.name())) {
			updateStatusToWorkInProgress(user);
		}

		if (serviceRequestStatus.equals(ServiceRequestStatus.COMPLETED.name())) {
			updateStatusToCompleted(user);
		}

		List<Bids> bids = bidsRepository.findByUserIdAndBidsStatusAndStatus(user, serviceRequestStatus, true,
				PageRequest.of(page,size));

		if(bids.size()<1){
			throw new BidsNotFoundException(AppConstant.ErrorTypes.BIDS_EXIST_ERROR,
					AppConstant.ErrorCodes.BIDS_ERROR_CODE, AppConstant.ErrorMessages.BIDS_NOT_EXIST);
		}

		return bids.stream().map(bid -> {

			PurchaseOrder purchaseOrder = purchaseOrderRepository.findByBid(bid);

			long remainingTime = Math
					.abs(purchaseOrder.getServiceRequest().getServiceRequestOn().getTime() - new Date().getTime());

			ServiceRequestInvoice serviceRequestInvoice = serviceRequestInvoiceRepository.findByBid(bid);

			RequestBidsResponseModel requestBidsResponseModel = RequestBidsResponseModel.builder()
					.requestUID(bid.getServiceRequest().getRequestUID())
					.serviceRequestId(bid.getServiceRequest().getId()).bidId(bid.getId()).poId(purchaseOrder.getId())
					.poUID(purchaseOrder.getPoUID()).vessel(bid.getServiceRequest().getVessel().getShipname())
					.category(bid.getServiceRequest().getEquipmentCategory().getName())
					.country(bid.getServiceRequest().getCountry().getName())
					.port(bid.getServiceRequest().getPort().getName()).etd(bid.getServiceRequest().getETD())
					.eta(bid.getServiceRequest().getETA())
					.serviceRequestOn(bid.getServiceRequest().getServiceRequestOn()).remainingTime(remainingTime)
					.build();

			if (serviceRequestInvoice != null) {
				requestBidsResponseModel.setInvoiceId(serviceRequestInvoice.getId());
			}

			return requestBidsResponseModel;

		}).collect(Collectors.toList());
	}

	// ----------Withdraw PO----------//

	public String withdrawPO(Integer poId) {

		PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(poId)
				.orElseThrow(() -> new PurchaseOrderException(AppConstant.ErrorTypes.PO_NOT_FOUND,
						AppConstant.ErrorCodes.PO_NOT_FOUND, AppConstant.ErrorMessages.PO_NOT_FOUND));

		Bids bids = purchaseOrder.getBid();

		bids.setBidsStatus(ServiceRequestStatus.WITHDRAWN.name());
		bids.setStatus(false);
		bidsRepository.save(bids);

		purchaseOrder.setPoStatus(ServiceRequestStatus.WITHDRAWN.name());
		purchaseOrder.setStatus(false);

		purchaseOrderRepository.save(purchaseOrder);

		return "PO Withdrawn Successfully!";
	}

	// Engineer Allocation

	public Object getAllBidRequestEngineerDetails(Integer bidderId) {
		List<EngineerAllocationResponseModel> allocationResponseModels = new ArrayList<EngineerAllocationResponseModel>();
		List<Bids> bids = bidsRepository.findByBidderId(bidderId, "PO_ACCEPTED", true, Pageable.unpaged());
		if (bids.size()==0) {
			throw new BidsNotFoundException(AppConstant.ErrorCodes.BIDS_ERROR_CODE,
					AppConstant.ErrorMessages.BIDS_NOT_EXIST, AppConstant.ErrorTypes.BIDS_EXIST_ERROR);
		}

		for (Bids bid : bids) {

			EngineerAllocationResponseModel responseModel = new EngineerAllocationResponseModel();
			if (purchaseOrderRepository.findDuplicateDateByBidId(bid.getId())>1)
				return "Duplicate Data Present in PO";

			PurchaseOrder order = purchaseOrderRepository.findByBid(bid);
			if (order == null) {
				throw new PurchaseOrderException(AppConstant.ErrorCodes.PO_NOT_FOUND,
						AppConstant.ErrorMessages.PO_NOT_FOUND, AppConstant.ErrorTypes.PO_NOT_FOUND);

			}
			BidsModel model=getBidById(bid.getId());
			model.getProposedEngineers().forEach(engineer->{
				BidEngineerRelation engineerRelation=engineerBidRelationRepository.getEngineersByBidIdEngineerIDServiceId(model.getId(),engineer.getEngineerId()
				,model.getServiceRequestId());
				if(engineerRelation.getEngineerStatus()!=null && engineerRelation.getEngineerStatus().equalsIgnoreCase("ASSIGNED"))
				{
					engineer.setStatus(engineerRelation.getEngineerStatus());
				}

			});
			responseModel.setBidmodel(model);
			responseModel.setEngineerResponseModel(getEngineer(bid.getCompanyId(), bid.getServiceRequest().getId()));
			responseModel.setServiceRequestResponseModel(
					serviceRequestService.getServiceRequestById(bid.getServiceRequest().getId()));
			responseModel.setPoNumber(order.getPoUID());
			allocationResponseModels.add(responseModel);

		}

		return allocationResponseModels;
	}

// Allocate engineers by bidder

	public String allocateEngineer(EngineerAllocationModel engineerAllocationModel) throws Exception {

		List<BidEngineerRelation> bidEngineerRelations = engineerBidRelationRepository
				.findByBidId(engineerAllocationModel.getBidId());
		Boolean flag = false;
		BidsModel bidsModel = getBidById(engineerAllocationModel.getBidId());
		BidsModel previousBid = bidsModel;
		for (ProposedEngineers engineers : engineerAllocationModel.getProposedEngineers()) {
			for (BidEngineerRelation relation : bidEngineerRelations) {
				if (engineers.getEngineerId().equals(relation.getEngineerId()))
					flag = true;
			}
		}
		List<ProposedEngineers> proposedEngineers = new ArrayList<ProposedEngineers>();
		if (flag == true) {
			bidsModel.setProposedEngineers(null);

			for (ProposedEngineers engineer : engineerAllocationModel.getProposedEngineers()) {
				if (engineer.getStatus() != null) {
					engineer.setStatus("ASSIGNED");
					proposedEngineers.add(engineer);
				}
			}

		} else {
			// updating the list if engineer change
			bidsModel.setProposedEngineers(null);
			for (ProposedEngineers engineer : engineerAllocationModel.getProposedEngineers()) {
				if (engineer.getStatus() != null) {
					engineer.setStatus(ServiceRequestStatus.ENGINEER_APPROVAL_PENDING.name());
					proposedEngineers.add(engineer);
				}
			}

			Bids bid = findByIds(bidsModel.getId());
			PurchaseOrder purchaseOrder = purchaseOrderRepository.findByBid(bid);
			purchaseOrder.setPoStatus(ServiceRequestStatus.ENGINEER_APPROVAL_PENDING.name());
			purchaseOrderRepository.save(purchaseOrder);
		}
		bidsModel.setProposedEngineers(proposedEngineers);
		engineerBidRelationRepository.deleteByBidId(bidsModel.getId());
		addEngineerIdInBidEngineerRelation(bidsModel, bidsModel.getId());
		minusRankingOfPreviousEngineers(previousBid);
		AssignRankingToExistingEngineers(bidsModel);

		return "Allocation successfull";
	}

	// -----------Method to Raise And Receive Additional Cost------------//

	public String updateBidsAdditionalCost(BidsModel bidsModel, String addCostStatus) {

		Bids bids = findByIds(bidsModel.getId());

		List<WorkingVendorModel> workingVendorModels = bidsModel.getWorkingVendorModel();

		List<TravelCostVendorModel> travelCostVendorModels = bidsModel.getTravelCostVendorModel();

		List<MiscellaneousVendorModel> miscellaneousVendorModels = bidsModel.getMiscellaneousVendorModel();

		List<WorkingVendor> workingVendorList = workingVendorModels.stream().map(w -> {

			if (addCostStatus.equals(ServiceRequestStatus.RECEIVED.name())) {
				if (w.getExtraExpenseApprovalStatus().equals(ServiceRequestStatus.APPROVED.name())) {
					if(w.getApprovedAmount()!=null) {
						w.setApprovedAmount(w.getApprovedAmount() + w.getExtraExpenses());
					}
					else{
						w.setApprovedAmount(w.getExtraExpenses());
					}
				}
			}

			w.setBidId(bids.getId());
			return workingService.convertToEntities(w);
		}).collect(Collectors.toList());

		List<TravelCostVendor> travelCostVendorList = travelCostVendorModels.stream().map(t -> {

			if (addCostStatus.equals(ServiceRequestStatus.RECEIVED.name())) {
				if (t.getExtraExpenseApprovalStatus().equals(ServiceRequestStatus.APPROVED.name())) {
					if(t.getApprovedAmount()!=null) {
						t.setApprovedAmount(t.getApprovedAmount() + t.getExtraExpenses());
					}
					else{
						t.setApprovedAmount(t.getExtraExpenses());
					}
				}
			}

			t.setBidId(bids.getId());
			return travelCostVendorService.convertToEntity(t);
		}).collect(Collectors.toList());

		List<MiscellaneousVendor> miscellaneousVendorList = miscellaneousVendorModels.stream().map(m -> {

			if (addCostStatus.equals(ServiceRequestStatus.RECEIVED.name())) {
				if (m.getExtraExpenseApprovalStatus().equals(ServiceRequestStatus.APPROVED.name())) {
					if(m.getApprovedAmount()!=null) {
						m.setApprovedAmount(m.getApprovedAmount() + m.getExtraExpenses());
					}
					else{
						m.setApprovedAmount(m.getExtraExpenses());
					}
				}
			}

			m.setBidId(bids.getId());
			return miscellaneousVendorService.convertToEntity(m);
		}).collect(Collectors.toList());

		workingVendorRepository.saveAll(workingVendorList);

		travelCostVendorRepository.saveAll(travelCostVendorList);

		miscellaneousVendorRepository.saveAll(miscellaneousVendorList);

		bids.setTotalAddCost(bidsModel.getTotalAddCost());
		bids.setTotalAddCostGet(bidsModel.getTotalAddCostGet());
		bids.setAdditionalCostStatus(addCostStatus);

		bidsRepository.save(bids);

		return "Additional Cost Has Been " + addCostStatus + " Successfully.";
	}

	// --------Method to get Bidder's All Additional Cost Raised Bids--------//

	public List<RaisedAddCostResponseModel> getAllAdditionalCostRaisedBids(Integer bidderId, Integer page) {

		if(page==null){
			throw new PaginationException(AppConstant.ErrorTypes.PAGE_NUMBER_NULL,
					AppConstant.ErrorCodes.PAGE_NUMBER_NULL,AppConstant.ErrorMessages.PAGE_NUMBER_NULL);
		}

		User user = userDetailsService.findByIds(bidderId);

		List<Bids> bids = bidsRepository.findAdditionalCostRaisedBids(user.getId(), PageRequest.of(page,size));

		if (bids.size() < 1) {
			throw new BidsNotFoundException(AppConstant.ErrorTypes.BIDS_EXIST_ERROR,
					AppConstant.ErrorCodes.BIDS_ERROR_CODE, AppConstant.ErrorMessages.BIDS_NOT_EXIST);
		}

		return bids.stream().map(bid -> convertToRaisedAddCostResponseModel(purchaseOrderRepository.findByBid(bid)))
				.collect(Collectors.toList());
	}

	// ---------Method to convert PurchaseOrder in RaisedAddCostResponseModel------------//

	public RaisedAddCostResponseModel convertToRaisedAddCostResponseModel(PurchaseOrder purchaseOrder) {

		if(purchaseOrder!=null) {
			return RaisedAddCostResponseModel.builder().bidId(purchaseOrder.getBid().getId())
					.bidUID(purchaseOrder.getBid().getBidUID()).serviceRequestId(purchaseOrder.getServiceRequest().getId())
					.poId(purchaseOrder.getId()).poUID(purchaseOrder.getPoUID())
					.vessel(purchaseOrder.getServiceRequest().getVessel().getShipname())
					.category(purchaseOrder.getServiceRequest().getEquipmentCategory().getName())
					.requestUID(purchaseOrder.getServiceRequest().getRequestUID())
					.serviceRequestOn(purchaseOrder.getServiceRequest().getServiceRequestOn())
					.RaisedBy(purchaseOrder.getBid().getUserId().getName())
					.status(purchaseOrder.getBid().getAdditionalCostStatus()).build();
		}
		else{
			return null;
		}
	}

	// ---------Method to Revoke Additional Cost----------//

	public String revokeAdditionalCost(Integer bidId) {

		Bids bids = findByIds(bidId);

		bids.setAdditionalCostStatus(ServiceRequestStatus.REVOKED.name());

		bidsRepository.save(bids);

		return "Addition Cost P.O Revoked Successfully";
	}

	// -----------Method to check if Status Exists----------//

	public Boolean checkStatusExists(String serviceRequestStatus) {

		for (ServiceRequestStatus s : ServiceRequestStatus.values()) {
			if (s.name().equals(serviceRequestStatus)) {
				return true;
			}
		}
		return false;
	}

	// ---------Method to Update Status to WorkInProgress---------//

	public void updateStatusToWorkInProgress(User user) {

		List<Bids> bidsList = bidsRepository.findByUserIdAndBidsStatusAndStatus(user,
				ServiceRequestStatus.PO_ACCEPTED.name(), true, Pageable.unpaged());

		bidsList.forEach(bid -> {

			if (new Date().getTime() >= bid.getServiceRequest().getServiceRequestOn().getTime()) {
				bid.setBidsStatus(ServiceRequestStatus.WORK_IN_PROGRESS.name());

				ServiceRequest serviceRequest = bid.getServiceRequest();
				serviceRequest.setRequestStatus(ServiceRequestStatus.WORK_IN_PROGRESS.name());
				serviceRequestRepository.save(serviceRequest);

				PurchaseOrder purchaseOrder = purchaseOrderRepository.findByBid(bid);
				purchaseOrder.setPoStatus(ServiceRequestStatus.WORK_IN_PROGRESS.name());
				purchaseOrderRepository.save(purchaseOrder);

				bidsRepository.save(bid);
			}
		});
	}

	// ---------Method to Update Status to Completed--------//

	public void updateStatusToCompleted(User user) {

		List<Bids> bidsList = bidsRepository.findByUserIdAndBidsStatusAndStatus(user,
				ServiceRequestStatus.WORK_IN_PROGRESS.name(), true, Pageable.unpaged());

		bidsList.forEach(bid -> {

			long requiredTime = (long) bid.getExpectedHrsNeeded() * 60 * 60 * 1000;

			if (new Date().getTime() >= (bid.getServiceRequest().getServiceRequestOn().getTime() + requiredTime)) {

				bid.setBidsStatus(ServiceRequestStatus.COMPLETED.name());

				ServiceRequest serviceRequest = bid.getServiceRequest();
				serviceRequest.setRequestStatus(ServiceRequestStatus.COMPLETED.name());
				serviceRequestRepository.save(serviceRequest);

				PurchaseOrder purchaseOrder = purchaseOrderRepository.findByBid(bid);
				purchaseOrder.setPoStatus(ServiceRequestStatus.COMPLETED.name());
				PurchaseOrder pOrder = purchaseOrderRepository.save(purchaseOrder);

				UserModel hoc = userDetailsService.getUserHeadByCompanyId(user.getCompanyId().getId());
				headOfCompanyRepository.save(HeadOfCompany.builder().headId(hoc.getId())
						.statusId(bid.getId()).status("COMPLETED_JOB").build());

				bidsRepository.save(bid);

				ServiceRequestInvoice serviceRequestInvoice = serviceRequestInvoiceRepository
						.findByServiceRequest(pOrder.getServiceRequest().getId());
				if(serviceRequestInvoice!=null){
					throw new InvoiceException(AppConstant.ErrorTypes.INVOICE_ALREADY_PRESENT,
							AppConstant.ErrorCodes.INVOICE_ALREADY_PRESENT, AppConstant.ErrorMessages.INVOICE_ALREADY_PRESENT);
				}

				serviceRequestInvoiceRepository.save(
						ServiceRequestInvoice.builder().serviceRequest(pOrder.getServiceRequest()).bid(pOrder.getBid())
								.currency(pOrder.getCurrency()).totalCost(pOrder.getBid().getTotalProposedAmount())
								.invoiceUID(paymentTransactionService.InvoiceUniqueIdGenerated())
								.invoiceStatus(ServiceRequestStatus.PENDING.name())
								.customer_id(pOrder.getServiceRequest().getRequester().getUid())
								.purchaseOrder(pOrder).build());
			}
		});
	}

	// ----------Method to Get Invoice Details By Invoice Id---------//

	public InvoiceResponseModel getInvoiceDetails(Integer invoiceId) {

		Optional<ServiceRequestInvoice> serviceRequestInvoice = serviceRequestInvoiceRepository.findById(invoiceId);

		if (!serviceRequestInvoice.isPresent()) {
			throw new PurchaseOrderException(AppConstant.ErrorTypes.INVOICE_NOT_FOUND,
					AppConstant.ErrorCodes.INVOICE_NOT_FOUND, AppConstant.ErrorMessages.INVOICE_NOT_FOUND);
		}

		ServiceRequestInvoice invoice = serviceRequestInvoice.get();

		BidsModel bidsModel = convertToBidsModel(invoice.getBid());

		return InvoiceResponseModel.builder().bidsModel(bidsModel).invoiceNumber(invoice.getInvoiceUID())
				.currency(invoice.getCurrency())
				.customerName(invoice.getBid().getServiceRequest().getRequester().getName())
				.customerAddress(invoice.getBid().getServiceRequest().getRequester().getAddress())
				.voucherApplied(invoice.getVoucherNumber()).voucherDiscount(invoice.getVoucherDiscount())
				.serviceRequestId(invoice.getServiceRequest().getId())
				.poUid(invoice.getPurchaseOrder().getPoUID())
				.Total(invoice.getTotalCost())
				.invoiceStatus(invoice.getInvoiceStatus())
				.build();
	}

	// ----------Method for Raise Invoice-----------//

	public String raiseInvoice(Integer invoiceId) {

		Optional<ServiceRequestInvoice> serviceRequestInvoice = serviceRequestInvoiceRepository.findById(invoiceId);

		if (!serviceRequestInvoice.isPresent()) {
			throw new PurchaseOrderException(AppConstant.ErrorTypes.INVOICE_NOT_FOUND,
					AppConstant.ErrorCodes.INVOICE_NOT_FOUND, AppConstant.ErrorMessages.INVOICE_NOT_FOUND);
		}

		ServiceRequestInvoice invoice = serviceRequestInvoice.get();

		invoice.setInvoiceStatus(ServiceRequestStatus.INVOICE_RAISED.name());

		Bids bid = invoice.getBid();
		bid.setBidsStatus(ServiceRequestStatus.INVOICE_RAISED.name());
		bidsRepository.save(bid);

		ServiceRequest serviceRequest = invoice.getServiceRequest();
		serviceRequest.setRequestStatus(ServiceRequestStatus.INVOICE_RAISED.name());
		serviceRequestRepository.save(serviceRequest);

		PurchaseOrder purchaseOrder = invoice.getPurchaseOrder();
		purchaseOrder.setPoStatus(ServiceRequestStatus.INVOICE_RAISED.name());
		purchaseOrderRepository.save(purchaseOrder);

		User user = userDetailsService.findByIds(bid.getUserId().getId());

		UserModel hoc = userDetailsService.getUserHeadByCompanyId(user.getCompanyId().getId());
		headOfCompanyRepository.save(HeadOfCompany.builder().headId(hoc.getId())
				.statusId(invoiceId).status("INVOICE_RAISED").build());

		serviceRequestInvoiceRepository.save(invoice);

		return "Invoice Raise Successfully!";
	}

	public String generatePdfForInovoice(InvoiceResponseModel model, HttpServletResponse response) {

		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=ServiceInvoice.pdf");

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
			Font separate = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD);

			BaseColor contentColor = WebColors.getRGBColor("#4f2729");
			font.setColor(contentColor);
			BaseColor invoiceColor = WebColors.getRGBColor("#575551");
			invoiceFont.setColor(invoiceColor);
			TotalAmontFont.setColor(contentColor);
			BaseColor NoteColor = WebColors.getRGBColor("#ba2e27");
			note.setColor(NoteColor);
			BaseColor HeadingColor = WebColors.getRGBColor("#050400");
			Heading.setColor(HeadingColor);
			BaseColor separator = WebColors.getRGBColor("#c7bda1");
			separate.setColor(separator);

			String path = "https://transas.s3.ap-south-1.amazonaws.com/TransasImages/invoice_main_page_ath_marine_page_1.jpg";
			Image image = Image.getInstance(path);
			image.scaleAbsolute(PageSize.A4);
			image.setAbsolutePosition(0, 0);
			document.add(image);
			// Adding Invoice Heading

			// Adding Invoice Number
			Paragraph invoiceNumber = new Paragraph("Invoice Number: " + model.getInvoiceNumber(), font);
			invoiceNumber.setSpacingBefore(100f);
			invoiceNumber.setAlignment(Element.ALIGN_RIGHT);
			invoiceNumber.setIndentationRight(25);
			document.add(invoiceNumber);

			// Adding Date
			LocalDate localDate = LocalDate.now();
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
			String date1 = localDate.format(dateTimeFormatter);
			Paragraph date = new Paragraph("Date: " + date1, font);
			date.setAlignment(Element.ALIGN_RIGHT);
			date.setIndentationRight(25);
			document.add(date);

			// Adding Currency
			Paragraph invoiceCurrency = new Paragraph("Invoice Currency: " + model.getCurrency(), font);
			invoiceCurrency.setAlignment(Element.ALIGN_RIGHT);
			invoiceCurrency.setIndentationRight(25);
			document.add(invoiceCurrency);

			// Adding Customer Details
			Paragraph CustomerDetails = new Paragraph("Customer Details", Heading);
			CustomerDetails.setAlignment(Element.ALIGN_LEFT);
			CustomerDetails.setIndentationLeft(43);
			CustomerDetails.setSpacingBefore(5f);
			CustomerDetails.setSpacingAfter(10f);
			document.add(CustomerDetails);

			// Adding Name
			Paragraph Name = new Paragraph("Name: " + model.getCustomerName(), font);
			Name.setAlignment(Element.ALIGN_LEFT);
			Name.setIndentationLeft(43);
			document.add(Name);

			// Adding Address
			Paragraph Address = new Paragraph("Address: " + model.getCustomerAddress(), font);
			Address.setAlignment(Element.ALIGN_LEFT);
			Address.setIndentationLeft(43);
			document.add(Address);

			Paragraph dash = new Paragraph("- - - - - - - - - - - - - - - - - - - - - - -"
					+ " - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ", separate);
			dash.setIndentationLeft(43);
			dash.setIndentationRight(25);
			document.add(dash);

			// Adding Description
			Paragraph Description = new Paragraph("Description", Heading);
			Description.setAlignment(Element.ALIGN_LEFT);
			Description.setIndentationLeft(43);
			Description.setSpacingAfter(10f);
			document.add(Description);

			// Adding Line Item and proposed amount
			Chunk glue = new Chunk(new VerticalPositionMark());

			Paragraph lineItem1 = new Paragraph("Line Items", TotalAmontFont);
			lineItem1.add(new Chunk(glue));
			lineItem1.add("Amount You Get");
			lineItem1.setIndentationLeft(43);
			lineItem1.setIndentationRight(25);
			document.add(lineItem1);

			// Adding Working
			Paragraph workingA = new Paragraph("Working(A)" + "", TotalAmontFont);
			workingA.setAlignment(Element.ALIGN_LEFT);
			workingA.setIndentationLeft(43);
			document.add(workingA);

			Integer workingTotal = 0;
			String workingAmount = "workingTotal";

			// Adding Amount
			List<WorkingVendorModel> vendorModels = model.getBidsModel().getWorkingVendorModel();
			for (WorkingVendorModel working : vendorModels) {
				if (working.getInline().equals(workingAmount)) {
					Paragraph inlineItemsA = new Paragraph("Total (A)", TotalAmontFont);
					workingTotal = working.getAmountYouGet();
					inlineItemsA.add(new Chunk(glue));
					inlineItemsA.add(working.getAmountYouGet().toString());
					inlineItemsA.setIndentationRight(28);
					inlineItemsA.setIndentationLeft(46);
					document.add(inlineItemsA);
				} else {
					Paragraph inlineItemsA = new Paragraph(working.getInline(), font);
					inlineItemsA.add(new Chunk(glue));
					inlineItemsA.add(working.getAmountYouGet().toString());
					inlineItemsA.setIndentationRight(28);
					inlineItemsA.setIndentationLeft(46);
					document.add(inlineItemsA);
				}
			}

			// Adding line
			Paragraph line = new Paragraph(" _____________________________________________________________", separate);
			line.setIndentationLeft(43);
			line.setSpacingAfter(5f);
			line.setIndentationRight(25);
			document.add(line);

			// Adding Travel
			Paragraph travelB = new Paragraph("Travel(B)" + "", TotalAmontFont);
			travelB.setAlignment(Element.ALIGN_LEFT);
			travelB.setIndentationLeft(43);
			document.add(travelB);

			Integer travelTotal = 0;
			String travelAmount = "travelTotal";
			int count = 0;
			List<TravelCostVendorModel> travelModel = model.getBidsModel().getTravelCostVendorModel();
			for (TravelCostVendorModel travel : travelModel) {
				if (count >= 6) {
					document.newPage();
					document.add(image);
					Paragraph lineItem2 = new Paragraph("Line Items", TotalAmontFont);
					lineItem2.add(new Chunk(glue));
					lineItem2.add("Amount You Get");
					lineItem2.setIndentationLeft(43);
					lineItem2.setSpacingBefore(80f);
					lineItem2.setIndentationRight(25);
					document.add(lineItem2);
					count = -2;
				}
				if (travel.getInline().equals(travelAmount)) {
					Paragraph inlineItemsB = new Paragraph("Total (B)", TotalAmontFont);
					travelTotal = travel.getAmountYouGet();
					inlineItemsB.add(new Chunk(glue));
					inlineItemsB.add(travel.getAmountYouGet().toString());
					inlineItemsB.setIndentationRight(28);
					inlineItemsB.setIndentationLeft(46);
					document.add(inlineItemsB);
				} else {
					Paragraph inlineItemsB = new Paragraph(travel.getInline(), font);
					inlineItemsB.add(new Chunk(glue));
					inlineItemsB.add(travel.getAmountYouGet().toString());
					inlineItemsB.setIndentationRight(28);
					inlineItemsB.setIndentationLeft(46);
					document.add(inlineItemsB);
				}

				count++;
			}

			document.add(line);

			// Adding Travel
			Paragraph otherC = new Paragraph("Other(C)" + "", TotalAmontFont);
			otherC.setAlignment(Element.ALIGN_LEFT);
			otherC.setIndentationLeft(43);
			document.add(otherC);
			// Adding amounts
			Integer otherTotal = 0;
			String otherAmount = "othersTotal";
			List<MiscellaneousVendorModel> miscellaneousVendorModels = model.getBidsModel()
					.getMiscellaneousVendorModel();
			for (MiscellaneousVendorModel other : miscellaneousVendorModels) {
				if (other.getInline().equals(otherAmount)) {
					Paragraph inlineItemsC = new Paragraph("Total (C)", TotalAmontFont);
					otherTotal = other.getAmountYouGet();
					inlineItemsC.add(new Chunk(glue));
					inlineItemsC.add(other.getAmountYouGet().toString());
					inlineItemsC.setIndentationRight(28);
					inlineItemsC.setIndentationLeft(46);
					document.add(inlineItemsC);
				} else {
					Paragraph inlineItemsC = new Paragraph(other.getInline(), font);
					inlineItemsC.add(new Chunk(glue));
					inlineItemsC.add(other.getAmountYouGet().toString());
					inlineItemsC.setIndentationRight(28);
					inlineItemsC.setIndentationLeft(46);
					document.add(inlineItemsC);
				}

			}

			// Adding additional value
			Paragraph additionalCostD = new Paragraph("Additional Cost (D)", TotalAmontFont);
			additionalCostD.add(new Chunk(glue));
			additionalCostD.add(model.getBidsModel().getTotalAddCostGet().toString());
			additionalCostD.setIndentationRight(28);
			additionalCostD.setIndentationLeft(46);
			document.add(additionalCostD);

			document.add(line);

			// Adding voucher info
			Paragraph voucherApplied = new Paragraph("Voucher Applied (E)", TotalAmontFont);
			voucherApplied.add(new Chunk(glue));
			voucherApplied.add(model.getVoucherApplied());
			voucherApplied.setIndentationRight(28);
			voucherApplied.setIndentationLeft(46);
			document.add(voucherApplied);

			Integer grandTotalAmount = workingTotal + travelTotal + otherTotal+model.getBidsModel().getTotalAddCostGet();
			// Adding grand total of A+B+C+D
			Paragraph grandTotal = new Paragraph("Total (A+B+C+D)", TotalAmontFont);
			grandTotal.add(new Chunk(glue));
			grandTotal.add(grandTotalAmount.toString());
			grandTotal.setIndentationRight(28);
			grandTotal.setIndentationLeft(46);
			document.add(grandTotal);

			document.add(dash);

			// Adding Payment Favour
			Paragraph PaymentFavour = new Paragraph("Payment in Favour of Ath Marine Pte. Ltd.", Heading);
			PaymentFavour.setAlignment(Element.ALIGN_LEFT);
			PaymentFavour.setIndentationLeft(43);
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
			Footer1.setSpacingBefore(40f);
			document.add(Footer1);

			Paragraph Footer2 = new Paragraph("(This is a computer generated invoice, no signature required)", note);
			Footer2.setAlignment(Element.ALIGN_CENTER);
			document.add(Footer2);

			document.close();
			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Pdf Generated";
	}


	//-----------Method to get All Withdrawn POs---------//

	public List<RequestBidsResponseModel> getAllWithdrawnPOs(Integer bidderId, Integer page){

		if(page==null){
			throw new PaginationException(AppConstant.ErrorTypes.PAGE_NUMBER_NULL,
					AppConstant.ErrorCodes.PAGE_NUMBER_NULL,AppConstant.ErrorMessages.PAGE_NUMBER_NULL);
		}

		User user = userDetailsService.findByIds(bidderId);

		List<Bids> bids = bidsRepository.findByUserId(user);

		List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findByBidsAndPOStatus(bids,ServiceRequestStatus.WITHDRAWN.name()
		,PageRequest.of(page,size));

		return purchaseOrders.stream().map(purchaseOrder -> RequestBidsResponseModel.builder()
				.requestUID(purchaseOrder.getBid().getServiceRequest().getRequestUID())
				.poId(purchaseOrder.getId()).poUID(purchaseOrder.getPoUID())
				.vessel(purchaseOrder.getBid().getServiceRequest().getVessel().getShipname())
				.category(purchaseOrder.getBid().getServiceRequest().getEquipmentCategory().getName())
				.country(purchaseOrder.getBid().getServiceRequest().getCountry().getName())
				.port(purchaseOrder.getBid().getServiceRequest().getPort().getName())
				.etd(purchaseOrder.getBid().getServiceRequest().getETD())
				.eta(purchaseOrder.getBid().getServiceRequest().getETA())
				.serviceRequestOn(purchaseOrder.getBid().getServiceRequest().getServiceRequestOn()).build()).collect(Collectors.toList());
	}

	public List<RequestBidsResponseModel> getAllLiveStatus(Integer bidderId, Integer page){

		userDetailsService.findByIds(bidderId);

		if(page==null){
			throw new PaginationException(AppConstant.ErrorTypes.PAGE_NUMBER_NULL,
					AppConstant.ErrorCodes.PAGE_NUMBER_NULL,AppConstant.ErrorMessages.PAGE_NUMBER_NULL);
		}

		List<String> status = Arrays.asList("BIDS_IN_PROGRESS","BID_RAISED","APPROVER_PENDING","DECLINED",
				"WORK_IN_PROGRESS","COMPLETED","INVOICE_RAISED","PO_RAISED");

		List<Bids> allBids = bidsRepository.findByBidderId(bidderId,status);

		List<BidderRequestRelation> bidderRequestRelations = bidderRequestRelationRepository
				.findByBidderIdAndRequestStatus(bidderId,ServiceRequestStatus.INTERESTED.name(),
						Pageable.unpaged());

		List<RequestBidsResponseModel> requestBidsResponseModels = new ArrayList<>();

		allBids.forEach(bid -> {

			ServiceRequest serviceRequest = bid.getServiceRequest();

			requestBidsResponseModels.add(RequestBidsResponseModel.builder()
				.serviceRequestId(serviceRequest.getId()).requestUID(serviceRequest.getRequestUID())
				.bidId(bid.getId()).bidUID(bid.getBidUID()).vessel(serviceRequest.getVessel().getShipname())
				.category(serviceRequest.getEquipmentCategory().getName())
				.country(serviceRequest.getCountry().getName())
				.port(serviceRequest.getPort().getName()).status(bid.getBidsStatus())
				.eta(serviceRequest.getETA()).etd(serviceRequest.getETD())
				.serviceRequestOn(serviceRequest.getServiceRequestOn()).build());
		});

		bidderRequestRelations.forEach(bidderRequestRelation -> {

			ServiceRequest serviceRequest = serviceRequestService.findByIds(bidderRequestRelation.getRequestId());

			requestBidsResponseModels.add(RequestBidsResponseModel.builder()
					.serviceRequestId(serviceRequest.getId()).requestUID(serviceRequest.getRequestUID())
					.vessel(serviceRequest.getVessel().getShipname())
					.category(serviceRequest.getEquipmentCategory().getName())
					.country(serviceRequest.getCountry().getName())
					.port(serviceRequest.getPort().getName()).status(ServiceRequestStatus.INTERESTED.name())
					.eta(serviceRequest.getETA()).etd(serviceRequest.getETD())
					.serviceRequestOn(serviceRequest.getServiceRequestOn()).build());
		});

		if(requestBidsResponseModels.size()<1){
			throw new BidsNotFoundException(AppConstant.ErrorTypes.BIDS_EXIST_ERROR,
					AppConstant.ErrorCodes.BIDS_ERROR_CODE,AppConstant.ErrorMessages.BIDS_NOT_EXIST);
		}

		requestBidsResponseModels.forEach(model -> {
			String bidStatus = model.getStatus();
			bidStatus = bidStatus.replaceAll("_"," ");
			String WORD_SEPARATOR = " ";
			bidStatus = Arrays.stream(bidStatus.split(WORD_SEPARATOR))
					.map(word -> word.isEmpty()
							? word
							: Character.toTitleCase(word.charAt(0)) + word.substring(1).toLowerCase()
					)
					.collect(Collectors.joining());

			if(bidStatus.equals("Completed")) {
				bidStatus = "CompletedJobs";
			}
			if(bidStatus.equals("Interested")) {
				bidStatus = "InterestedRequest";
			}
			if(bidStatus.equals("BidRaised")) {
				bidStatus = "RaisedBids";
			}
			if(bidStatus.equalsIgnoreCase("PoRaised")) {
				bidStatus = "PurchaseOrderReceived";
			}

			model.setStatus(bidStatus);
		});

		if(((page*size)+size)<requestBidsResponseModels.size()) {
			return requestBidsResponseModels.subList(page * size, (page * size) + size);
		}
		else if((page*size)<requestBidsResponseModels.size()){
			return requestBidsResponseModels.subList(page * size, requestBidsResponseModels.size());
		}
		else if((page*size)==requestBidsResponseModels.size()){
			throw new BidsNotFoundException(AppConstant.ErrorTypes.BIDS_EXIST_ERROR,
					AppConstant.ErrorCodes.BIDS_ERROR_CODE,AppConstant.ErrorMessages.BIDS_NOT_EXIST);
		}
		else{
			return requestBidsResponseModels;
		}
	}


	//-----------Method to get History Counts--------//

	public HistoryCounts getHistoryCounts(Integer id){

		User user = userDetailsService.findByIds(id);

		serviceRequestService.updateAllRequestsStatus(user);

		int timedOutCount = serviceRequestRepository.countByRequestStatus(ServiceRequestStatus.TIMED_OUT.name());

		int notShownInterest = bidderRequestRelationRepository
				.countByBidderIdAndRequestStatus(id,ServiceRequestStatus.NOT_INTERESTED.name());

		int bidsWithdrawn = bidsRepository.countByBidderIdAndStatus(id,ServiceRequestStatus.WITHDRAWN.name());

		List<Bids> bids = bidsRepository.findByUserId(user);

		int posWithdrawn = purchaseOrderRepository.countByBidsAndPOStatus(bids,ServiceRequestStatus.WITHDRAWN.name());

		return HistoryCounts.builder().timedOut(timedOutCount).notShownAnyInterest(notShownInterest)
				.bidsWithdrawn(bidsWithdrawn).poWithdraws(posWithdrawn).build();
	}

	public Object getAllEngineerJobDetails(Integer bidderId,Integer month) {
		if (month<=0 || month>12)
			return "Please enter correct month";
		User user=userDetailsService.findByIds(bidderId);
		List<UserModel> engineersList= userDetailsService.getAllEngineersByCompanyId(user.getCompanyId().getId());
		List<EngineerCalendarResponse> jobDetails=new ArrayList<EngineerCalendarResponse>();
		engineersList.forEach(engineer->{
			jobDetails.addAll(engineerService.convertToCalendarResponseModel(engineer.getId(),month));
		});
		if (jobDetails.size()==0)
		{
			return "Sorry, This month no jobs available";
		}
		return jobDetails;
	}

	public Object getEngineerJobDetail(Integer engineerId, String serviceDate) {
		engineerService.checkEngineerIdExist(engineerId);
		List<BidEngineerRelation> jobs=engineerBidRelationRepository.getJObDetailByEngineerIdAndServiceDate(engineerId,serviceDate);
		List<EngineerCalendarResponse> jobDetails=new ArrayList<EngineerCalendarResponse>();
		User user=userDetailsService.findByIds(engineerId);

		jobs.forEach(job->{
			ServiceRequestResponseModel serviceRequest=serviceRequestService.getServiceRequestById(job.getServiceRequest().getId());
			PurchaseOrder purchaseOrder=purchaseOrderRepository.findByRequestId(job.getServiceRequest().getId());
			BidsModel bidsModel=getBidById(purchaseOrder.getBid().getId());
			jobDetails.add(EngineerCalendarResponse.builder()
							.engineerName(user.getName())
							.serviceOn(job.getServiceOn())
							.poNumber(purchaseOrder.getCustomerPoNo())
							.vesselName(serviceRequest.getVessel().getVesselName())
							.port(serviceRequest.getPort().getName())
							.CustomerCompanyName("")
							.equipmentCategory(serviceRequest.getCategory().getName())
							.equipmentName(serviceRequest.getEquipmentName())
							.poValue(bidsModel.getTotalProposedAmount().toString())
					.build());
		});
		return jobDetails;
	}

	//-----------------Method To generate Pdf for PO
	public String downloadPOByPOID(Integer poId, HttpServletResponse response) {
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=PurchaseOrder.pdf");

		com.itextpdf.text.Document document = new com.itextpdf.text.Document();

		PurchaseOrderResponse purchaseOrderResponse=getPOById(poId);

		ServiceRequestInvoice invoice=serviceRequestInvoiceRepository.findByServiceRequest(purchaseOrderResponse.getRequestId());
		InvoiceResponseModel model=getInvoiceDetails(invoice.getId());
		User userModel=userDetailsService.findByIds(invoice.getServiceRequest().getRequester().getId());
		User vendorUser=userDetailsService.findByIds(invoice.getBid().getCompanyId());

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
			Font separate = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD);
			Font purchaseOrderFont = new Font(Font.FontFamily.HELVETICA, 28);
			Font underline=new Font(Font.FontFamily.HELVETICA, 13, Font.UNDERLINE);

			BaseColor contentColor = WebColors.getRGBColor("#4f2729");
			font.setColor(contentColor);
			BaseColor invoiceColor = WebColors.getRGBColor("#575551");
			invoiceFont.setColor(invoiceColor);
			TotalAmontFont.setColor(contentColor);
			BaseColor NoteColor = WebColors.getRGBColor("#ba2e27");
			note.setColor(NoteColor);
			BaseColor HeadingColor = WebColors.getRGBColor("#050400");
			Heading.setColor(HeadingColor);
			BaseColor separator = WebColors.getRGBColor("#c7bda1");
			separate.setColor(separator);
			BaseColor poColor = WebColors.getRGBColor("#050400");
			purchaseOrderFont.setColor(poColor);
			BaseColor underlineColor = WebColors.getRGBColor("#050400");
			underline.setColor(underlineColor);


			String path = "https://transas.s3.ap-south-1.amazonaws.com/TransasImages/final_invoice_continue_page.jpg";
			Image image = Image.getInstance(path);
			image.scaleAbsolute(PageSize.A4);
			image.setAbsolutePosition(0, 0);
			document.add(image);
			// Adding PO Heading
			Paragraph purchaseOrder = new Paragraph("Purchase Order", purchaseOrderFont);
			purchaseOrder.setAlignment(Element.ALIGN_RIGHT);
			purchaseOrder.setSpacingBefore(-40);
			//purchaseOrder.setIndentationRight(5);
			document.add(purchaseOrder);
			// Adding Ath PO Number
			Paragraph AthPoNo = new Paragraph("Ath P.O No: ", TotalAmontFont);
			AthPoNo.setSpacingBefore(10f);
			AthPoNo.setFont(font);
			AthPoNo.add(purchaseOrderResponse.getPoUID());
			AthPoNo.setAlignment(Element.ALIGN_RIGHT);
			//AthPoNo.setIndentationRight(15);
			document.add(AthPoNo);

			// Adding Customer's PO Number
			Paragraph customerPoNo = new Paragraph("Customer's P.O No: ", TotalAmontFont);
			customerPoNo.setFont(font);
			customerPoNo.add("123234123");
			customerPoNo.setAlignment(Element.ALIGN_RIGHT);
			document.add(customerPoNo);

			// Adding Line Item and proposed amount
			Chunk glue = new Chunk(new VerticalPositionMark());

			//Adding  from and To
			Paragraph fromTo = new Paragraph("From", TotalAmontFont);
			fromTo.add(new Chunk(glue));
			fromTo.add("To");
			fromTo.setSpacingBefore(20);
			document.add(fromTo);

			//Adding  Customer and Vendor Details
			Paragraph CustomerVendor = new Paragraph("Customer Id: "+userModel.getUid(), font);
			CustomerVendor.add(new Chunk(glue));
			CustomerVendor.add("Partner Id: "+vendorUser.getUid());
			document.add(CustomerVendor);

			//Adding  Customer and Vendor Details
			Paragraph CustomerVendorName = new Paragraph("Name: "+userModel.getName(), font);
			CustomerVendorName.add(new Chunk(glue));
			CustomerVendorName.add("Name: "+vendorUser.getName());
			document.add(CustomerVendorName);

			//Adding  Customer and Vendor Details
			Paragraph CustomerVendorAddress = new Paragraph("Address: "+userModel.getAddress(), font);
			CustomerVendorAddress.add(new Chunk(glue));
			CustomerVendorAddress.add("Address: "+vendorUser.getAddress());
			document.add(CustomerVendorAddress);

			// Adding Vessel Name
			Paragraph VesselName = new Paragraph("Vessel Name: " + purchaseOrderResponse.getVesselName(), font);
			VesselName.setAlignment(Element.ALIGN_LEFT);
			VesselName.setSpacingBefore(20);
			document.add(VesselName);

			// Adding Equipment Name
			Paragraph EquipmentName = new Paragraph("Equipment Name: " + purchaseOrderResponse.getVesselName(), font);
			EquipmentName.setAlignment(Element.ALIGN_LEFT);
			document.add(EquipmentName);

			// Adding Equipment Category
			Paragraph EquipmentCategory = new Paragraph("Equipment Category: " + purchaseOrderResponse.getEquipmentCategory(), font);
			EquipmentCategory.setAlignment(Element.ALIGN_LEFT);
			document.add(EquipmentCategory);

			// Adding Service Request On
			Paragraph serviceRequestOn = new Paragraph("Service Request On: " + purchaseOrderResponse.getServiceRequestOn(), font);
			serviceRequestOn.setAlignment(Element.ALIGN_LEFT);
			document.add(serviceRequestOn);

			//Adding Table Data
			PdfPTable pdfPTable= new PdfPTable(4);
			pdfPTable.setWidthPercentage(100);
			pdfPTable.setSpacingBefore(20);
			pdfPTable.setTotalWidth(50f);
			PdfPCell BidId=new PdfPCell();
			BidId.setFixedHeight(30F);
			BidId.addElement(new Paragraph("     Bid I.D"));
			pdfPTable.addCell(BidId);

			PdfPCell contactPerson=new PdfPCell();
			contactPerson.setFixedHeight(30F);
			contactPerson.addElement(new Paragraph("     Contact Person"));
			pdfPTable.addCell(contactPerson);

			PdfPCell currency=new PdfPCell();
			currency.setFixedHeight(30F);
			currency.addElement(new Paragraph("     Currency"));
			pdfPTable.addCell(currency);

			PdfPCell po=new PdfPCell();
			po.setFixedHeight(30F);
			po.addElement(new Paragraph("     P.O Value"));
			pdfPTable.addCell(po);

			PdfPCell BidIdValue=new PdfPCell();
			BidIdValue.setFixedHeight(30F);
			BidIdValue.addElement(new Paragraph("     "+purchaseOrderResponse.getBids().getBidUid()));
			pdfPTable.addCell(BidIdValue);

			PdfPCell contactPersonValue=new PdfPCell();
			contactPersonValue.setFixedHeight(30F);
			contactPersonValue.addElement(new Paragraph("     "+purchaseOrderResponse.getContactPerson()));
			pdfPTable.addCell(contactPersonValue);

			PdfPCell currencyValue=new PdfPCell();
			currencyValue.setFixedHeight(30F);
			currencyValue.addElement(new Paragraph("     "+purchaseOrderResponse.getCurrency()));
			pdfPTable.addCell(currencyValue);

			PdfPCell poValue=new PdfPCell();
			poValue.setFixedHeight(30F);
			poValue.addElement(new Paragraph("     "+purchaseOrderResponse.getBids().getTotalProposedAmount()));
			pdfPTable.addCell(poValue);

			document.add(pdfPTable);


			Paragraph dash = new Paragraph("- - - - - - - - - - - - - - - - - - - - - - -"
					+ " - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -", separate);

			document.add(dash);

			// Adding Description
			Paragraph Description = new Paragraph("Description", Heading);
			Description.setAlignment(Element.ALIGN_LEFT);
			document.add(Description);



			// Adding Line Item and proposed amount
			//Chunk glue = new Chunk(new VerticalPositionMark());

			Paragraph lineItem1 = new Paragraph("Line Items", TotalAmontFont);
			lineItem1.add(new Chunk(glue));
			lineItem1.add("Amount You Get");
			document.add(lineItem1);

			// Adding Working
			Paragraph workingA = new Paragraph("Working(A)" + "", TotalAmontFont);
			workingA.setAlignment(Element.ALIGN_LEFT);
			document.add(workingA);

			Integer workingTotal = 0;
			String workingAmount = "workingTotal";

			// Adding Amount
			List<WorkingVendorModel> vendorModels = model.getBidsModel().getWorkingVendorModel();
			for (WorkingVendorModel working : vendorModels) {
				if (working.getInline().equals(workingAmount)) {
					Paragraph inlineItemsA = new Paragraph("Total (A)", TotalAmontFont);
					workingTotal = working.getAmountYouGet();
					inlineItemsA.add(new Chunk(glue));
					inlineItemsA.add(working.getAmountYouGet().toString());
					document.add(inlineItemsA);
				} else {
					Paragraph inlineItemsA = new Paragraph(working.getInline(), font);
					inlineItemsA.add(new Chunk(glue));
					inlineItemsA.add(working.getAmountYouGet().toString());
					document.add(inlineItemsA);
				}
			}

			// Adding line
			Paragraph line = new Paragraph(" _______________________________________________________________________", separate);
			line.setSpacingAfter(5f);
			document.add(line);

			// Adding Travel
			Paragraph travelB = new Paragraph("Travel(B)" + "", TotalAmontFont);
			travelB.setAlignment(Element.ALIGN_LEFT);
			document.add(travelB);

			Integer travelTotal = 0;
			String travelAmount = "travelTotal";
			int count = 0;
			List<TravelCostVendorModel> travelModel = model.getBidsModel().getTravelCostVendorModel();
			for (TravelCostVendorModel travel : travelModel) {
				if (count >= 6) {
					document.newPage();
					document.add(image);
					Paragraph lineItem2 = new Paragraph("Line Items", TotalAmontFont);
					lineItem2.add(new Chunk(glue));
					lineItem2.add("Amount You Get");
					lineItem2.setSpacingBefore(20f);
					document.add(lineItem2);
					count = -2;
				}
				if (travel.getInline().equals(travelAmount)) {
					Paragraph inlineItemsB = new Paragraph("Total (B)", TotalAmontFont);
					travelTotal = travel.getAmountYouGet();
					inlineItemsB.add(new Chunk(glue));
					inlineItemsB.add(travel.getAmountYouGet().toString());
					document.add(inlineItemsB);
				} else {
					Paragraph inlineItemsB = new Paragraph(travel.getInline(), font);
					inlineItemsB.add(new Chunk(glue));
					inlineItemsB.add(travel.getAmountYouGet().toString());
					document.add(inlineItemsB);
				}

				count++;
			}

			document.add(line);

			// Adding Travel
			Paragraph otherC = new Paragraph("Other(C)" + "", TotalAmontFont);
			otherC.setAlignment(Element.ALIGN_LEFT);
			otherC.setIndentationLeft(43);
			document.add(otherC);
			// Adding amounts
			Integer otherTotal = 0;
			String otherAmount = "othersTotal";
			List<MiscellaneousVendorModel> miscellaneousVendorModels = model.getBidsModel()
					.getMiscellaneousVendorModel();
			for (MiscellaneousVendorModel other : miscellaneousVendorModels) {
				if (other.getInline().equals(otherAmount)) {
					Paragraph inlineItemsC = new Paragraph("Total (C)", TotalAmontFont);
					otherTotal = other.getAmountYouGet();
					inlineItemsC.add(new Chunk(glue));
					inlineItemsC.add(other.getAmountYouGet().toString());
					document.add(inlineItemsC);
				} else {
					Paragraph inlineItemsC = new Paragraph(other.getInline(), font);
					inlineItemsC.add(new Chunk(glue));
					inlineItemsC.add(other.getAmountYouGet().toString());
					document.add(inlineItemsC);
				}

			}

			// Adding additional value
			Paragraph additionalCostD = new Paragraph("Additional Cost (D)", TotalAmontFont);
			additionalCostD.add(new Chunk(glue));
			additionalCostD.add(model.getBidsModel().getTotalAddCostGet().toString());
			document.add(additionalCostD);

			document.add(line);

			// Adding voucher info
			Paragraph voucherApplied = new Paragraph("Voucher Applied (E)", TotalAmontFont);
			voucherApplied.add(new Chunk(glue));
			voucherApplied.add(model.getVoucherApplied());
			document.add(voucherApplied);

			Integer grandTotalAmount = workingTotal + travelTotal + otherTotal+model.getBidsModel().getTotalAddCostGet();
			// Adding grand total of A+B+C+D
			Paragraph grandTotal = new Paragraph("Total (A+B+C+D)", TotalAmontFont);
			grandTotal.add(new Chunk(glue));
			grandTotal.add(grandTotalAmount.toString());
			document.add(grandTotal);

			document.add(dash);


			// Adding Name of persone who raise PO
			Paragraph raisedBy = new Paragraph("Name: ", TotalAmontFont);
			raisedBy.setAlignment(Element.ALIGN_LEFT);
			raisedBy.setFont(font);
			raisedBy.add(userModel.getName());
			document.add(raisedBy);

			// Adding Name Value
			Paragraph nameValue = new Paragraph("(The person who raised the P.O)", font);
			nameValue.setAlignment(Element.ALIGN_LEFT);
			document.add(nameValue);

			Paragraph Footer2 = new Paragraph("(This is a computer generated invoice, no signature required)", note);
			Footer2.setSpacingBefore(30);
			Footer2.setAlignment(Element.ALIGN_CENTER);
			document.add(Footer2);

			document.close();
			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Pdf Generated";
	}

}
