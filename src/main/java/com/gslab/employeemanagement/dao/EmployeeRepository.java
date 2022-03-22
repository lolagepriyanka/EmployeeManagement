package com.gslab.employeemanagement.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.gslab.employeemanagement.model.Employee;

public interface EmployeeRepository extends MongoRepository<Employee, String>, CustomEmployeeRepository {

}
