package com.athmarine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.Make;

@Repository
public interface MakeRepository extends JpaRepository<Make, Integer> {

}
