package com.gslab.employeemanagement.service;

import static com.gslab.employeemanagement.constant.EmployeeConstants.EMPLOYEE_DEPT;
import static com.gslab.employeemanagement.constant.EmployeeConstants.EMPLOYEE_EMAIL;
import static com.gslab.employeemanagement.constant.EmployeeConstants.EMPLOYEE_ID;
import static com.gslab.employeemanagement.constant.EmployeeConstants.EMPLOYEE_MANAGER;
import static com.gslab.employeemanagement.constant.EmployeeConstants.EMPLOYEE_NAME;
import static com.gslab.employeemanagement.constant.ErrorMessageConstant.DEPT_NOT_FOUND;
import static com.gslab.employeemanagement.constant.ErrorMessageConstant.EMAIL_ERROR;
import static com.gslab.employeemanagement.constant.ErrorMessageConstant.EMPTY_DEPT_ERROR;
import static com.gslab.employeemanagement.constant.ErrorMessageConstant.EMP_ID_CHANGE_ERROR;
import static com.gslab.employeemanagement.constant.ErrorMessageConstant.EMP_NAME_ERROR;
import static com.gslab.employeemanagement.constant.ErrorMessageConstant.MANAGER_NAME_ERROR;
import static com.gslab.employeemanagement.constant.ErrorMessageConstant.MIN_DEPT_ERROR;
import static com.gslab.employeemanagement.constant.ErrorMessageConstant.NULL_DEPT_ERROR;
import static com.gslab.employeemanagement.constant.ErrorMessageConstant.WRONG_EMP_ATTR;
import static com.gslab.employeemanagement.constant.ErrorMessageConstant.ZERO_EMP_ERROR;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gslab.employeemanagement.constant.EmployeeConstants;
import com.gslab.employeemanagement.dao.DepartmentRepository;
import com.gslab.employeemanagement.dao.EmployeeRepository;
import com.gslab.employeemanagement.exception.DepartmentNotFound;
import com.gslab.employeemanagement.exception.EmployeeExistException;
import com.gslab.employeemanagement.exception.EmployeeNotFoundException;
import com.gslab.employeemanagement.exception.InvalidInputException;
import com.gslab.employeemanagement.model.Department;
import com.gslab.employeemanagement.model.Employee;
import com.gslab.employeemanagement.model.EmployeeRequest;

@Service
public class EmployeeService {

	@Autowired
	EmployeeRepository empRepo;

	@Autowired
	DepartmentRepository deptRepo;

	public Employee createEmp(EmployeeRequest employee) {
		validateCreatePayload(employee);
		List<Department> finalDepts = new ArrayList<>();
		for (String dept : employee.getDepartments()) {
			Optional<Department> presentDept = deptRepo.findById(dept);
			if (presentDept.isEmpty()) {
				throw new DepartmentNotFound(dept + DEPT_NOT_FOUND);
			}
			finalDepts.add(presentDept.get());
		}
		Employee emp = new Employee();
		BeanUtils.copyProperties(employee, emp);
		emp.setDepartments(finalDepts);
		Random random = new Random();
		emp.setEmpId(EmployeeConstants.EMPID_PREFIX + random.nextInt(10000));
		return empRepo.save(emp);
	}

	public List<Employee> fetchAllEmployees() {
		List<Employee> employees = empRepo.findAll();
		if (employees.size() <= 0) {
			throw new EmployeeNotFoundException(ZERO_EMP_ERROR);
		}
		return employees;
	}

	public void deleteEmployee(String empId) {
		Optional<Employee> emp = empRepo.findById(empId);
		if (emp.isEmpty()) {
			throw new EmployeeNotFoundException("Employee " + empId + " not present.");
		}
		empRepo.deleteById(empId);
	}

	public void validateCreatePayload(EmployeeRequest employee) {
		if (empRepo.getEmployeeByEmail(employee.getEmail()))
			throw new EmployeeExistException("Employe is already present in the database.");
		if (StringUtils.isBlank(employee.getEmpName()))
			throw new InvalidInputException(EMP_NAME_ERROR);
		if (StringUtils.isBlank(employee.getManager()))
			throw new InvalidInputException(MANAGER_NAME_ERROR);
		if (StringUtils.isBlank(employee.getEmail()))
			throw new InvalidInputException(EMAIL_ERROR);
		if (employee.getDepartments().size() == 0)
			throw new InvalidInputException(MIN_DEPT_ERROR);
	}

	public void updateEmployee(String empId, Map<String, Object> requestMap) {
		if (!empRepo.isEmployeePresent(empId))
			throw new EmployeeExistException("Employee " + empId + " is not present.");
		Map<String, Object> payload = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);
		payload.putAll(requestMap);
		validateUpdatePayload(payload);
		empRepo.updateEmployee(empId, payload);
	}

	private void validateUpdatePayload(Map<String, Object> payload) {

		Set<String> keys = payload.keySet();

		if (payload.containsKey(EMPLOYEE_ID)) {
			throw new InvalidInputException(EMP_ID_CHANGE_ERROR);
		}
		if (payload.containsKey(EMPLOYEE_DEPT)) {
			List<String> depts = (List<String>) (payload.get(EMPLOYEE_DEPT));
			if (depts.size() == 0)
				throw new InvalidInputException(EMPTY_DEPT_ERROR);
			for (String dept : depts) {
				if (StringUtils.isBlank(dept))
					throw new InvalidInputException(NULL_DEPT_ERROR);
			}
		}

		for (String key : keys) {
			if (!(key.equalsIgnoreCase(EMPLOYEE_DEPT) || key.equalsIgnoreCase(EMPLOYEE_EMAIL)
					|| key.equalsIgnoreCase(EMPLOYEE_MANAGER) || key.equalsIgnoreCase(EMPLOYEE_NAME))) {
				throw new InvalidInputException(WRONG_EMP_ATTR);
			}
		}

	}

}
