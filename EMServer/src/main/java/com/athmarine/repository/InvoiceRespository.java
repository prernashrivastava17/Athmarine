package com.athmarine.repository;

import com.athmarine.entity.Invoice;
import com.athmarine.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRespository extends JpaRepository<Invoice,Integer> {

    Integer countByUserAndIsPaymentStatus(User user, Boolean status);

    List<Invoice> findByUserAndIsPaymentStatus(User user, Boolean status);

}
