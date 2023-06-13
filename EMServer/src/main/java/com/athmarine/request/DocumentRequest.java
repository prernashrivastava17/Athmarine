package com.athmarine.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentRequest {
	
	private Integer turnOverId; 
	
	private Integer vendorCompanyId;
	
	private Integer customerCompanyId; 
	
	private Integer enginnerCertificateId;

}
