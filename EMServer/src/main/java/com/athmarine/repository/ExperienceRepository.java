package com.athmarine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.EngineerEquiments;
import com.athmarine.entity.Experience;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Integer> {

	public List<Experience> findAllByEnggequip(EngineerEquiments id);

}
