package com.athmarine.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.Currency;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Integer> {
	
	Optional<Currency> findByCurrency(String currency);
	
	List<Currency> findAllByOrderByCurrencyAsc();

}
