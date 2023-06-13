package com.athmarine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.Voucher;

import java.util.List;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer> {

	@Query(value = "SELECT DISTINCT COUNT(*) from voucher", nativeQuery = true)
	Integer getAllVoucher();

	@Query(value="SELECT * FROM voucher where company_id=:companyId AND redeemed=false", nativeQuery = true)
	List<Voucher> findAllByCompanyId(Integer companyId);

	@Query(value="SELECT COUNT(*) FROM voucher where company_id=:companyId AND redeemed=false", nativeQuery = true)
	Integer countAllByCompanyId(Integer companyId);

}
