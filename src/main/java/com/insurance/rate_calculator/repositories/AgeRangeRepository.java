package com.insurance.rate_calculator.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.insurance.rate_calculator.entities.AgeRange;

public interface AgeRangeRepository extends JpaRepository<AgeRange, Long> {
    @Query("SELECT ar FROM AgeRange ar WHERE :age BETWEEN ar.ageFrom AND ar.ageTo")
    AgeRange findByAge(@Param("age") int age);
}
