package com.example.demo.repository;

import com.example.demo.entities.EmployeesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeesRepository extends JpaRepository<EmployeesEntity, Integer> {
}
