package org.glygen.carbbank.dao;

import java.util.List;

import org.glygen.carbbank.model.MT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MTRepository extends JpaRepository<MT, Long> {
	@Query("SELECT DISTINCT lower(a.value) FROM MT a")
	List<String> findDistinctValue();
	
	long countByValue (String name);
}
