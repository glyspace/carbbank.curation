package org.glygen.carbbank.dao;

import java.util.Optional;

import org.glygen.carbbank.model.mapping.MappingCellLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MappingCelllineRepository extends JpaRepository<MappingCellLine, Long> {
	Optional<MappingCellLine> findByNameIgnoreCase (String name);
}
