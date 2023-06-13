package com.athmarine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.MSTPorts;

@Repository
public interface MasterPortsRepository extends JpaRepository<MSTPorts,Integer>{

}
