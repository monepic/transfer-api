package com.rbs.casestudy.transferapi.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rbs.casestudy.transferapi.model.Account;

/**
 * @author ed
 */
public interface AccountRepository extends JpaRepository<Account, String> {}
