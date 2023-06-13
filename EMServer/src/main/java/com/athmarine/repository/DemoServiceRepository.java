package com.athmarine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.User;
import com.athmarine.entity.VendorServices;

@Repository
public interface DemoServiceRepository extends JpaRepository<VendorServices, Integer> {

	public List<VendorServices> findAllByCompanyId(User id);

}
