package org.glygen.carbbank.dao;

import java.util.Optional;

import org.glygen.carbbank.model.mapping.MappingOT;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MappingOTRepository extends JpaRepository<MappingOT, Long> {
	
	Optional<MappingOT> findByNameIgnoreCase (String name);
}
