package org.glygen.carbbank.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

@Entity
public class AN {
	Long id;
	String value;
	CarbbankRecord record;
	@Column
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	@JsonIgnore
    @ManyToOne
	public CarbbankRecord getRecord() {
		return record;
	}
	public void setRecord(CarbbankRecord record) {
		this.record = record;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="carbbank_seq")
	//@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="an_seq")
    //@SequenceGenerator(name="an_seq", sequenceName="carbbank.an_seq", initialValue=1)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
}
