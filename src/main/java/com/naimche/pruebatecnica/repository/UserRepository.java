package com.naimche.pruebatecnica.repository;

import com.naimche.pruebatecnica.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {


}
