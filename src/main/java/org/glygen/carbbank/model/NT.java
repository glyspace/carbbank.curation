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
public class NT {
	Long id;
	String value;
	CarbbankRecord record;
	
	@Column(length=2000)
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
	//@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="nt_seq")
    //@SequenceGenerator(name="nt_seq", sequenceName="carbbank.nt_seq", initialValue=1)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

}
