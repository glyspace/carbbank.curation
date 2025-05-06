package org.glygen.carbbank.dao;

import java.util.List;

import org.glygen.carbbank.model.AG;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AGRepository extends JpaRepository<AG, Long> {
	@Query("SELECT DISTINCT lower(a.value) FROM AG a")
	List<String> findDistinctValue();
	
	long countByValue (String name);
}
