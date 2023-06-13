package com.athmarine.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.athmarine.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

}
