package com.insurance.rate_calculator.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.insurance.rate_calculator.entities.ZipCode;

public interface ZipCodeRepository extends JpaRepository<ZipCode, Long> {
	@Query("SELECT z FROM ZipCode z WHERE z.zipFrom <= :zipCode AND z.zipTo > :zipCode AND z.gender.id = :genderId")
	ZipCode findByZipRange(@Param("zipCode") int zipCode, @Param("genderId") Long genderId);

	
	@Query(value = """
		    SELECT z.* 
		    FROM zip_code z
		    JOIN gender g ON z.gender_id = g.id
		    WHERE :inputZipCode NOT BETWEEN z.zip_from AND z.zip_to
		    AND g.id = :genderId
		    ORDER BY LEAST(ABS(:inputZipCode - z.zip_from), ABS(:inputZipCode - z.zip_to)) ASC
		    LIMIT 1
		""", nativeQuery = true)
	ZipCode findClosestZipCode(@Param("inputZipCode") int inputZipCode, @Param("genderId") Long genderId);
	
	@Query(value = """
		    SELECT z FROM ZipCode z
		    WHERE z.gender.id = :genderId
		    AND (
		        z.zipFrom = (SELECT z2.zipTo FROM ZipCode z2 WHERE z2.id = :zipCodeId)
		        OR z.zipTo = (SELECT z2.zipFrom FROM ZipCode z2 WHERE z2.id = :zipCodeId)
		    )
		    ORDER BY
		        LEAST(ABS(z.zipFrom - (SELECT z2.zipTo FROM ZipCode z2 WHERE z2.id = :zipCodeId)), 
		              ABS(z.zipTo - (SELECT z2.zipFrom FROM ZipCode z2 WHERE z2.id = :zipCodeId))) ASC
		""")
		List<ZipCode> findNeighboringZipCodesById(@Param("zipCodeId") Long zipCodeId, @Param("genderId") Long genderId);

}

 