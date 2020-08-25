package com.wkowalczyk.restapi.repo;

import com.wkowalczyk.restapi.model.Elektrownia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ElektrownieRepository extends JpaRepository<Elektrownia, Long> {
}
