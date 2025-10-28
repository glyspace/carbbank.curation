package org.glygen.carbbank.dao;

import java.util.Optional;

import org.glygen.carbbank.model.mapping.MappingDomain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MappingDomainRepository extends JpaRepository<MappingDomain, Long> {
	Optional<MappingDomain> findByNameIgnoreCase (String name);
}
