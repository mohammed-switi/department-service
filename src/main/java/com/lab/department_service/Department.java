package com.lab.department_service;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Department {
    private @Id @GeneratedValue Long id;
    private String name;
    private String location;

    public Department() {}

    public Department(String name, String location) {
        this.name = name;
        this.location = location;
    }

}