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
	@Column
	String doiId;
	@Column
	Boolean checked;
	@Column
	String matchCount;
	@Column(length=4000)
	String matchDetails;
	
	public String getMatchDetails() {
		return matchDetails;
	}
	
	public void setMatchDetails(String matchDetails) {
		this.matchDetails = matchDetails;
	}
	
	public String getMatchCount() {
		return matchCount;
	}
	
	public void setMatchCount(String matchCount) {
		this.matchCount = matchCount;
	}
	
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
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Publication) {
			if (title != null && title.equalsIgnoreCase(((Publication) obj).getTitle())) {
				if (author != null) {
					if (author.equalsIgnoreCase(((Publication) obj).getAuthor())) {
						if (journal != null) {
							if (journal.equalsIgnoreCase(((Publication) obj).getJournal())) {
								return true;
							}
						}
						else {
							return true;
						}
					}
				} else {
					if (journal != null) {
						if (journal.equalsIgnoreCase(((Publication) obj).getJournal())) {
							return true;
						}
					}
					else {
						return true;
					}
				}
			}
		}
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		String pub = title+author+journal;
		return pub.hashCode();
	}
	public String getDoiId() {
		return doiId;
	}
	public void setDoiId(String doiId) {
		this.doiId = doiId;
	}
	public Boolean getChecked() {
		return checked;
	}
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	
	@Override
	public String toString() {
		return "Title: " + title + " Authors: " + author + " Journal: " + journal;
	}

}
