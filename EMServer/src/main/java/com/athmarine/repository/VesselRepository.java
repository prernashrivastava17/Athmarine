package com.athmarine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.Vessel;

@Repository
public interface VesselRepository extends JpaRepository<Vessel, Integer> {

	List<Vessel> findAllByShipnameAndIMO(String shipName,Integer IMO);


}
