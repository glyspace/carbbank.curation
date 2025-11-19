package org.glygen.carbbank.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="carbbankglycan", schema="carbbank")
public class CarbbankGlycan {

	@Id
	@GeneratedValue
	Long id;
	
	@Column(columnDefinition="text")
	String glycoCT;
	
	@Column(length=20)
	String glytoucanId;
	
	@JsonIgnore
    @ManyToOne
	CarbbankRecord record;
	
	public CarbbankRecord getRecord() {
		return record;
	}
	public void setRecord(CarbbankRecord record) {
		this.record = record;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGlycoCT() {
		return glycoCT;
	}

	public void setGlycoCT(String glycoCT) {
		this.glycoCT = glycoCT;
	}

	public String getGlytoucanId() {
		return glytoucanId;
	}

	public void setGlytoucanId(String glytoucanId) {
		this.glytoucanId = glytoucanId;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CarbbankGlycan) {
			return (glycoCT != null && glycoCT.equalsIgnoreCase(((CarbbankGlycan) obj).getGlycoCT()))
					|| glytoucanId != null && glytoucanId.equalsIgnoreCase(((CarbbankGlycan) obj).getGlytoucanId());
		}
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		if (glytoucanId != null) return glytoucanId.hashCode();
		if (glycoCT != null) return glycoCT.hashCode();
		return super.hashCode();
	}
	
}
