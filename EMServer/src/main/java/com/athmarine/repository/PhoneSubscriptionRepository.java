package com.athmarine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athmarine.entity.PhoneSubscription;

@Repository
public interface PhoneSubscriptionRepository extends JpaRepository<PhoneSubscription, Integer> {

}
