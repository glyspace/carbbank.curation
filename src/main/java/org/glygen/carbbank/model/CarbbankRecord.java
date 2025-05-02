package org.glygen.carbbank.model;

import java.util.Collection;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class CarbbankRecord {
	
	Long id;
	
	String structure;
	
	String AU;
	String BA;
	String BA2;
	String CC;
	String CT;
	String DA;
	String FC;
	String SB;
	String SI;
	String TI;
	
	Collection<AG> agList;
	Collection<AM> amList;
	Collection<AN> anList;
	Collection<BS> bsList;
	Collection<DB> dbList;
	Collection<MT> mtList;
	Collection<NC> ncList;
	Collection<NT> ntList;
	Collection<PA> paList;
	Collection<PM> pmList;
	Collection<SC> scList;
	Collection<ST> stList;
	Collection<TN> tnList;
	Collection<VR> vrList;
	
	@Id
    @GeneratedValue
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column
	public String getAU() {
		return AU;
	}
	public void setAU(String aU) {
		AU = aU;
	}
	@Column
	public String getBA() {
		return BA;
	}
	public void setBA(String bA) {
		BA = bA;
	}
	@Column
	public String getBA2() {
		return BA2;
	}
	public void setBA2(String bA2) {
		BA2 = bA2;
	}
	@Column
	public String getCC() {
		return CC;
	}
	public void setCC(String cC) {
		CC = cC;
	}
	@Column
	public String getCT() {
		return CT;
	}
	@Column
	public void setCT(String cT) {
		CT = cT;
	}
	@Column
	public String getDA() {
		return DA;
	}
	public void setDA(String dA) {
		DA = dA;
	}
	@Column
	public String getFC() {
		return FC;
	}
	
	public void setFC(String fC) {
		FC = fC;
	}
	@Column
	public String getSB() {
		return SB;
	}
	public void setSB(String sB) {
		SB = sB;
	}
	
	@Column
	public String getSI() {
		return SI;
	}
	public void setSI(String sI) {
		SI = sI;
	}
	
	@Column(length=2000)
	public String getTI() {
		return TI;
	}
	public void setTI(String tI) {
		TI = tI;
	}

	@Column(columnDefinition="text")
	public String getStructure() {
		return structure;
	}

	public void setStructure(String structure) {
		this.structure = structure;
	}

	@OneToMany(mappedBy = "record", cascade = CascadeType.ALL)
	public Collection<AG> getAgList() {
		return agList;
	}

	public void setAgList(Collection<AG> agList) {
		this.agList = agList;
	}

	@OneToMany(mappedBy = "record", cascade = CascadeType.ALL)
	public Collection<AM> getAmList() {
		return amList;
	}

	public void setAmList(Collection<AM> amList) {
		this.amList = amList;
	}

	@OneToMany(mappedBy = "record", cascade = CascadeType.ALL)
	public Collection<AN> getAnList() {
		return anList;
	}

	public void setAnList(Collection<AN> anList) {
		this.anList = anList;
	}

	@OneToMany(mappedBy = "record", cascade = CascadeType.ALL)
	public Collection<BS> getBsList() {
		return bsList;
	}

	public void setBsList(Collection<BS> bsList) {
		this.bsList = bsList;
	}

	@OneToMany(mappedBy = "record", cascade = CascadeType.ALL)
	public Collection<DB> getDbList() {
		return dbList;
	}

	public void setDbList(Collection<DB> dbList) {
		this.dbList = dbList;
	}

	@OneToMany(mappedBy = "record", cascade = CascadeType.ALL)
	public Collection<MT> getMtList() {
		return mtList;
	}

	public void setMtList(Collection<MT> mtList) {
		this.mtList = mtList;
	}

	@OneToMany(mappedBy = "record", cascade = CascadeType.ALL)
	public Collection<NC> getNcList() {
		return ncList;
	}

	public void setNcList(Collection<NC> ncList) {
		this.ncList = ncList;
	}

	@OneToMany(mappedBy = "record", cascade = CascadeType.ALL)
	public Collection<NT> getNtList() {
		return ntList;
	}

	public void setNtList(Collection<NT> ntList) {
		this.ntList = ntList;
	}

	@OneToMany(mappedBy = "record", cascade = CascadeType.ALL)
	public Collection<PA> getPaList() {
		return paList;
	}

	public void setPaList(Collection<PA> paList) {
		this.paList = paList;
	}

	@OneToMany(mappedBy = "record", cascade = CascadeType.ALL)
	public Collection<PM> getPmList() {
		return pmList;
	}

	public void setPmList(Collection<PM> pmList) {
		this.pmList = pmList;
	}

	@OneToMany(mappedBy = "record", cascade = CascadeType.ALL)
	public Collection<SC> getScList() {
		return scList;
	}

	public void setScList(Collection<SC> scList) {
		this.scList = scList;
	}

	@OneToMany(mappedBy = "record", cascade = CascadeType.ALL)
	public Collection<ST> getStList() {
		return stList;
	}

	public void setStList(Collection<ST> stList) {
		this.stList = stList;
	}

	@OneToMany(mappedBy = "record", cascade = CascadeType.ALL)
	public Collection<TN> getTnList() {
		return tnList;
	}

	public void setTnList(Collection<TN> tnList) {
		this.tnList = tnList;
	}

	@OneToMany(mappedBy = "record", cascade = CascadeType.ALL)
	public Collection<VR> getVrList() {
		return vrList;
	}

	public void setVrList(Collection<VR> vrList) {
		this.vrList = vrList;
	}
}
