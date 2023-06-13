package com.athmarine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.ServiceCategoryEquipmentMapping;

@Repository
public interface ServiceCategoryEquipmentRepository extends JpaRepository<ServiceCategoryEquipmentMapping, Integer> {

	@Query("Select s from ServiceCategoryEquipmentMapping s where s.vendorServices.id=:id")
	public List<ServiceCategoryEquipmentMapping> findByService(Integer id);

	@Query("Select s from ServiceCategoryEquipmentMapping s where s.vendorServices.id=:sid AND s.mstEquipmentCategory.id=:eid")
	public List<ServiceCategoryEquipmentMapping> findByServiceAndCategory(Integer sid, Integer eid);

}
