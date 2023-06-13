package com.athmarine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.Turnover;

@Repository
public interface TurnoverRepository extends JpaRepository<Turnover, Integer> {

}