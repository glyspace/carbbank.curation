package org.glygen.carbbank.dao;

import java.util.List;

import org.glygen.carbbank.model.AM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AMRepository extends JpaRepository<AM, Long> {
	@Query("SELECT DISTINCT lower(a.value) FROM AM a")
	List<String> findDistinctValue();
	
	long countByValue (String name);

}
