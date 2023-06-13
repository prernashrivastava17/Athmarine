package com.athmarine.repository;

import com.athmarine.entity.ServiceRequest;
import com.athmarine.entity.ServiceRequestDocuments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRequestDocumentRepository extends JpaRepository<ServiceRequestDocuments,Integer> {

    List<ServiceRequestDocuments> findByServiceRequest(ServiceRequest serviceRequest);

}
