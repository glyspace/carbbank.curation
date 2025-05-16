package org.glygen.carbbank.dao;

import java.util.List;

import org.glygen.carbbank.model.mapping.MappingCN;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MappingCNRepository extends JpaRepository<MappingCN, Long> {
	
	List<MappingCN> findByNameEqualsIgnoreCase (String name);

}
