package com.gslab.employeemanagement.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("employee")
public class Employee {

	@Id
	private String empId;
	private String empName;
	private String email;
	private String manager;

	@DBRef
	private List<Department> departments;

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

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

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public List<Department> getDepartments() {
		return departments;
	}

	@Override
	public String toString() {
		return "Employee [empId=" + empId + ", empName=" + empName + ", email=" + email + ", manager=" + manager
				+ ", departments=" + departments + "]";
	}

	public Employee() {
		super();
	}

	public Employee(String empId, String empName, String email, String manager, List<Department> departments) {
		super();
		this.empId = empId;
		this.empName = empName;
		this.email = email;
		this.manager = manager;
		this.departments = departments;
	}

	public void setDepartments(List<Department> departments) {
		this.departments = departments;
	}

}
