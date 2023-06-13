package com.athmarine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.EngineerCertificates;
import com.athmarine.entity.EngineerEquiments;

@Repository
public interface EngineerCertificateRepository extends JpaRepository<EngineerCertificates, Integer> {

	public List<EngineerCertificates> findAllByEnggequip(EngineerEquiments id);

}
