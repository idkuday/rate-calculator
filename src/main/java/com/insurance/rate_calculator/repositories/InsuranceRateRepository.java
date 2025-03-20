package com.insurance.rate_calculator.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.insurance.rate_calculator.entities.AgeRange;
import com.insurance.rate_calculator.entities.Gender;
import com.insurance.rate_calculator.entities.InsuranceRate;
import com.insurance.rate_calculator.entities.ZipCode;

public interface InsuranceRateRepository extends JpaRepository<InsuranceRate, Long> {
    InsuranceRate findByZipCodeAndAgeRangeAndGender(ZipCode zipCode, AgeRange ageRange, Gender gender);
    List<InsuranceRate> findByGender(Gender gender);
    
    void deleteByZipCodeIdAndGenderId(Long zipId, Long genderId);
}

