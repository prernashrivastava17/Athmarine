package com.athmarine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.DepartmentName;

@Repository
public interface DepartmentNameRepositroy extends JpaRepository<DepartmentName, Integer> {

	Boolean existsByDepartmentName(String appName);

}