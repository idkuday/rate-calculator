package com.insurance.rate_calculator.controllers;


import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.insurance.rate_calculator.entities.AgeRange;
import com.insurance.rate_calculator.entities.ZipCode;
import com.insurance.rate_calculator.repositories.AgeRangeRepository;
import com.insurance.rate_calculator.repositories.ZipCodeRepository;
import com.insurance.rate_calculator.services.RateService;

@Controller
public class RateController {
    @Autowired
    private RateService rateService;
    @Autowired
	private ZipCodeRepository zipCodeRepository;
    @Autowired	
    private AgeRangeRepository ageRangeRepository;

    @RequestMapping("/")
    public String showRateForm() {
        return "rateCalculator";
    }
    
    @GetMapping("/rateTable")
    public String getRateTable(Model model) {
        List<AgeRange> ageRanges = ageRangeRepository.findAll();
        Comparator<ZipCode> comp = (z1,z2) -> z1.getZipFrom() - z2.getZipFrom();
        Map<Long, Map<Long, Double>> maleRates = rateService.getRatesByGender("Male");
        Map<Long, Map<Long, Double>> femaleRates = rateService.getRatesByGender("Female");
        
        List<ZipCode> zipCodesMale = maleRates.entrySet().stream().map(e->zipCodeRepository.findById(e.getKey()).get()).sorted(comp).collect(Collectors.toList());
        List<ZipCode> zipCodesFemale = femaleRates.entrySet().stream().map(e->zipCodeRepository.findById(e.getKey()).get()).sorted(comp).collect(Collectors.toList());
        
        
        model.addAttribute("zipCodesMale", zipCodesMale);
        model.addAttribute("zipCodesFemale", zipCodesFemale);
        model.addAttribute("ageRanges", ageRanges);
        model.addAttribute("maleRates", maleRates);
        model.addAttribute("femaleRates", femaleRates);
        
        return "rateTable";
    }


    @PostMapping("/calculateRate")
    public String calculateRate(@RequestParam("zipCode") int zipCode,
                                @RequestParam("age") int age,
                                @RequestParam("gender") String gender,
                                Model model) {
        Double rate = rateService.getRates(zipCode, age, gender);
        model.addAttribute("rate", rate);
        model.addAttribute("zipCode", zipCode);
        model.addAttribute("age", age);
        model.addAttribute("gender", gender);
        return "rateCalculator";
    }

    @PostMapping("/updateMaleRates")
    public String updateMaleRates(@RequestParam Map<String, String> allParams, @RequestParam("maleRemovedRows") String removedRowsParam) {
    	updateRates(allParams, removedRowsParam, "Male");
        return "redirect:/rateTable";
    }

    @PostMapping("/updateFemaleRates")
    public String updateFemaleRates(@RequestParam Map<String, String> allParams, @RequestParam("femaleRemovedRows") String removedRowsParam) {
    	updateRates(allParams, removedRowsParam, "Female");
        return "redirect:/rateTable";
    }
    
    public String updateRates(Map<String, String> allParams,String removedRowsParam, String gender) {
    	Map<Long, Map<Long, Double>> updatedRates = new HashMap<>();
    	Map<Integer,Map<Long, Double>> ratesNew = new HashMap<>();
    	List<String> zipsToBeDeleted=null;
    	if(removedRowsParam != null && !removedRowsParam.isEmpty()) {
    		zipsToBeDeleted = Arrays.asList(removedRowsParam.split(","));
    	}
		for(Map.Entry<String, String> entry : allParams.entrySet()) {
			String key = entry.getKey();
	        if (key.startsWith(gender.toLowerCase()+".zipCode.")) {
		        String[] parts = key.split("\\.");
		        Long zipCodeId = Long.valueOf(parts[2]);
		        String fieldType = parts[3];
		
		        if ("ageRange".equals(fieldType)) {
		            Long ageRangeId = Long.valueOf(parts[4]);
		            updatedRates.computeIfAbsent(zipCodeId, k -> new HashMap<>())
		                     .put(ageRangeId, Double.valueOf(entry.getValue()));
		        }
		    }
	        
		}
		Queue<Integer> newZipCodes = new LinkedList<>();
		for(Map.Entry<String, String> entry : allParams.entrySet()) {
			String key = entry.getKey();
			if(key.startsWith("newRow.zipCode")) {   	
		    	newZipCodes.add(Integer.parseInt(entry.getValue()));
		    }
		}
		String current = null;
		Integer currentZip=null;
		for(Map.Entry<String, String> entry : allParams.entrySet()) {
			String key = entry.getKey();

		   if(key.startsWith("newRow.ageRange")) {	   
		    	String[] parts = key.split("\\.");
		    	if(current ==null ||!current.equals(parts[3])) {
		    		current =parts[3];
		    		currentZip=newZipCodes.poll();
		    	}
	    		String fieldType = parts[1];		
		        if ("ageRange".equals(fieldType)) {
		            Long ageRangeId = Long.valueOf(parts[2]);
		            ratesNew.computeIfAbsent(currentZip,k -> new HashMap<>())
		                     .put(ageRangeId, Double.valueOf(entry.getValue()));
		        }	

		    }
		}

		rateService.removeRatesOnZipCode(zipsToBeDeleted, gender);
        rateService.updateRates(updatedRates,gender);
        rateService.addRates(ratesNew, gender);        
        return "redirect:/rateTable";
    }
}

