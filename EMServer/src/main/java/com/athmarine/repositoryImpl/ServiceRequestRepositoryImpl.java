package com.athmarine.repositoryImpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.athmarine.entity.ServiceRequest;
import com.athmarine.entity.ServiceRequestStatus;
import com.athmarine.entity.User;
import com.athmarine.repository.CustomerServiceRequestRepository;
import com.athmarine.repository.VesselRepository;
import com.athmarine.request.GetNewServiceRequestsRequest;
import com.athmarine.service.MasterCountryService;
import com.athmarine.service.VesselService;

@Repository
@Transactional
public class ServiceRequestRepositoryImpl implements CustomerServiceRequestRepository {

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	VesselRepository vesselRepository;

	@Autowired
	MasterCountryService countryService;

	@Autowired
	VesselService vesselService;

	@Override
	@SuppressWarnings("unchecked")
	public List<ServiceRequest> findAllServiceByRequester(User requester, GetNewServiceRequestsRequest request) {
		List<ServiceRequest> list = null;
		Session session = sessionFactory.openSession();
		@SuppressWarnings("deprecation")
		Criteria cr = session.createCriteria(ServiceRequest.class);
		cr.setFirstResult((request.getPage() - 1) * request.getSize());
		cr.setMaxResults(request.getSize());

		if (request.getRequestStatus().equals(ServiceRequestStatus.BIDS_RECEIVED)) {
			cr.add(Restrictions.disjunction()
					.add(Restrictions.like("requestStatus", ServiceRequestStatus.BIDS_RECEIVED))
					.add(Restrictions.like("requestStatus", ServiceRequestStatus.PO_RAISED))
					.add(Restrictions.like("requestStatus", ServiceRequestStatus.PO_ACCEPTED))
					.add(Restrictions.like("requestStatus", ServiceRequestStatus.WORK_IN_PROGRESS)));

		} else if (request.getRequestStatus().equals(ServiceRequestStatus.COMPLETED)) {
			cr.add(Restrictions.disjunction().add(Restrictions.like("requestStatus", ServiceRequestStatus.INVOICE_PAID))
					.add(Restrictions.like("requestStatus", ServiceRequestStatus.INVOICE_RAISED))
					.add(Restrictions.like("requestStatus", ServiceRequestStatus.COMPLETED)));
		} else {
			cr.add(Restrictions.eq("requestStatus", request.getRequestStatus()));
		}
		cr.addOrder(Order.asc("QuotationTime"));
	
		List<ServiceRequest> object = cr.list();

		if (object != null && object.size() > 0) {
			list = object;
		}

		return list;
	}

	@Override
	public List<ServiceRequest> findAllByRequester(User requester, GetNewServiceRequestsRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public List<ServiceRequest> findAllByRequester(User requester, GetNewServiceRequestsRequest request) {
//		List<ServiceRequest> list = null;
//		Session session = sessionFactory.openSession();
//		Criteria cr = session.createCriteria(ServiceRequest.class);
//		cr.setFirstResult((request.getPage() - 1) * request.getSize());
//		cr.setMaxResults(request.getSize());
//
//		cr.add(Restrictions.eq("requestStatus", request.getRequestStatus()));
//		if (request.getToDate() != null) {
//
//			cr.add(Restrictions.between("createdAt", request.getFromDate(), request.getToDate()));
//		}
//
//		if (request.getEta() != null) {
//
//			cr.add(Restrictions.between("createdAt", request.getFromDate(), request.getEta()));
//		}
//
//		if (request.getEtd() != null) {
//
//			cr.add(Restrictions.between("createdAt", request.getFromDate(), request.getEtd()));
//		}
//
//		if (request.getCountryId() != 0) {
//			MSTCountry country = countryService.findByCountryId(request.getCountryId());
//			cr.add(Restrictions.eq("country", country));
//		}
//
//		if (request.getVesselId() != 0) {
//			Vessel vessel = vesselService.findByIds(request.getVesselId());
//			cr.add(Restrictions.eq("vessel", vessel));
//		}
//		cr.addOrder(Order.asc("QuotationTime"));
//		List<ServiceRequest> object = cr.list();
//
//		if (object != null && object.size() > 0) {
//			list = object;
//		}
//
//		return list;
//	}

}
