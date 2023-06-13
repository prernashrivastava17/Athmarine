package com.athmarine.request;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateETAAndETDModel {

	private Integer id;

	private Date ETA;

	private Date ETD;

}
