package com.gslab.employeemanagement.dao;

import java.util.Map;

public interface CustomDepartmentRepository {

	boolean isDeptPresent(String deptId);
	void updateDepartment(String deptId, Map<String, String> deptPayload);
}
