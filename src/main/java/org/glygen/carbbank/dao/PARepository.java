package org.glygen.carbbank.dao;

import java.util.List;

import org.glygen.carbbank.model.PA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PARepository extends JpaRepository<PA, Long> {
	@Query("SELECT DISTINCT lower(a.value) FROM PA a")
	List<String> findDistinctValue();
	
	long countByValue (String name);

}
