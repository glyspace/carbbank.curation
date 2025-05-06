package org.glygen.carbbank.dao;

import java.util.List;

import org.glygen.carbbank.model.mapping.Publication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicationRepository extends JpaRepository<Publication, Long> {
	
	List<Publication> findByTitleIgnoreCase (String title);

}
