package com.athmarine.service;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.athmarine.response.EngineerCalendarResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.BidEngineerRelation;
import com.athmarine.exception.EngineerChargeNotFoundException;
import com.athmarine.exception.EngineerEquimentsNotFoundException;
import com.athmarine.exception.EngineerException;
import com.athmarine.exception.ServiceNotFoundException;
import com.athmarine.exception.ServiceRequestNotFoundException;
import com.athmarine.repository.EngineerBidRelationRepository;
import com.athmarine.request.BidEngineerRelationModel;
import com.athmarine.resources.AppConstant;
import com.athmarine.response.EngineerServiceReportResponseModel;
import com.athmarine.response.GetServiceRequestCount;
import com.athmarine.response.ServiceRequestResponseModel;

@Service
public class EngineerService {

	@Autowired
	EngineerBidRelationRepository engineerBidRelationRepository;
	
	@Autowired
	public ServiceRequestService serviceRequestService;
	
	public GetServiceRequestCount getAllCountsByEngineerId(@NotNull Integer engineerId) {
	
	checkEngineerIdExist(engineerId);	
	
		return GetServiceRequestCount.builder()
				.jobAssigned(engineerBidRelationRepository.findByEngineerIdAndEngineerStatus(engineerId, "ASSIGNED"))
				.jobInProgress(engineerBidRelationRepository.findByEngineerIdAndEngineerStatus(engineerId, "JOB_IN_PROGRESS"))
				.serviceReportUploaded(engineerBidRelationRepository.getServiceReportUploadedCount(engineerId))
				.build();
	}

	public void checkEngineerIdExist(@NotNull Integer engineerId) {
		
		if(engineerBidRelationRepository.checkEngineerIdExist(engineerId)==0)
		{
			throw new EngineerEquimentsNotFoundException(AppConstant.ErrorMessages.VENDOR_ENGINEER_NOT_EXIST,
					AppConstant.ErrorCodes.VENDOR_ENGINEER_ERROR_CODE,
					AppConstant.ErrorTypes.VENDOR_ENGINEER_EXIST_ERROR);
		}
	}

	public String updateStatusToInProgress(@NotNull Integer engineerId,@NotNull Integer serviceRequestId) {
		checkEngineerIdExist(engineerId);
		checkServiceRequestIdExist(serviceRequestId);
		Integer count=engineerBidRelationRepository.checkDuplicatesByEngineerIdServiceId(engineerId,serviceRequestId);
		if (count>1)
			return "Duplicate data present in Records";
		BidEngineerRelation job=engineerBidRelationRepository.findJobByEngineerIdServiceId(engineerId, serviceRequestId);
		job.setEngineerStatus("JOB_IN_PROGRESS");
		engineerBidRelationRepository.save(job);	
		return "Status Update Successfully";
	}

	private void checkServiceRequestIdExist(@NotNull Integer serviceRequestId) {
		if(engineerBidRelationRepository.checkRequestIdExist(serviceRequestId)==0)
		{
			throw new ServiceRequestNotFoundException(AppConstant.ErrorMessages.SERVICE_REQUEST_NOT_EXIST, 
					AppConstant.ErrorCodes.SERVICE_REQUEST_ERROR_CODE,
					AppConstant.ErrorTypes.SERVICE_REQUEST_EXIST_ERROR);
		}
		
	}

	public String prepareServiceReport(BidEngineerRelationModel serviceReportDetails) {
		if(serviceReportDetails.getEngineerId()==null || serviceReportDetails.getServiceRequestId()==null)
			return "EngineerId Or Service RequestID is NULL";
		checkEngineerIdExist(serviceReportDetails.getEngineerId());
		checkServiceRequestIdExist(serviceReportDetails.getServiceRequestId());
		BidEngineerRelation job=engineerBidRelationRepository.findJobByEngineerIdServiceId(serviceReportDetails.getEngineerId(),
				serviceReportDetails.getServiceRequestId());
		job.setServiceReport(serviceReportDetails.getServiceReport());
		engineerBidRelationRepository.save(job);
		
		return "Service Report Upload Successfully";
	}

