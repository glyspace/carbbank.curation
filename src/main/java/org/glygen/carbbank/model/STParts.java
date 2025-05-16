package org.glygen.carbbank.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="ST_parts")
public class STParts {
	
	@Id
	@GeneratedValue
	Long id;
	@Column
	String value;
	@ManyToOne(targetEntity = ST.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "st_id", foreignKey = @ForeignKey(name = "FK_VERIFY_ST"))
	ST st;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public ST getSt() {
		return st;
	}
	public void setSt(ST st) {
		this.st = st;
	}
	
}