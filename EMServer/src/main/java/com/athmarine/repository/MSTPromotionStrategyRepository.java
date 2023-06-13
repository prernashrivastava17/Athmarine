package com.athmarine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.MSTPromotionStrategy;

@Repository
public interface MSTPromotionStrategyRepository extends JpaRepository<MSTPromotionStrategy,Integer>{

}
