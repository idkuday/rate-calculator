package com.insurance.rate_calculator.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.insurance.rate_calculator.entities.Gender;

public interface GenderRepository extends JpaRepository<Gender, Long> {
    Gender findByGender(String gender);
}
