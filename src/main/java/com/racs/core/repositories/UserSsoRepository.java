package com.racs.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.racs.core.entities.User;

@Repository
public interface UserSsoRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
}
