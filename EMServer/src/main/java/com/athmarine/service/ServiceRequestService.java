package com.athmarine.service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.athmarine.entity.*;
import com.athmarine.exception.BidsNotFoundException;
import com.athmarine.exception.PaginationException;
import com.athmarine.exception.ServiceRequestStatusException;
import com.athmarine.repository.*;
import com.athmarine.request.*;
import com.athmarine.response.BidderDashboardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.athmarine.exception.ServiceRequestNotFoundException;
import com.athmarine.resources.AppConstant;
import com.athmarine.response.GetServiceRequestCount;
import com.athmarine.response.ServiceRequestResponseModel;

@Service
public class ServiceRequestService {

	@Autowired
	ServiceRequestRepository serviceRequestRepository;

	@Autowired
	MasterCountryService masterCountryService;

	@Autowired
	MasterPortsService masterPortsService;

	@Autowired
	UserDetailsServiceImpl userDetailsServiceImpl;

	@Autowired
	MasterEquipmentCategoryService categoryService;

	@Autowired
	VesselService vesselService;

	@Autowired
	ServiceRequestDocumentRepository serviceRequestDocumentRepository;

	@Autowired
	BidderRequestRelationRepository bidderRequestRelationRepository;

	@Autowired
	BidsRepository bidsRepository;

	@Autowired
	BidsService bidsService;

	@Autowired
	PurchaseOrderRepository purchaseOrderRepository;

	@Autowired
	HeadOfCompanyRepository headOfCompanyRepository;

	@Value("${pagination.size}")
	private Integer size;


	//-------------Method to Create Service Request-----------//

	public ServiceRequestResponseModel createServiceRequest(ServiceRequestModel serviceRequestModel) {

		serviceRequestModel.setRequestStatus(ServiceRequestStatus.NEW_REQUEST);
		serviceRequestModel.setRequestUID(generateRequestUID());
		ServiceRequest serviceRequest = serviceRequestRepository.save(convertToEntity(serviceRequestModel));

		List<ServiceRequestDocumentModel> serviceRequestDocumentModels = serviceRequestModel.getServiceRequestDocumentModels();

		saveServiceRequestDocuments(serviceRequestDocumentModels,serviceRequest);

		return convertToResponseModel(serviceRequestRepository.findById(serviceRequest.getId()).get());
	}


	public List<ServiceRequestResponseModel> getAllServiceRequestByRequesterId(int id) {

		User requester = userDetailsServiceImpl.findByIds(id);
		return serviceRequestRepository.findAllByRequester(requester).stream()
				.map(this::convertToResponseModel).collect(Collectors.toList());
	}

	public List<ServiceRequestResponseModel> convertToServiceResponseModel(List<ServiceRequest> entityList) {
		return entityList.stream()
				.map(this::convertToResponseModel).collect(Collectors.toList());
	}


	public ServiceRequest findByIds(Integer id) {
		if (id == null) {

			throw new ServiceRequestNotFoundException(AppConstant.ErrorTypes.SERVICE_REQUEST_EXIST_ERROR,
					AppConstant.ErrorCodes.SERVICE_REQUEST_ERROR_CODE,
					"ServiceRequest " + AppConstant.ErrorMessages.ID_EMPTY_MESSAGE);

		}
		return serviceRequestRepository.findById(id)
		.orElseThrow(() -> new ServiceRequestNotFoundException(
				AppConstant.ErrorTypes.SERVICE_REQUEST_EXIST_ERROR, AppConstant.ErrorCodes.SERVICE_REQUEST_ERROR_CODE,
				AppConstant.ErrorMessages.SERVICE_REQUEST_NOT_EXIST));
	}


	// ---------------- Method to convert ServiceRequestModel to ServiceRequest-----------------//

	private ServiceRequest convertToEntity(ServiceRequestModel model) {

		return ServiceRequest.builder().requestUID(model.getRequestUID()).requestStatus(model.getRequestStatus().toString())
				.vessel(vesselService.convertToEntity(model.getVessel())).country(masterCountryService.findByCountryId(model.getCountry().getId()))
				.port(masterPortsService.findByIds(model.getPort().getId())).locationType(model.getLocationType())
				.ETA(model.getETA()).ETD(model.getETD()).serviceRequestOn(model.getServiceRequestOn())
				.equipmentCategory(categoryService.findByIds(model.getEquipmentCategory().getId()))
				.equipmentName(model.getEquipmentName()).manufacturer(model.getManufacturer())
				.model(model.getModel()).issueType(model.getIssueType()).preferredCurrency(model.getPreferredCurrency())
				.quotationTime(model.getQuotationTime()).serviceTimeCategory(model.getServiceTimeCategory())
				.description(model.getDescription()).requester(userDetailsServiceImpl.findByIds(model.getRequesterId()))
				.build();

	}

