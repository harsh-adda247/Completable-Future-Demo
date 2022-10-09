package com.example.demo.repository;

import com.example.demo.entities.EmployeesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeesRepository extends JpaRepository<EmployeesEntity, Integer> {

    @Query(value = "select * from employees where id between :startRange and :endRange", nativeQuery = true)
    List<EmployeesEntity> findByIdRange(Integer startRange, Integer endRange);
}
