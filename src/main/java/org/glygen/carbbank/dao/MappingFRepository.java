package org.glygen.carbbank.dao;

import java.util.Optional;

import org.glygen.carbbank.model.mapping.MappingF;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MappingFRepository extends JpaRepository<MappingF, Long> {
	Optional<MappingF> findByNameIgnoreCase (String name);
}
