package com.athmarine.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.athmarine.entity.EmailSubscription;

public interface EmailSubscriptionRepository extends JpaRepository<EmailSubscription, Integer> {

}
