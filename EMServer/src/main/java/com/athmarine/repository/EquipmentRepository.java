package com.athmarine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.Equipment;
import com.athmarine.entity.MSTEquipmentCategory;
import com.athmarine.request.EquipmentModel;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Integer> {

	public List<EquipmentModel> getByCategory(MSTEquipmentCategory category);
}
