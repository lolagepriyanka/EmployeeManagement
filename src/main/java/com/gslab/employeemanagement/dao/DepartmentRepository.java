package com.gslab.employeemanagement.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.gslab.employeemanagement.model.Department;

public interface DepartmentRepository extends MongoRepository<Department, String>, CustomDepartmentRepository {

}
