package org.glygen.carbbank.dao;

import java.util.Optional;

import org.glygen.carbbank.model.mapping.MappingK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MappingKRepository extends JpaRepository<MappingK, Long> {
	Optional<MappingK> findByNameIgnoreCase (String name);
}
