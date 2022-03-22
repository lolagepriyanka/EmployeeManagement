package com.gslab.employeemanagement.model;

import java.util.List;

public class EmployeeRequest {

	private String empName;
	private String email;
	private String manager;

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public EmployeeRequest(String empName, String email, String manager, List<String> departments) {
		super();
		this.empName = empName;
		this.email = email;
		this.manager = manager;
		this.departments = departments;
	}

	public EmployeeRequest() {
		super();
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public List<String> getDepartments() {
		return departments;
	}

	public void setDepartments(List<String> departments) {
		this.departments = departments;
	}

	private List<String> departments;
}
