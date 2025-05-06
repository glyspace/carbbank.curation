package org.glygen.carbbank.dao;

import java.util.List;

import org.glygen.carbbank.model.AN;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ANRepository extends JpaRepository<AN, Long> {
	@Query("SELECT DISTINCT lower(a.value) FROM AN a")
	List<String> findDistinctValue();
	
	long countByValueIgnoreCase (String name);
}
