package org.glygen.carbbank.dao;

import java.util.List;

import org.glygen.carbbank.model.mapping.Publication;

public interface PublicationRepositoryCustom {
	
	List<Publication> findMatchingPublications (Publication pub);

}
