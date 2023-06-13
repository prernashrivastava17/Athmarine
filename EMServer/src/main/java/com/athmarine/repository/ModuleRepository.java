package com.athmarine.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.athmarine.entity.Module;

public interface ModuleRepository extends JpaRepository<Module, Integer> {

	public Optional<Module> findById(Integer id);
}
