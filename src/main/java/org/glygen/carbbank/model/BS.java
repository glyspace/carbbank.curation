package org.glygen.carbbank.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class BS {
	Long id;
	String bs;
	String c;
	String cell_line;
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
	String p_d;
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
	@GeneratedValue
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
	public String getCell_line() {
		return cell_line;
	}
	public void setCell_line(String cell_line) {
		this.cell_line = cell_line;
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
	@Column
	public String getP_d() {
		return p_d;
	}
	public void setP_d(String p_d) {
		this.p_d = p_d;
	}
	@Column
	public String getStar() {
		return star;
	}
	public void setStar(String star) {
		this.star = star;
	}

}
