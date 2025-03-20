package com.insurance.rate_calculator.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ZipCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer zipFrom;

    private Integer zipTo;
    
    @ManyToOne
    @JoinColumn(name = "gender_id", nullable = false)
    private Gender gender; 

    public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	// Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getZipFrom() {
        return zipFrom;
    }

    public void setZipFrom(int zipFrom) {
        this.zipFrom = zipFrom;
    }

    public int getZipTo() {
        return zipTo;
    }

    public void setZipTo(int zipTo) {
        this.zipTo = zipTo;
    }
}