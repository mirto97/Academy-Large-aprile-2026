package com.academy.esercizio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.academy.esercizio.entity.Role;


@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    
}
