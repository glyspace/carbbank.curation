package org.glygen.carbbank.dao;

import java.util.Optional;

import org.glygen.carbbank.model.mapping.MappingP_D;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MappingP_DRepository extends JpaRepository<MappingP_D, Long> {
	Optional<MappingP_D> findByNameIgnoreCase (String name);
}
