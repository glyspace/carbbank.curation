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
public class BS {
	Long id;
	String bs;
	String c;
	String cellline;
	String cn;
	String disease;
	String domain;
	String f;
	String gs;
	String gt;
	String k;
	String ls;
	String nt;
	String o;
	String ot;
	String pd;
	String star;
	
	CarbbankRecord record;
	
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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column
	public String getBs() {
		return bs;
	}
	public void setBs(String bs) {
		this.bs = bs;
	}
	@Column
	public String getC() {
		return c;
	}
	public void setC(String c) {
		this.c = c;
	}
	@Column
	public String getCellline() {
		return cellline;
	}
	public void setCellline(String cell_line) {
		this.cellline = cell_line;
	}
	@Column
	public String getCn() {
		return cn;
	}
	public void setCn(String cn) {
		this.cn = cn;
	}
	@Column
	public String getDisease() {
		return disease;
	}
	public void setDisease(String disease) {
		this.disease = disease;
	}
	@Column
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	@Column
	public String getF() {
		return f;
	}
	public void setF(String f) {
		this.f = f;
	}
	@Column
	public String getGs() {
		return gs;
	}
	public void setGs(String gs) {
		this.gs = gs;
	}
	@Column
	public String getGt() {
		return gt;
	}
	public void setGt(String gt) {
		this.gt = gt;
	}
	@Column
	public String getK() {
		return k;
	}
	public void setK(String k) {
		this.k = k;
	}
	@Column
	public String getLs() {
		return ls;
	}
	public void setLs(String ls) {
		this.ls = ls;
	}
	@Column
	public String getNt() {
		return nt;
	}
	public void setNt(String nt) {
		this.nt = nt;
	}
	@Column
	public String getO() {
		return o;
	}
	public void setO(String o) {
		this.o = o;
	}
	@Column
	public String getOt() {
		return ot;
	}
	public void setOt(String ot) {
		this.ot = ot;
	}
	@Column(name="p_d")
	public String getPd() {
		return pd;
	}
	public void setPd(String p_d) {
		this.pd = p_d;
	}
	@Column
	public String getStar() {
		return star;
	}
	public void setStar(String star) {
		this.star = star;
	}

}
