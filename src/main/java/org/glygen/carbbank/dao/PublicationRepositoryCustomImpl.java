package org.glygen.carbbank.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.glygen.carbbank.model.mapping.Publication;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

public class PublicationRepositoryCustomImpl implements PublicationRepositoryCustom {

	@PersistenceContext
    private EntityManager entityManager;
	
	@Override
	public List<Publication> findMatchingPublications(Publication pub) {
		
		String baseQuery = "SELECT DISTINCT p FROM Publication p";
        String countQuery = "SELECT COUNT(DISTINCT p) FROM Publication p";
        
        StringBuilder whereClause = new StringBuilder("");
        Map<String, Object> params = new HashMap<>();
        
        if (pub.getTitle() != null && !pub.getTitle().isEmpty()) {
        	whereClause.append(" WHERE LOWER(p.title) LIKE LOWER(:title)");
            params.put("title", pub.getTitle());
        } else if (pub.getAuthor() != null && !pub.getAuthor().isEmpty()) {
        	if (whereClause.isEmpty()) {
    			whereClause.append(" WHERE LOWER(p.author) like LOWER(:author)");
    		} else {
    			whereClause.append(" AND LOWER(p.author) like LOWER(:author)");
    		}
        	params.put("author", pub.getAuthor());
        }
        
       /* if (pub.getYear() != null && !pub.getYear().isEmpty()) {
        	if (whereClause.isEmpty()) {
    			whereClause.append(" WHERE LOWER(p.year) like LOWER(:year)");
    		} else {
    			whereClause.append(" AND LOWER(p.year) like LOWER(:year)");
    		}
        	
        	params.put("year", pub.getYear());
        }
        
        if (pub.getJournalName() != null && !pub.getJournalName().isEmpty()) {
        	if (whereClause.isEmpty()) {
    			whereClause.append(" WHERE LOWER(p.journalName) like LOWER(:journal)");
    		} else {
    			whereClause.append(" AND LOWER(p.journalName) like LOWER(:journal)");
    		}
        	
        	params.put("journal", pub.getJournalName());
        }
        
        if (pub.getVolume() != null && !pub.getVolume().isEmpty()) {
        	if (whereClause.isEmpty()) {
    			whereClause.append(" WHERE LOWER(p.volume) like LOWER(:volume)");
    		} else {
    			whereClause.append(" AND LOWER(p.volume) like LOWER(:volume)");
    		}
        	
        	params.put("volume", pub.getVolume());
        }
        
        if (pub.getPageRange() != null && !pub.getPageRange().isEmpty()) {
        	if (whereClause.isEmpty()) {
    			whereClause.append(" WHERE LOWER(p.pageRange) like LOWER(:page)");
    		} else {
    			whereClause.append(" AND LOWER(p.pageRange) like LOWER(:page)");
    		}
        	
        	params.put("page", pub.getPageRange());
        }*/
        
		String finalQuery = baseQuery + whereClause.toString();
        String finalCountQuery = countQuery + whereClause.toString();
        

		TypedQuery<Object[]> query = entityManager.createQuery(finalQuery, Object[].class);

        //TypedQuery<Glycan> query = entityManager.createQuery(finalQuery, Glycan.class);
        TypedQuery<Long> count = entityManager.createQuery(finalCountQuery, Long.class);

        params.forEach((k, v) -> {
            query.setParameter(k, v);
            count.setParameter(k, v);
        });

		List<Object[]> rawResults = query.getResultList();
		List<Publication> publications = rawResults.stream()
		    .map(row -> (Publication) row[0])
		    .distinct() 
		    .collect(Collectors.toList());
		
		return publications;
	}

}
