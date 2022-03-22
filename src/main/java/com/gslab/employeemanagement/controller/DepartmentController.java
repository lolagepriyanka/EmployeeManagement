package com.gslab.employeemanagement.controller;

import static com.gslab.employeemanagement.constant.ErrorMessageConstant.DEPT_NOT_FOUND;
import static com.gslab.employeemanagement.constant.ErrorMessageConstant.WRONG_PAYLOAD;
import static com.gslab.employeemanagement.constant.ErrorMessageConstant.EMPLOYEE_NOT_FOUND;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gslab.employeemanagement.dao.DepartmentRepository;
import com.gslab.employeemanagement.exception.DepartmentExistException;
import com.gslab.employeemanagement.exception.DepartmentNotFound;
import com.gslab.employeemanagement.exception.EmployeeNotFoundException;
import com.gslab.employeemanagement.exception.InvalidInputException;
import com.gslab.employeemanagement.model.Department;
import com.gslab.employeemanagement.model.Employee;
import com.gslab.employeemanagement.model.Response;
import com.gslab.employeemanagement.model.Status;
import com.gslab.employeemanagement.service.DepartmentService;

@RestController
@RequestMapping("/org/department")
public class DepartmentController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentController.class);
	@Autowired
	DepartmentRepository repo;

	@Autowired
	DepartmentService deptService;

	@PostMapping
	public ResponseEntity<Status> createDepartment(@RequestBody Department dept) {
		try {
			deptService.createDept(dept);
			LOGGER.info("Employee with " + dept.getDeptId() + " is created successfully.");
			return new ResponseEntity<Status>(HttpStatus.CREATED);
		} catch (InvalidInputException | DepartmentExistException e) {
			LOGGER.error(WRONG_PAYLOAD + e);
			return new ResponseEntity<Status>(new Status(e.getMessage()), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			LOGGER.error("Error occured in DeptController.createEmployee API: " + e);
			return new ResponseEntity<Status>(new Status(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping
	public ResponseEntity<Response> getDepartments() {
		List<Department> depts = new ArrayList<>();
		try {
			depts = deptService.getAllDepts();
		} catch (DepartmentNotFound e) {
			LOGGER.error(DEPT_NOT_FOUND + e);
			return new ResponseEntity<Response>(new Response(e.getMessage(), depts), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			LOGGER.error("Error occured in DepartmentController.getAllDepts API: " + e);
			return new ResponseEntity<Response>(new Response(e.getMessage(), depts), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Response>(new Response("success", depts), HttpStatus.OK);
	}

	@PatchMapping("/{deptId}")
	public ResponseEntity<Status> updateDepartment(@RequestBody Map<String, String> deptPayload,
			@PathVariable("deptId") String deptId) {
		try {
			deptService.updateDepartment(deptId, deptPayload);
			LOGGER.info("Department [" + deptId + "] updated successfully");
			return new ResponseEntity<Status>(HttpStatus.OK);
		} catch (InvalidInputException e) {
			LOGGER.error(WRONG_PAYLOAD + e);
			return new ResponseEntity<Status>(new Status(e.getMessage()), HttpStatus.BAD_REQUEST);
		} catch (DepartmentNotFound e) {
			LOGGER.error(DEPT_NOT_FOUND + e);
			return new ResponseEntity<Status>(new Status(e.getMessage()), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			LOGGER.error("Error occured in DepartmentController.updateDept API: " + e);
			return new ResponseEntity<Status>(new Status(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/{deptId}")
	public ResponseEntity<Status> deleteDepartment(@PathVariable("deptId") String deptId) {
		try {
			deptService.deleteDept(deptId);
			LOGGER.info("Department [" + deptId + "] deleted successfully");
			return new ResponseEntity<Status>(HttpStatus.NO_CONTENT);
		} catch (DepartmentNotFound e) {
			LOGGER.error(DEPT_NOT_FOUND + e);
			return new ResponseEntity<Status>(new Status(e.getMessage()), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			LOGGER.error("Error occured in DepartmentController.delete API: " + e);
			return new ResponseEntity<Status>(new Status(e.getMessage()), HttpStatus.BAD_REQUEST);
		}

	}

	@GetMapping("/{deptId}/employee")
	public ResponseEntity<Response> getEmployeesByDept(@PathVariable("deptId") String deptId) {
		List<Employee> depts = new ArrayList<>();
		try {
			depts = deptService.getEmployeesByDept(deptId);
		} catch (DepartmentNotFound e) {
			LOGGER.error(DEPT_NOT_FOUND + e);
			return new ResponseEntity<Response>(new Response(e.getMessage(), null), HttpStatus.NOT_FOUND);
		} catch (EmployeeNotFoundException e) {
			LOGGER.error(EMPLOYEE_NOT_FOUND + e);
			return new ResponseEntity<Response>(new Response(e.getMessage(), null), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			LOGGER.error("Error occured in DepartmentController.getEmployeesByDept API: " + e);
			return new ResponseEntity<Response>(new Response(e.getMessage(), depts), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Response>(new Response("success", depts), HttpStatus.OK);
	}
}
