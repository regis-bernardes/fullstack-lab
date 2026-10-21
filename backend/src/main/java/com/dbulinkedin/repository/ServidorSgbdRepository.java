package com.dbulinkedin.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dbulinkedin.entity.ServidorSgbd;

@Repository
public interface ServidorSgbdRepository extends JpaRepository<ServidorSgbd, Integer> {
    List<ServidorSgbd> findByAtivoTrue();
    
    Page<ServidorSgbd> findAll(Pageable pageable);
}

