package com.athmarine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.PaymentTransaction;
import com.athmarine.entity.User;


@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Integer> {

	public PaymentTransaction findByUser(User user);

	@Query(value = "SELECT DISTINCT COUNT(*) from Payment_Transaction", nativeQuery = true)
	public Integer getAllPaymentCount();

	Boolean existsByUser(User user);

	@Query("Select p from PaymentTransaction p order by p.system_transactionId")
	public List<PaymentTransaction> findAllTransactions();

	@Query("Select p from PaymentTransaction p where p.system_transactionId=:txnId")
	public PaymentTransaction findTxnBySystemTransactionId(String txnId);

	@Query("Select p from PaymentTransaction p where p.user=:user AND p.transactionStatus='succeeded'")
	public PaymentTransaction findByUserandStatus(User user);

    @Query("Select p from PaymentTransaction p where p.user=:user")
    public List<PaymentTransaction> findTransactionsByUser(User user);
	
	@Query(value = "SELECT DISTINCT COUNT(*) from payment_transaction", nativeQuery = true)
	public Integer getAllPayment();

}