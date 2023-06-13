package com.athmarine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.Feedback;
import com.athmarine.entity.User;
import com.athmarine.entity.VendorCompany;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {

	public List<Feedback> findAllByVendorCompany(VendorCompany vendorCompany);

	public List<Feedback> findAllByEngineer(User engineer);

}
