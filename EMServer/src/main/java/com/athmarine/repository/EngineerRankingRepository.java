package com.athmarine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.athmarine.entity.EngineerRanking;

public interface EngineerRankingRepository extends JpaRepository<EngineerRanking, Integer> {
	
	@Query (value = "select * from engineer_ranking where company_Id=:id", nativeQuery = true)
	public List<EngineerRanking> findAllEngineerRankingByCompanyId(Integer id);
	
	public EngineerRanking findByEngineerId(Integer id);

}