	//-------------Method to Convert ServiceRequest to ServiceRequestResponseModel------------//

	private ServiceRequestResponseModel convertToResponseModel(ServiceRequest entity) {

		return ServiceRequestResponseModel.builder().id(entity.getId())
				.requestUID(entity.getRequestUID()).requestStatus(entity.getRequestStatus())
				.vessel(vesselService.convertToResponseModel(entity.getVessel()))
				.country(masterCountryService.convertToModel(entity.getCountry()))
				.port(masterPortsService.convertToModels(entity.getPort()))
				.location(entity.getLocationType()).ETA(entity.getETA())
				.ETD(entity.getETD()).serviceRequestOn(entity.getServiceRequestOn())
				.preferredCurrency(entity.getPreferredCurrency())
				.category(categoryService.convertToModel(entity.getEquipmentCategory()))
				.equipmentName(entity.getEquipmentName()).manufacturer(entity.getManufacturer())
				.model(entity.getModel()).issueType(entity.getIssueType()).description(entity.getDescription())
				.quotationTime(entity.getQuotationTime()).serviceTimeCategory(entity.getServiceTimeCategory())
				.bidRemainingTime(getRemainingTimeForBidding(entity.getQuotationTime(),entity.getCreatedAt()))
				.serviceRequestDocumentModels(convertToServiceRequestDocumentModel(
						serviceRequestDocumentRepository.findByServiceRequest(entity)))
				.userModel(userDetailsServiceImpl.convertToUserModel(entity.getRequester())).build();
	}

	//---------------Method to generate request unique id --------------//

	private String generateRequestUID()
	{
		StringBuilder uid = new StringBuilder();
		uid.append("RFQ");
		uid.append(LocalDateTime.now().getYear() % 2000);
		Random rnd = new Random();
		int number = rnd.nextInt(999999);
		uid.append(String.format("%06d", number));
		return uid.toString();
	}


	//---------------Method to convert ServiceRequestDocuments to ServiceRequestDocumentModel -----------//

	private List<ServiceRequestDocumentModel> convertToServiceRequestDocumentModel(List<ServiceRequestDocuments> serviceRequestDocuments)
	{
		return serviceRequestDocuments.stream().map(docs -> {
			ServiceRequestDocumentModel serviceRequestDocumentModel = new ServiceRequestDocumentModel();
			serviceRequestDocumentModel.setDocuments(docs.getDocuments());
			return  serviceRequestDocumentModel;
		}).collect(Collectors.toList());
	}


	//---------Method to save all service request documents in the db--------------//

	private void saveServiceRequestDocuments(List<ServiceRequestDocumentModel> serviceRequestDocumentModels,
											 ServiceRequest serviceRequest)
	{
		List<ServiceRequestDocuments> serviceRequestDocumentsList =

				serviceRequestDocumentModels.stream().map(doc -> {

					ServiceRequestDocuments serviceRequestDocuments = new ServiceRequestDocuments();

					serviceRequestDocuments.setDocuments(doc.getDocuments());

					serviceRequestDocuments.setServiceRequest(serviceRequest);

					return serviceRequestDocuments;

				}).collect(Collectors.toList());

		serviceRequestDocumentRepository.saveAll(serviceRequestDocumentsList);
	}


	//-------------Method to Get All Requests By Status----------------//

