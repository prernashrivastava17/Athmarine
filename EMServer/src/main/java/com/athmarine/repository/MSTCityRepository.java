package com.athmarine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.MSTCity;

@Repository
public interface MSTCityRepository extends JpaRepository<MSTCity, Integer> {

}
