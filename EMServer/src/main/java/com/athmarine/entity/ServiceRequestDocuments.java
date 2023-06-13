package com.athmarine.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.*;

@Entity
@Table(name = "service_request_documents")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ServiceRequestDocuments {

    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String documents;

    @ManyToOne
    @JoinColumn(name = "serviceRequest")
    private ServiceRequest serviceRequest;

    @Column(name = "created_by", updatable = false)
    @CreatedBy
    private String createdBy;

    @Column(name = "updated_by")
    @LastModifiedBy
    private String updatedBy;
}
