package com.lab.department_service;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    public Optional<Department> findByName(String name);

}