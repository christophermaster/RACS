package com.racs.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.racs.core.entities.Roles;



public interface RoleRepository extends JpaRepository<Roles, Long> {

}