	public List<ServiceRequestResponseModel> getAllServiceRequestsByStatus(ServiceRequestStatus serviceRequestStatus,
																		   Integer bidderId, Integer page)
	{
		if(page==null){
			throw new PaginationException(AppConstant.ErrorTypes.PAGE_NUMBER_NULL,
					AppConstant.ErrorCodes.PAGE_NUMBER_NULL,AppConstant.ErrorMessages.PAGE_NUMBER_NULL);
		}

		userDetailsServiceImpl.findByIds(bidderId);

		List<ServiceRequest> serviceRequestList = serviceRequestRepository.findByRequestStatus(serviceRequestStatus.name());

		if(serviceRequestList.size()<1)
		{
			throw new ServiceRequestNotFoundException(AppConstant.ErrorTypes.SERVICE_NOT_EXIST_ERROR,
					AppConstant.ErrorCodes.INVALID_INPUT_ERROR_CODE,
					AppConstant.ErrorMessages.SERVICE_REQUEST_NOT_EXIST);
		}

		if(serviceRequestStatus.name().equals(ServiceRequestStatus.NEW_REQUEST.name())){

			Iterator<ServiceRequest> iterator = serviceRequestList.listIterator();

			while(iterator.hasNext()){

				ServiceRequest serviceRequest = iterator.next();

				BidderRequestRelation bidderRequestRelation = bidderRequestRelationRepository
						.findByBidderIdAndRequestIdAndRequestStatus(bidderId,serviceRequest.getId(),
								ServiceRequestStatus.NOT_INTERESTED.name());

				if(bidderRequestRelation!=null){
					iterator.remove();
				}
				else{
					bidderRequestRelation = bidderRequestRelationRepository
							.findByBidderIdAndRequestIdAndRequestStatus(bidderId,serviceRequest.getId(),
									ServiceRequestStatus.INTERESTED.name());

					if(bidderRequestRelation!=null){
						iterator.remove();
					}
				}
			}
		}

		if(((page*size)+size)<serviceRequestList.size()) {
			return convertToServiceResponseModel(serviceRequestList.subList(page * size, (page * size) + size));
		}
		else if((page*size)<serviceRequestList.size()){
			return convertToServiceResponseModel(serviceRequestList.subList(page * size, serviceRequestList.size()));
		}
		else if((page*size)==serviceRequestList.size()){
			throw new BidsNotFoundException(AppConstant.ErrorTypes.BIDS_EXIST_ERROR,
					AppConstant.ErrorCodes.BIDS_ERROR_CODE,AppConstant.ErrorMessages.BIDS_NOT_EXIST);
		}
		else{
			return convertToServiceResponseModel(serviceRequestList);
		}
	}


	//---------Method to Get Service Request By Id------------//

	public ServiceRequestResponseModel getServiceRequestById(Integer id) {

		return convertToResponseModel(serviceRequestRepository.findById(id).orElseThrow(
				() -> new ServiceRequestNotFoundException(AppConstant.ErrorTypes.SERVICE_REQUEST_EXIST_ERROR,
						AppConstant.ErrorCodes.SERVICE_REQUEST_ERROR_CODE,
						AppConstant.ErrorMessages.SERVICE_REQUEST_NOT_EXIST)));
	}


	//----------Method to Get Request's Left Out Time for Bidding-----------------//

	public long getRemainingTimeForBidding(int quotationTime, Date createdAtTime)
	{
		Date currentDate = new Date();

		long diffInMillies = Math.abs(currentDate.getTime()-createdAtTime.getTime());

		float seventyPercent = quotationTime * 0.7f;

		long quotationTimeInMillies = (long) seventyPercent * 60 * 60 * 1000;

		return quotationTimeInMillies-diffInMillies;
	}

	//-----------Method to change Request's status by Bidder Id---------//

	public String changeRequestStatusByBidderId(Integer serviceRequestId, Integer bidderId, String requestStatus){

		if (!bidsService.checkStatusExists(requestStatus)) {
			throw new ServiceRequestStatusException(AppConstant.ErrorTypes.STATUS_NOT_FOUND,
					AppConstant.ErrorCodes.STATUS_NOT_FOUND, AppConstant.ErrorMessages.STATUS_NOT_FOUND);
		}

		findByIds(serviceRequestId);
		User user = userDetailsServiceImpl.findByIds(bidderId);

		BidderRequestRelation bidderRequestRelation = new BidderRequestRelation();
		bidderRequestRelation.setRequestId(serviceRequestId);
		bidderRequestRelation.setBidderId(bidderId);
		bidderRequestRelation.setRequestStatus(requestStatus);
		bidderRequestRelationRepository.save(bidderRequestRelation);

		if(requestStatus.equals(ServiceRequestStatus.INTERESTED.name())) {

			ServiceRequest serviceRequest=findByIds(serviceRequestId);
			UserModel hoc = userDetailsServiceImpl.getUserHeadByCompanyId(user.getCompanyId().getId());
			headOfCompanyRepository.save(HeadOfCompany.builder().headId(hoc.getId())
					.statusId(serviceRequestId).status("INTERESTED_REQUEST")
							.bidderId(bidderId)
							.serviceOn(serviceRequest.getServiceRequestOn())
					.build());

			return "Thank you for showing your Interest";
		}
		else {
			return "Request is Not Interested Successfully. You can check it in History Tab";
		}
	}


