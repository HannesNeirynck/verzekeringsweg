package com.aginsurance.verzekeringsweg.persistance.repos;

import com.aginsurance.verzekeringsweg.persistance.entities.Vraag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface VraagRepository extends JpaRepository<Vraag, Long> {





}
