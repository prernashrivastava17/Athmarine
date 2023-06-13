package com.athmarine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.MSTState;

@Repository
public interface MasterStateRepository extends JpaRepository<MSTState, Integer> {
	
	@Query(value="Select * from mst_state where id in :ids",nativeQuery=true)
	List<MSTState> findAllByStateList(List<Integer> ids);


}
