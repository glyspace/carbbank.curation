package org.glygen.carbbank.dao2;

import java.util.Optional;

import org.glygen.carbbank.glycomedb.RemoteStructure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RemoteStructureRepository extends JpaRepository<RemoteStructure, Integer> {
	Optional<RemoteStructure> findByResourceIdAndResource(String resourceId, String resource);
	

	@Query("SELECT r FROM RemoteStructure r JOIN FETCH r.structures WHERE r.resourceId = :id and r.resource='carbbank'")
	Optional<RemoteStructure> findByResourceIdWithStructures(@Param("id") String id);

}
