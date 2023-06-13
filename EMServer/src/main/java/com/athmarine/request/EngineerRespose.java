package com.athmarine.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EngineerRespose {

	private MakeModel makeModel;

	private EngineerCertificateModel engineerCertificateModel;

	private ExperienceModel experienceModel;

}
