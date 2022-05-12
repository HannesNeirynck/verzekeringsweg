package com.aginsurance.verzekeringsweg.persistance.repos;

import com.aginsurance.verzekeringsweg.persistance.entities.SubDomein;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface SubdomeinRepository extends JpaRepository<SubDomein,Long> {

    Optional<SubDomein> findByNaam(String naam);


    @Query("DELETE from SubDomein ")
    void deleteAll();
}
