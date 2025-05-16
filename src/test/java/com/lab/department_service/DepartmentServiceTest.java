package com.lab.department_service;

import com.lab.department_service.Department;
import com.lab.department_service.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class DepartmentRepositoryTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    void testSaveAndFindById() {
        Department department = new Department("HR", "Human Resources");
        Department savedDepartment = departmentRepository.save(department);

        Optional<Department> result = departmentRepository.findById(savedDepartment.getId());

        assertTrue(result.isPresent());
        assertEquals("HR", result.get().getName());
    }

    @Test
    void testFindAll() {
        Department department1 = new Department("HR", "Human Resources");
        Department department2 = new Department("IT", "Information Technology");
        departmentRepository.save(department1);
        departmentRepository.save(department2);

        Iterable<Department> departments = departmentRepository.findAll();

        assertNotNull(departments);
        assertTrue(departments.iterator().hasNext());
    }
}