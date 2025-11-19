package org.glygen.carbbank.model.mapping;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="mapping_BS_p_d", schema="carbbank")
public class MappingP_D extends Mapping {
	
	@Column
	String rank;
	
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
}

