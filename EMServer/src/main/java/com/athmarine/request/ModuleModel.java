package com.athmarine.request;

import java.util.List;

import com.athmarine.entity.Role;
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
public class ModuleModel {

	private Integer id;

	private String parentPermission;

	private String permission;

	private boolean addRight;

	private boolean viewRight;

	private boolean modifyRight;

	private int status;

	private List<Role> role;
}