	public Object getAllServiceReport(@NotNull Integer engineerId) {

		checkEngineerIdExist(engineerId);
		
		List<EngineerServiceReportResponseModel> serviceDetails =new ArrayList<EngineerServiceReportResponseModel>();
		
		
		List<BidEngineerRelation> jobs=engineerBidRelationRepository.findAllJobsByEngineerId(engineerId);
		jobs.stream().forEach(job-> {
			
			ServiceRequestResponseModel requestService=serviceRequestService.getServiceRequestById(job.getServiceRequest().getId());
			serviceDetails.add(EngineerServiceReportResponseModel.builder()
					.requestId(requestService.getRequestUID())
					.vessel(requestService.getVessel().getVesselName())
					.category(requestService.getCategory().getName())
					.country(requestService.getCountry().getName())
					.port(requestService.getPort().getName())
					.ETA(requestService.getETA())
					.ETD(requestService.getETD())
					.serviceRequestId(requestService.getId())
					.serviceReport(job.getServiceReport())
					.serviceRequestOn(requestService.getServiceRequestOn())
					.build());
		});
		if (serviceDetails.size()==0)
			return  "Sorry NO Data Found";
		return serviceDetails;
	}

	public Object getAllJobsByStatus(@NotNull Integer engineerId,String status) {
		
		checkEngineerIdExist(engineerId);
		
		List<EngineerServiceReportResponseModel> serviceDetails =new ArrayList<EngineerServiceReportResponseModel>();
		
		List<BidEngineerRelation> jobs=engineerBidRelationRepository.findAllJobs(engineerId,status);
		
		jobs.stream().forEach(job->
		{
			ServiceRequestResponseModel requestService=serviceRequestService.getServiceRequestById(job.getServiceRequest().getId());
			serviceDetails.add(EngineerServiceReportResponseModel.builder()
					.requestId(requestService.getRequestUID())
					.vessel(requestService.getVessel().getVesselName())
					.category(requestService.getCategory().getName())
					.country(requestService.getCountry().getName())
					.port(requestService.getPort().getName())
					.ETA(requestService.getETA())
					.ETD(requestService.getETD())
					.serviceRequestId(requestService.getId())
					.serviceReport(job.getServiceReport())
					.serviceRequestOn(requestService.getServiceRequestOn())
					.issueType(requestService.getIssueType())
					.status(job.getEngineerStatus())
					.build());
		});
		if (serviceDetails.size()==0)
			return  "Sorry NO Data Found";
		return serviceDetails;
	}

    public Object getEngineerJobCalendar(Integer engineerId,Integer month) {
		checkEngineerIdExist(engineerId);

		if (month<=0 || month>12)
			return "Please enter correct month";
		List<EngineerCalendarResponse> engineerJobList=new ArrayList<EngineerCalendarResponse>();
		engineerJobList= convertToCalendarResponseModel(engineerId,month);
		if (engineerJobList.size()==0)
		{
			return "Sorry, This month no jobs available";
		}
		return engineerJobList;
    }

	public List<EngineerCalendarResponse> convertToCalendarResponseModel(Integer engineerId, Integer month) {
		List<EngineerCalendarResponse> engineerJobList=new ArrayList<EngineerCalendarResponse>();

		List<BidEngineerRelation> engineerRelations=engineerBidRelationRepository.getAllJObsByEngineerIdAndServiceMonth(engineerId,month);
		engineerRelations.forEach(job->{
			engineerJobList.add(EngineerCalendarResponse.builder()
					.engineerId(job.getEngineerId())
					.serviceOn(job.getServiceOn())
					.serviceEndOn(job.getServiceEndOn())
					.build());
		});
		return engineerJobList;
	}
}
