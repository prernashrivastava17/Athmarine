package com.athmarine.request;

import com.athmarine.response.FeedbackResponseModel;
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
public class EngineerRatingResponseModel {

	private Integer id;

	private String name;

	private FeedbackResponseModel feedbackResponseModel;

}
