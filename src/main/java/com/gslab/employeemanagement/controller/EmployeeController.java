package com.gslab.employeemanagement.controller;

import static com.gslab.employeemanagement.constant.ErrorMessageConstant.DEPT_NOT_FOUND;
import static com.gslab.employeemanagement.constant.ErrorMessageConstant.EMPLOYEE_NOT_FOUND;
import static com.gslab.employeemanagement.constant.ErrorMessageConstant.WRONG_PAYLOAD;

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

import com.gslab.employeemanagement.exception.DepartmentNotFound;
import com.gslab.employeemanagement.exception.EmployeeExistException;
import com.gslab.employeemanagement.exception.EmployeeNotFoundException;
import com.gslab.employeemanagement.exception.InvalidInputException;
import com.gslab.employeemanagement.model.Employee;
import com.gslab.employeemanagement.model.EmployeeRequest;
import com.gslab.employeemanagement.model.Response;
import com.gslab.employeemanagement.model.Status;
import com.gslab.employeemanagement.service.EmployeeService;

@RestController
@RequestMapping("/org/employee")
public class EmployeeController {
	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);
	@Autowired
	EmployeeService empService;

	@GetMapping
	public ResponseEntity<Response> getAllEmployees() {
		List<Employee> employees = new ArrayList<>();
		try {
			employees = empService.fetchAllEmployees();

		} catch (EmployeeNotFoundException e) {
			LOGGER.error(EMPLOYEE_NOT_FOUND + e);
			return new ResponseEntity<Response>(new Response(e.getMessage(), employees), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			LOGGER.error("Error occured in EmployeeController.getAllEmployees API: " + e);
			return new ResponseEntity<Response>(new Response(e.getMessage(), employees),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Response>(new Response("success", employees), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Response> createEmployee(@RequestBody EmployeeRequest employee) {
		try {
			Employee createdEmployee = empService.createEmp(employee);
			LOGGER.info("Employee with " + createdEmployee.getEmpId() + " is created successfully.");
			return new ResponseEntity<Response>(
					new Response("Employee created with Id : " + createdEmployee.getEmpId(), null), HttpStatus.CREATED);

		} catch (InvalidInputException | EmployeeExistException e) {
			LOGGER.error(WRONG_PAYLOAD + e);
			return new ResponseEntity<Response>(new Response(e.getMessage(), null), HttpStatus.BAD_REQUEST);
		} catch (DepartmentNotFound e) {
			LOGGER.error(DEPT_NOT_FOUND + e);
			return new ResponseEntity<Response>(new Response(e.getMessage(), null), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			LOGGER.error("Error occured in EmployeeController.createEmployee API: " + e);
			return new ResponseEntity<Response>(new Response(e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@DeleteMapping("/{empId}")
	public ResponseEntity<Status> deleteEmployee(@PathVariable("empId") String empId) {
		try {
			empService.deleteEmployee(empId.trim());
			LOGGER.info("Employee [" + empId + "] removed successfully");
			return new ResponseEntity<Status>(HttpStatus.NO_CONTENT);
		} catch (EmployeeNotFoundException e) {
			LOGGER.error(EMPLOYEE_NOT_FOUND + e);
			return new ResponseEntity<Status>(new Status(e.getMessage()), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			LOGGER.error("Error occured in EmployeeController.deleteEmployee API: " + e);
			return new ResponseEntity<Status>(new Status(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PatchMapping("/{empId}")
	public ResponseEntity<Status> updateEmployee(@PathVariable("empId") String empId,
			@RequestBody Map<String, Object> requestMap) {
		try {
			empService.updateEmployee(empId, requestMap);
			LOGGER.info("Employee [" + empId + "] updated successfully");
			return new ResponseEntity<Status>(HttpStatus.OK);
		} catch (EmployeeExistException e) {
			LOGGER.error(EMPLOYEE_NOT_FOUND + e);
			return new ResponseEntity<Status>(new Status(e.getMessage()), HttpStatus.NOT_FOUND);
		} catch (InvalidInputException e) {
			LOGGER.error(WRONG_PAYLOAD + e);
			return new ResponseEntity<Status>(new Status(e.getMessage()), HttpStatus.BAD_REQUEST);
		} catch (DepartmentNotFound e) {
			LOGGER.error(DEPT_NOT_FOUND + e);
			return new ResponseEntity<Status>(new Status(e.getMessage()), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			LOGGER.error("Error occured in EmployeeController.updateEmployee API: " + e);
			return new ResponseEntity<Status>(new Status(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
