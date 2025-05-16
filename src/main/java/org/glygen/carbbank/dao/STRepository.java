package org.glygen.carbbank.dao;

import java.util.List;

import org.glygen.carbbank.model.ST;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface STRepository extends JpaRepository<ST, Long> {
	@Query("SELECT DISTINCT lower(a.value) FROM ST a")
	List<String> findDistinctValue();
	
	long countByValueIgnoreCase (String name);
}
