package com.rbs.casestudy.transferapi.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;

import com.rbs.casestudy.transferapi.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{
 
    /**
     * Returns a {@link Page} of entities meeting the paging restriction provided in the {@code Pageable} object.
     *
     * @param pageable
     * @return a page of entities
     */
    @EntityGraph(value="Transaction", type = EntityGraphType.LOAD)
    Page<Transaction> findAll(Pageable pageable);
}
