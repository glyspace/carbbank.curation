package org.glygen.carbbank.dao;

import java.util.List;

import org.glygen.carbbank.model.BS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BSRepository extends JpaRepository<BS, Long> {
	
	@Query("SELECT DISTINCT lower(b.bs) FROM BS b")
	List<String> findDistinctBS();
	@Query("SELECT DISTINCT lower(b.c) FROM BS b")
	List<String> findDistinctC();
	@Query("SELECT DISTINCT lower(b.cellline) FROM BS b")
	List<String> findDistinctCellline();
	@Query("SELECT DISTINCT lower(b.cn) FROM BS b")
	List<String> findDistinctCN();
	@Query("SELECT DISTINCT lower(b.disease) FROM BS b")
	List<String> findDistinctDisease();
	@Query("SELECT DISTINCT lower(b.domain) FROM BS b")
	List<String> findDistinctDomain();
	@Query("SELECT DISTINCT lower(b.f) FROM BS b")
	List<String> findDistinctF();
	@Query("SELECT DISTINCT lower(b.gs) FROM BS b")
	List<String> findDistinctGS();
	@Query("SELECT DISTINCT lower(b.gt) FROM BS b")
	List<String> findDistinctGT();
	@Query("SELECT DISTINCT lower(b.k) FROM BS b")
	List<String> findDistinctK();
	@Query("SELECT DISTINCT lower(b.ls) FROM BS b")
	List<String> findDistinctLS();
	@Query("SELECT DISTINCT lower(b.o) FROM BS b")
	List<String> findDistinctO();
	@Query("SELECT DISTINCT lower(b.ot) FROM BS b")
	List<String> findDistinctOT();
	@Query("SELECT DISTINCT lower(b.pd) FROM BS b")
	List<String> findDistinctP_D();
	
	long countByBsIgnoreCase (String name);
	long countByCIgnoreCase (String name);
	long countByCelllineIgnoreCase (String name);
	long countByCnIgnoreCase (String name);
	long countByDiseaseIgnoreCase (String name);
	long countByDomainIgnoreCase (String name);
	long countByFIgnoreCase (String name);
	long countByGsIgnoreCase (String name);
	long countByGtIgnoreCase (String name);
	long countByKIgnoreCase (String name);
	long countByLsIgnoreCase (String name);
	long countByOIgnoreCase (String name);
	long countByOtIgnoreCase (String name);
	long countByPdIgnoreCase (String name);
	
	List<BS> findByGsIgnoreCase (String name);
	List<BS> findByCelllineIgnoreCase (String name);
	List<BS> findByCnIgnoreCase (String name);
	List<BS> findByDiseaseIgnoreCase (String name);
	List<BS> findByDomainIgnoreCase (String name);
	List<BS> findByFIgnoreCase (String name);
	List<BS> findByGtIgnoreCase (String name);
	List<BS> findByKIgnoreCase (String name);
	List<BS> findByLsIgnoreCase (String name);
	List<BS> findByOIgnoreCase (String name);
	List<BS> findByOtIgnoreCase (String name);
	List<BS> findByPdIgnoreCase (String name);

}
