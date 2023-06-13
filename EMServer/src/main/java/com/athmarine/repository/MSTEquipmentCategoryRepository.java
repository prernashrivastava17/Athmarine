package com.athmarine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.MSTEquipmentCategory;

@Repository
public interface MSTEquipmentCategoryRepository extends JpaRepository<MSTEquipmentCategory,Integer>{

}
