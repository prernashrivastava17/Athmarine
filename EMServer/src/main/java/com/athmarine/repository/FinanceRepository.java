package com.athmarine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.Finance;
import com.athmarine.entity.User;

@Repository
public interface FinanceRepository extends JpaRepository<Finance, Integer> {

	public Finance getFinanceByUser(User id);
}
