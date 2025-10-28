package org.glygen.carbbank.dao;

import java.util.Optional;

import org.glygen.carbbank.model.mapping.MappingBS_C;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MappingCRepository extends JpaRepository<MappingBS_C, Long> {
	Optional<MappingBS_C> findByNameIgnoreCase (String name);
}
