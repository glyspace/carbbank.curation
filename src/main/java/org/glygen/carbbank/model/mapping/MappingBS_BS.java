package org.glygen.carbbank.model.mapping;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="mapping_BS_BS", schema="carbbank")
public class MappingBS_BS extends Mapping {
	
	@Column
	String rank;
	
	@Column
	String namespaceName2;
	
	@Column
	String namespaceId2;
	
	public String getNamespaceName2() {
		return namespaceName2;
	}
	public void setNamespaceName2(String namespaceName2) {
		this.namespaceName2 = namespaceName2;
	}
	public String getNamespaceId2() {
		return namespaceId2;
	}
	public void setNamespaceId2(String namespaceId2) {
		this.namespaceId2 = namespaceId2;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
}
