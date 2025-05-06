package org.glygen.carbbank.dao;

import org.glygen.carbbank.model.CarbbankRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarbbankRepository extends JpaRepository<CarbbankRecord, Long> {
}
