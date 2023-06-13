package com.athmarine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.MSTCountry;

@Repository
public interface MasterCountryRepository extends JpaRepository<MSTCountry, Integer> {

	public List<MSTCountry> findAllByOrderByNameAsc();;

	public List<MSTCountry> findAllByOrderByCurrencyAsc();
}
