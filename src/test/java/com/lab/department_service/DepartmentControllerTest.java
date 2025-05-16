package com.lab.department_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class DepartmentControllerTest {

    private MockMvc mockMvc;
    private DepartmentRepository departmentRepository;
    private DepartmentModelAssembler departmentModelAssembler;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        departmentRepository = Mockito.mock(DepartmentRepository.class);
        departmentModelAssembler = Mockito.mock(DepartmentModelAssembler.class);
        DepartmentController controller = new DepartmentController(departmentRepository, departmentModelAssembler);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }
    @Test
    void testGetDepartmentById() throws Exception {
        Department department = new Department("HR", "Human Resources");
        department.setId(1L);
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(departmentModelAssembler.toModel(department)).thenReturn(null); // Mock assembler behavior

        mockMvc.perform(get("/departments/1"))
                .andExpect(status().isOk());

        verify(departmentRepository, times(1)).findById(1L);
    }
    @Test
    void testSaveDepartment() throws Exception {
        Department department = new Department("IT", "Information Technology");
        department.setId(2L);

        // Mock repository behavior
        when(departmentRepository.save(any(Department.class))).thenReturn(department);

        // Mock assembler behavior
        EntityModel<Department> entityModel = EntityModel.of(department,
                linkTo(DepartmentController.class).slash(department.getId()).withSelfRel());
        when(departmentModelAssembler.toModel(department)).thenReturn(entityModel);

        // Perform POST request
        mockMvc.perform(post("/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(department)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("IT"));

        verify(departmentRepository, times(1)).save(any(Department.class));
    }
}