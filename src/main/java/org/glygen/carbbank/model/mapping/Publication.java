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
	String journalName;
	@Column
	String year;
	@Column 
	String volume;
	@Column
	String pageRange;
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
	public String getJournalName() {
		return journalName;
	}
	public void setJournalName(String journal) {
		this.journalName = journal;
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
					if (authorMatch(((Publication) obj).getAuthor())) {
						return journalMatch (((Publication) obj));
					}
				} else {
					return journalMatch (((Publication) obj));
				}
			}
		}
		return super.equals(obj);
	}
	
	public boolean journalMatch(Publication publication) {
		if (this.journalName != null && this.journalName.equalsIgnoreCase(publication.getJournalName())) {
			// check if at least one of year or volume or page range matches
			if (this.year != null && this.year.equalsIgnoreCase(publication.getYear())) {
				return true;
			}
			if (this.volume != null && this.volume.equalsIgnoreCase(publication.getVolume())) {
				return true;
			}
			if (this.pageRange != null && this.pageRange.equalsIgnoreCase(publication.getPageRange())) {
				return true;
			}
		} else {
			// check if others match
			if (this.year != null && this.year.equalsIgnoreCase(publication.getYear())) {
				if (this.volume != null && this.volume.equalsIgnoreCase(publication.getVolume())) {
					return true;
				}
				if (this.pageRange != null && this.pageRange.equalsIgnoreCase(publication.getPageRange())) {
					return true;
				}
			} else {
				if (this.volume != null && this.volume.equalsIgnoreCase(publication.getVolume())) {
					if (this.pageRange != null && this.pageRange.equalsIgnoreCase(publication.getPageRange())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean authorMatch (String author2) {
		if (this.author != null && this.author.equalsIgnoreCase(author2))
			return true;
		if (this.author != null && author2 != null) {
			// use only last names to match
			String[] authorList = this.author.split(";");
			String lastNames1 = "";
			for (String a: authorList) {
				if (a.indexOf(" ") != -1) {
					a = a.trim().substring(0, a.lastIndexOf(" "));
				}
				lastNames1 += a.trim() + ";";
			}
			authorList = author2.split(";");
			String lastNames2 = "";
			for (String a: authorList) {
				if (a.indexOf(" ") != -1) {
					a = a.trim().substring(0, a.lastIndexOf(" "));
				}
				lastNames2 += a.trim() + ";";
			}
			return lastNames1.equalsIgnoreCase(lastNames2);
		}
		if (this.author == null && author2 == null) return true;
		return false;
	}
	
	@Override
	public int hashCode() {
		String pub = title+author+journalName+year+pageRange;
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
		return "Title: " + title + " Authors: " + author + " Journal: " + journalName + " Year: " + year + " Volume: " + volume + " Page Range: " + pageRange;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getPageRange() {
		return pageRange;
	}

	public void setPageRange(String pageRange) {
		this.pageRange = pageRange;
	}

}
