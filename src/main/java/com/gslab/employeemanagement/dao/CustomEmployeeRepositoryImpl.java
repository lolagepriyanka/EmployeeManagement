package com.gslab.employeemanagement.dao;

import static com.gslab.employeemanagement.constant.EmployeeConstants.EMPLOYEE_DEPT;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.gslab.employeemanagement.exception.DepartmentNotFound;
import com.gslab.employeemanagement.model.Department;
import com.gslab.employeemanagement.model.Employee;

public class CustomEmployeeRepositoryImpl implements CustomEmployeeRepository {

	@Autowired
	MongoTemplate mongotemplate;

	@Override
	public boolean isEmployeePresent(String empId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("empId").is(empId.toUpperCase()));
		return mongotemplate.findOne(query, Employee.class) != null ? true : false;

	}

	@Override
	public void updateEmployee(String empId, Map<String, Object> payload) {
		Update update = new Update();

		List<Department> deptToAdd = new ArrayList<>();

		for (Map.Entry<String, Object> entry : payload.entrySet()) {
			if (StringUtils.equalsIgnoreCase(entry.getKey(), EMPLOYEE_DEPT)) {
				List<String> depts = (List<String>) entry.getValue();
				for (String dept : depts) {
					Query query = new Query();
					query.addCriteria(Criteria.where("deptId").is(dept));
					Department newDept = mongotemplate.findOne(query, Department.class);
					if (newDept == null)
						throw new DepartmentNotFound("Department doesn't exist with Id: " + dept);
					deptToAdd.add(newDept);
					update.set(entry.getKey(), deptToAdd);
				}
			} else {
				update.set(entry.getKey(), (String) (entry.getValue()));
			}
		}

		Query updateQuery = new Query();
		updateQuery.addCriteria(Criteria.where("empId").is(empId.toUpperCase()));
		mongotemplate.updateFirst(updateQuery, update, Employee.class);
	}

	@Override
	public List<Employee> getEmployeeByDept(String deptId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("departments").in(deptId));
		return mongotemplate.find(query, Employee.class);

	}

	@Override
	public void removeDeptFromEmployees(String deptId) {
		mongotemplate.updateMulti(new Query(),
				new Update().pull("departments", Query.query(Criteria.where("$id").is(deptId))), Employee.class);
	}

	@Override
	public boolean getEmployeeByEmail(String email) {
		Query query = new Query();
		query.addCriteria(Criteria.where("email").is(email));
		Employee emp = mongotemplate.findOne(query, Employee.class);
		return emp != null ? true : false;

	}

}
