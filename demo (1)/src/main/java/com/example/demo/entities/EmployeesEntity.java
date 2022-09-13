package com.example.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "employees")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EmployeesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "emp_id")
    private Integer employeeId;

    private String name;

    public EmployeesEntity(Integer employeeId, String name) {
        this.employeeId = employeeId;
        this.name = name;
    }
}
