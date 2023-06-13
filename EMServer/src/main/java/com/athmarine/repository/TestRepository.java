package com.athmarine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.Test;


@Repository
public interface TestRepository extends JpaRepository<Test, Integer> {

}