	//-----------Method to get Request's count according to the status by Bidder Id----------//

	public BidderDashboardResponse getServiceRequestsCounts(Integer id)
	{
		User user = userDetailsServiceImpl.findByIds(id);

		updateAllRequestsStatus(user);

		List<String> status = Arrays.asList("BIDS_IN_PROGRESS","BID_RAISED","APPROVER_PENDING","DECLINED",
				"WORK_IN_PROGRESS","COMPLETED","INVOICE_RAISED","PO_RAISED");

		List<Bids> allBids = bidsRepository.findByBidderId(id,status);

		List<BidderRequestRelation> bidderRequestRelations = bidderRequestRelationRepository
				.findByBidderIdAndRequestStatus(id,ServiceRequestStatus.INTERESTED.name(),
						Pageable.unpaged());

		int liveStatusCount = allBids.size()+bidderRequestRelations.size();

		int newRequestCount = serviceRequestRepository.countByRequestStatus(ServiceRequestStatus.NEW_REQUEST.name());

		int interestedRequestCount = bidderRequestRelationRepository.countByBidderIdAndRequestStatus(id,
				ServiceRequestStatus.INTERESTED.name());

		int bidInProgressCount = bidsRepository.countByUserIdAndBidsStatusAndStatus(user,
				ServiceRequestStatus.BIDS_IN_PROGRESS.name(),true);

		int bidsRaisedCount = bidsRepository.countByUserIdAndBidsStatusAndStatus(user,
				ServiceRequestStatus.BID_RAISED.name(),true);

		int purchaseOrderCount = bidsRepository.countByUserIdAndBidsStatusAndStatus(user,
				ServiceRequestStatus.PO_RAISED.name(),true);

		int engineerAllocationCount = bidsRepository.countByUserIdAndBidsStatusAndStatus(user,
				ServiceRequestStatus.PO_ACCEPTED.name(),true);

		int workInProgressCount = bidsRepository.countByUserIdAndBidsStatusAndStatus(user,
				ServiceRequestStatus.WORK_IN_PROGRESS.name(),true);

		int raiseAddCostCount = bidsRepository.countByUserIdAndAdditionalCostStatusAndStatus(user,
				ServiceRequestStatus.RAISED.name(),true);


		int completedJobsCount = bidsRepository.countByUserIdAndBidsStatusAndStatus(user,
				ServiceRequestStatus.COMPLETED.name(),true);

		int invoiceRaisedCount = bidsRepository.countByUserIdAndBidsStatusAndStatus(user,
				ServiceRequestStatus.INVOICE_RAISED.name(),true);

		return BidderDashboardResponse.builder().liveStatus(liveStatusCount).newRequest(newRequestCount)
				.interestedRequest(interestedRequestCount).bidsInProgress(bidInProgressCount)
				.raisedBids(bidsRaisedCount).purchaseOrderReceived(purchaseOrderCount)
				.engineerAllocation(engineerAllocationCount).workInProgress(workInProgressCount)
				.completedJobs(completedJobsCount).raisedAddCostDiscount(raiseAddCostCount)
				.invoiceRaised(invoiceRaisedCount).build();
	}


	//----------Method to update all requests before counting----------//

