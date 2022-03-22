package com.gslab.employeemanagement.service;

import static com.gslab.employeemanagement.constant.EmployeeConstants.DEPT_HEAD;
import static com.gslab.employeemanagement.constant.EmployeeConstants.DEPT_ID;
import static com.gslab.employeemanagement.constant.EmployeeConstants.DEPT_NAME;
import static com.gslab.employeemanagement.constant.ErrorMessageConstant.DEPT_HAED_ERROR;
import static com.gslab.employeemanagement.constant.ErrorMessageConstant.DEPT_ID_CHANGE_ERROR;
import static com.gslab.employeemanagement.constant.ErrorMessageConstant.DEPT_ID_ERROR;
import static com.gslab.employeemanagement.constant.ErrorMessageConstant.DEPT_NAME_ERROR;
import static com.gslab.employeemanagement.constant.ErrorMessageConstant.DEPT_NOT_FOUND;
import static com.gslab.employeemanagement.constant.ErrorMessageConstant.WRONG_DEPT_ATTR;
import static com.gslab.employeemanagement.constant.ErrorMessageConstant.ZERO_DEPT_ERROR;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gslab.employeemanagement.dao.DepartmentRepository;
import com.gslab.employeemanagement.dao.EmployeeRepository;
import com.gslab.employeemanagement.exception.DepartmentExistException;
import com.gslab.employeemanagement.exception.DepartmentNotFound;
import com.gslab.employeemanagement.exception.InvalidInputException;
import com.gslab.employeemanagement.model.Department;
import com.gslab.employeemanagement.model.Employee;

@Service
public class DepartmentService {

	@Autowired
	DepartmentRepository deptRepo;

	@Autowired
	EmployeeRepository empRepo;

	public void createDept(Department dept) {
		validateDeptPayload(dept);
		deptRepo.save(dept);
	}

	private void validateDeptPayload(Department dept) {
		if (StringUtils.isBlank(dept.getDeptId()))
			throw new InvalidInputException(DEPT_ID_ERROR);
		if (StringUtils.isBlank(dept.getDeptName()))
			throw new InvalidInputException(DEPT_NAME_ERROR);
		if (StringUtils.isBlank(dept.getDeptHead()))
			throw new InvalidInputException(DEPT_HAED_ERROR);
		if (deptRepo.isDeptPresent(dept.getDeptId()))
			throw new DepartmentExistException("Department " + dept.getDeptId() + " is already present.");

	}

	public List<Department> getAllDepts() {
		List<Department> departments = deptRepo.findAll();
		if (departments.size() == 0) {
			throw new DepartmentNotFound(ZERO_DEPT_ERROR);
		}
		return departments;
	}

	public void updateDepartment(String deptId, Map<String, String> deptPayload) {
		if (!deptRepo.isDeptPresent(deptId))
			throw new DepartmentNotFound(DEPT_NOT_FOUND);
		validateDeptUpdatePayload(deptPayload);
		deptRepo.updateDepartment(deptId, deptPayload);
	}

	private void validateDeptUpdatePayload(Map<String, String> deptPayload) {
		Set<String> keys = deptPayload.keySet();
		if (deptPayload.containsKey(DEPT_ID))
			throw new InvalidInputException(DEPT_ID_CHANGE_ERROR);

		for (String key : keys) {
			if (!(key.equalsIgnoreCase(DEPT_HEAD) || key.equalsIgnoreCase(DEPT_NAME))) {
				throw new InvalidInputException(WRONG_DEPT_ATTR);
			}
		}

	}

	public void deleteDept(String deptId) {
		if (!deptRepo.isDeptPresent(deptId))
			throw new DepartmentNotFound(DEPT_NOT_FOUND);
		empRepo.removeDeptFromEmployees(deptId);
		deptRepo.deleteById(deptId);
	}

	public List<Employee> getEmployeesByDept(String deptId) {
		if (!deptRepo.isDeptPresent(deptId))
			throw new DepartmentNotFound(DEPT_NOT_FOUND);		
		List<Employee> employees = new ArrayList<Employee>();
		employees = empRepo.getEmployeeByDept(deptId);
		if (employees.size() == 0) {
			throw new DepartmentNotFound("No employees found under department " + deptId);
		}
		return employees;
	}

}
