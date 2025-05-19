package com.lab.department_service;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
class DepartmentModelAssembler implements RepresentationModelAssembler<Department, EntityModel<Department>> {


  @Override
  @NonNull
  public EntityModel<Department> toModel(@NonNull Department department) {

    return EntityModel.of(department, //
        linkTo(methodOn(DepartmentController.class).one(department.getId())).withSelfRel(),
        linkTo(methodOn(DepartmentController.class).all()).withRel("departments"));
  }
}
