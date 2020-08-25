package com.wkowalczyk.restapi.repo;

import com.wkowalczyk.restapi.model.Zdarzenie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ZdarzeniaRepository extends JpaRepository<Zdarzenie, Long> {
    List<Zdarzenie> findByElektrowniaId(Long elektrowniaId);
}
