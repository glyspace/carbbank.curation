package org.glygen.carbbank.model.mapping;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Publication {
	
	@Id
	@GeneratedValue
	Long id;
	@Column(length=2000)
	String title;
	@Column
	String author;
	@Column
	String journal;
	@Column
	String pmid;
	@Column
	String carbbankPmid;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getJournal() {
		return journal;
	}
	public void setJournal(String journal) {
		this.journal = journal;
	}
	public String getPmid() {
		return pmid;
	}
	public void setPmid(String pmid) {
		this.pmid = pmid;
	}
	public String getCarbbankPmid() {
		return carbbankPmid;
	}
	public void setCarbbankPmid(String carbbankPmid) {
		this.carbbankPmid = carbbankPmid;
	}

}
