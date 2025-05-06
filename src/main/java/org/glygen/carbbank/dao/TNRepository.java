package org.glygen.carbbank.dao;

import java.util.List;

import org.glygen.carbbank.model.TN;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TNRepository extends JpaRepository<TN, Long> {
	@Query("SELECT DISTINCT lower(a.value) FROM TN a")
	List<String> findDistinctValue();
	
	long countByValue (String name);
}
