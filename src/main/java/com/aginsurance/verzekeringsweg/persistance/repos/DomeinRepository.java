package com.aginsurance.verzekeringsweg.persistance.repos;

import com.aginsurance.verzekeringsweg.persistance.entities.HoofdDomein;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface DomeinRepository extends JpaRepository<HoofdDomein, Long> {

    Optional<HoofdDomein> findByNaam(String naam);
}
