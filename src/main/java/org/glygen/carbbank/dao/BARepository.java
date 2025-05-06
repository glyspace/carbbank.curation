package org.glygen.carbbank.dao;

import java.util.List;

import org.glygen.carbbank.model.BA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BARepository extends JpaRepository<BA, Long> {
	@Query("SELECT DISTINCT lower(a.value) FROM BA a")
	List<String> findDistinctValue();
	
	long countByValue (String name);
}
