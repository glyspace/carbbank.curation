package org.glygen.carbbank.dao;

import java.util.Optional;

import org.glygen.carbbank.model.mapping.MappingO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MappingORepository extends JpaRepository<MappingO, Long> {
	Optional<MappingO> findByNameIgnoreCase (String name);
}
