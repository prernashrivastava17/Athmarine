package com.athmarine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.Equipment;
import com.athmarine.entity.MSTManufacturer;

@Repository
public interface ManufacturerRepository extends JpaRepository<MSTManufacturer, Integer> {

	public List<MSTManufacturer> findAllByEquipment(Equipment equipment);
	
}
