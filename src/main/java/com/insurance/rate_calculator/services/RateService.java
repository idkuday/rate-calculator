package com.insurance.rate_calculator.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.insurance.rate_calculator.entities.AgeRange;
import com.insurance.rate_calculator.entities.Gender;
import com.insurance.rate_calculator.entities.InsuranceRate;
import com.insurance.rate_calculator.entities.ZipCode;
import com.insurance.rate_calculator.repositories.AgeRangeRepository;
import com.insurance.rate_calculator.repositories.GenderRepository;
import com.insurance.rate_calculator.repositories.InsuranceRateRepository;
import com.insurance.rate_calculator.repositories.ZipCodeRepository;

import jakarta.transaction.Transactional;

@Service
public class RateService {
    @Autowired
    private ZipCodeRepository zipCodeRepository;

    @Autowired
    private AgeRangeRepository ageRangeRepository;

    @Autowired
    private GenderRepository genderRepository;

    @Autowired
    private InsuranceRateRepository insuranceRateRepository;
    
    private static List<Gender> gendersCache;
    
    public Double getRates(int zipCode, int age, String gender) {
    	Gender gen = getGender(gender);
        ZipCode zip = zipCodeRepository.findByZipRange(zipCode, gen.getId());
        AgeRange ageRange = ageRangeRepository.findByAge(age);
        

        if (zip == null || ageRange == null || gen == null) {
            return -2d;  // Return -2 for invalid data
        }

        InsuranceRate insuranceRate = insuranceRateRepository.findByZipCodeAndAgeRangeAndGender(zip, ageRange, gen);
        return insuranceRate != null ? insuranceRate.getRate() : -1d;  // Return -1 if no rate found
    }
    
    private Gender getGender(String gender) {
		if(gendersCache == null) {
			gendersCache = genderRepository.findAll();
			gendersCache.sort((a,b)->a.getGender().compareTo(b.getGender()));
		}
		return gender.equals("Male") ? gendersCache.get(1) :gendersCache.get(0);
	}

	public Map<Long, Map<Long, Double>> getRatesByGender(String gender) {
        Gender gen = getGender(gender);
        List<InsuranceRate> insuranceRates = insuranceRateRepository.findByGender(gen);
        
        Map<Long, Map<Long, Double>> rates = new HashMap<>();
        
        for (InsuranceRate rate : insuranceRates) {
            rates.computeIfAbsent(rate.getZipCode().getId(), k -> new HashMap<>())
                 .put(rate.getAgeRange().getId(), rate.getRate());
        }
        return rates;
    } 

    // Save updated rates
    public void updateRates(Map<Long, Map<Long, Double>> updatedRates, String gender) {
    	Gender gen = getGender(gender);
        for (Map.Entry<Long, Map<Long, Double>> entry : updatedRates.entrySet()) {
            ZipCode zipCode = zipCodeRepository.findById(entry.getKey()).orElseThrow();
            
            for (Map.Entry<Long, Double> subEntry : entry.getValue().entrySet()) {
                AgeRange ageRange = ageRangeRepository.findById(subEntry.getKey()).orElseThrow();
                InsuranceRate rate = insuranceRateRepository
                        .findByZipCodeAndAgeRangeAndGender(zipCode, ageRange, gen);
                if(rate == null) {
                	rate = new InsuranceRate();
                }
                rate.setRate(subEntry.getValue());
                rate.setZipCode(zipCode);
                rate.setAgeRange(ageRange);
                rate.setGender(gen);
                
                insuranceRateRepository.save(rate);
            }
        }

    }
    public void addRates(Map<Integer, Map<Long, Double>> newRates, String gender) {
    	Gender gen = getGender(gender);
        for (Map.Entry<Integer, Map<Long, Double>> entry : newRates.entrySet()) {
        	if(zipCodeRepository.findByZipRange(entry.getKey(), gen.getId())!=null) {
        		continue;
        	}
            ZipCode zipCode = zipCodeRepository.findByZipRange(entry.getKey(), gen.getId());
            Integer newZip = entry.getKey();
            if(zipCode==null) {
            	ZipCode zipCodeNearest = zipCodeRepository.findClosestZipCode(newZip, gen.getId());
            	ZipCode newZipCode = new ZipCode();
            	if(zipCodeNearest.getZipFrom()>newZip) {
            		newZipCode.setZipFrom(newZip);
            		newZipCode.setZipTo(zipCodeNearest.getZipFrom());
            	}
            	else {
            		newZipCode.setZipFrom(zipCodeNearest.getZipFrom());
            		newZipCode.setZipTo(newZip);
            	}
            	newZipCode.setGender(gen);
            	zipCode = zipCodeRepository.save(newZipCode);
            }
            else {
            	ZipCode newZipCode = new ZipCode();
        		newZipCode.setZipFrom(newZip);
        		newZipCode.setZipTo(zipCode.getZipTo());
        		newZipCode.setGender(gen);
        		zipCode.setZipTo(newZip);
        		zipCodeRepository.save(zipCode);
        		zipCode = zipCodeRepository.save(newZipCode);
            }
            
            for (Map.Entry<Long, Double> subEntry : entry.getValue().entrySet()) {
                AgeRange ageRange = ageRangeRepository.findById(subEntry.getKey()).orElseThrow();
            	InsuranceRate rate = new InsuranceRate();
                rate.setRate(subEntry.getValue());
                rate.setZipCode(zipCode);
                rate.setAgeRange(ageRange);
                rate.setGender(gen);                
                insuranceRateRepository.save(rate);
            }
        }
    }
    @Transactional
    public void removeRatesOnZipCode(List<String> removedZips, String gender) {
    	Gender gen = getGender(gender);
    	if(removedZips==null) {
    		return;
    	}
    	removedZips.forEach(
    			zipId->{
    		    	if(removedZips==null || removedZips.size()==0) {
    		    		return;
    		    	}
					List<ZipCode> zipCodeNeighbors = zipCodeRepository.findNeighboringZipCodesById(Long.parseLong(zipId), gen.getId());
					if(zipCodeNeighbors.size()==2) {
						ZipCode z1 = zipCodeNeighbors.get(0);
						ZipCode z2 = zipCodeNeighbors.get(1);
						if(z1.getZipFrom()<z2.getZipFrom()) {
							z1.setZipTo(z2.getZipFrom());							
						}
						else {
							z1.setZipTo(z2.getZipFrom());
						}
					}
    				insuranceRateRepository.deleteByZipCodeIdAndGenderId(Long.parseLong(zipId), gen.getId());
    			}
    			);
    }
    
}
