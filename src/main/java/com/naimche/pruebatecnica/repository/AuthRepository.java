package com.naimche.pruebatecnica.repository;

import com.naimche.pruebatecnica.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

}
