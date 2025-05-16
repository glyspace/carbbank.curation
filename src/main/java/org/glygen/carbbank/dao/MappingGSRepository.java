package org.glygen.carbbank.dao;

import java.util.List;

import org.glygen.carbbank.model.mapping.MappingCN;
import org.glygen.carbbank.model.mapping.MappingGS;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MappingGSRepository extends JpaRepository<MappingGS, Long>{
	List<MappingGS> findByNameEqualsIgnoreCase (String name);
}