	public void updateAllRequestsStatus(User user){

		List<ServiceRequest> serviceRequests = serviceRequestRepository.findByRequestStatus(
				ServiceRequestStatus.NEW_REQUEST.name());

		for(ServiceRequest s:serviceRequests){

			Date currentDate = new Date();

			long diffInMillis = Math.abs(currentDate.getTime()-s.getCreatedAt().getTime());

			float seventyPercent = s.getQuotationTime() * 0.7f;

			float quotationTime70InMillis = seventyPercent * 60 * 60 * 1000;

			long quotationTimeInMillis = (long) s.getQuotationTime() * 60 * 60 * 1000;

			if(diffInMillis>=quotationTime70InMillis){
				s.setRequestStatus(ServiceRequestStatus.TIMED_OUT.name());
				serviceRequestRepository.save(s);

				List<BidderRequestRelation> bidderRequestRelations = bidderRequestRelationRepository
						.findByRequestIdAndRequestStatus(s.getId(),ServiceRequestStatus.NOT_INTERESTED.name());

				if(bidderRequestRelations.size()>0){
					bidderRequestRelations.forEach(bidderRequestRelation -> {
						bidderRequestRelation.setRequestStatus(ServiceRequestStatus.TIMED_OUT.name());
						bidderRequestRelationRepository.save(bidderRequestRelation);
					});
				}

				bidsService.calculateFareValue(s.getId());
			}

			if(diffInMillis>=quotationTimeInMillis){
				List<Bids> bids = bidsRepository.findBidsByRequestIdAndBidStatus(s.getId(),
						ServiceRequestStatus.BIDS_IN_PROGRESS.name());

				for(Bids b:bids) {
					b.setBidsStatus(ServiceRequestStatus.BID_RAISED.name());
					UserModel hoc = userDetailsServiceImpl.getUserHeadByCompanyId(user.getCompanyId().getId());
					headOfCompanyRepository.save(HeadOfCompany.builder().headId(hoc.getId())
							.statusId(b.getId()).status("RAISED_BID").build());
					bidsRepository.save(b);
				}
			}
		}

		bidsService.updatePOs();
		bidsService.updateStatusToWorkInProgress(user);
		bidsService.updateStatusToCompleted(user);
	}

	//-----------Method to get Bidder's Requests By Status------------//

	public List<ServiceRequestResponseModel> getBiddersRequestsByStatus(Integer bidderId, String requestStatus
			, Integer page) {

		if(page==null){
			throw new PaginationException(AppConstant.ErrorTypes.PAGE_NUMBER_NULL,
					AppConstant.ErrorCodes.PAGE_NUMBER_NULL,AppConstant.ErrorMessages.PAGE_NUMBER_NULL);
		}

		userDetailsServiceImpl.findByIds(bidderId);

		if (!bidsService.checkStatusExists(requestStatus)) {
			throw new ServiceRequestStatusException(AppConstant.ErrorTypes.STATUS_NOT_FOUND,
					AppConstant.ErrorCodes.STATUS_NOT_FOUND, AppConstant.ErrorMessages.STATUS_NOT_FOUND);
		}

		if(!requestStatus.equals(ServiceRequestStatus.INTERESTED.name())
				&& !requestStatus.equals(ServiceRequestStatus.NOT_INTERESTED.name())){
			throw new ServiceRequestStatusException(AppConstant.ErrorTypes.STATUS_NOT_FOUND,
					AppConstant.ErrorCodes.STATUS_NOT_FOUND, AppConstant.ErrorMessages.STATUS_NOT_FOUND);
		}


		List<BidderRequestRelation> bidderRequestRelations = bidderRequestRelationRepository
				.findByBidderIdAndRequestStatus(bidderId, requestStatus, PageRequest.of(page,size));

		/*if(requestStatus.equals(ServiceRequestStatus.NOT_INTERESTED.name())){
			bidderRequestRelations.removeIf(requests -> {
				ServiceRequest serviceRequest = findByIds(requests.getRequestId());
				if(serviceRequest.getRequestStatus().equals(ServiceRequestStatus.TIMED_OUT)){
					return requests;
				}
			});
		}*/

		if(bidderRequestRelations.size()>0){
			return bidderRequestRelations.stream().map(b -> {

				ServiceRequestResponseModel responseModel = convertToResponseModel(findByIds(b.getRequestId()));

				Integer bidsSubmitted = bidsRepository.countByRequestIdAndBidStatus(responseModel.getId(),
						ServiceRequestStatus.BIDS_IN_PROGRESS.name());

				responseModel.setBidsSubmitted(bidsSubmitted);

				return responseModel;

			}).collect(Collectors.toList());
		}
		else {
			throw new ServiceRequestNotFoundException(AppConstant.ErrorTypes.INTERESTED_REQUESTS_NOT_FOUND,
					AppConstant.ErrorCodes.INTERESTED_REQUESTS_NOT_FOUND,
					AppConstant.ErrorMessages.INTERESTED_REQUESTS_NOT_FOUND);
		}
	}
}
