package org.glygen.carbbank.dao;

import java.util.List;

import org.glygen.carbbank.model.mapping.Publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface PublicationRepository extends JpaRepository<Publication, Long>, PublicationRepositoryCustom {
	
	List<Publication> findByTitleIgnoreCase (String title);
	
}
