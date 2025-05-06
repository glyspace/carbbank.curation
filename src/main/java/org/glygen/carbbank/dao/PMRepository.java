package org.glygen.carbbank.dao;

import java.util.List;

import org.glygen.carbbank.model.PM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PMRepository extends JpaRepository<PM, Long> {
	@Query("SELECT DISTINCT lower(a.value) FROM PM a")
	List<String> findDistinctValue();
	
	long countByValueIgnoreCase (String name);
}
