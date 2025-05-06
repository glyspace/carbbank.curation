package org.glygen.carbbank.dao;

import java.util.List;

import org.glygen.carbbank.model.DB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DBRepository extends JpaRepository<DB, Long> {
	@Query("SELECT DISTINCT lower(a.value) FROM DB a")
	List<String> findDistinctValue();
	
	long countByValue (String name);

}
