package com.racs.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.racs.core.entities.RoleUser;

public interface RoleAppRepository extends JpaRepository<RoleUser, Long> {

}
