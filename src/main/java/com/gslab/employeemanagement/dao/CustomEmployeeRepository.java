package com.gslab.employeemanagement.dao;

import java.util.List;
import java.util.Map;

import com.gslab.employeemanagement.model.Employee;

public interface CustomEmployeeRepository {

	public boolean isEmployeePresent(String empId);

	public void updateEmployee(String empid, Map<String, Object> payload);
	
	public List<Employee> getEmployeeByDept(String deptId);
	
	public void removeDeptFromEmployees(String deptId);
	
	public boolean getEmployeeByEmail(String email);
}
