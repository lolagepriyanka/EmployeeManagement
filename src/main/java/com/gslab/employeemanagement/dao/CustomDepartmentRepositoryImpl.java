package com.gslab.employeemanagement.dao;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.gslab.employeemanagement.model.Department;

@Repository
public class CustomDepartmentRepositoryImpl implements CustomDepartmentRepository {

	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public boolean isDeptPresent(String deptId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("deptId").is(deptId.toUpperCase()));
		return mongoTemplate.findOne(query, Department.class) != null ? true : false;
	}

	@Override
	public void updateDepartment(String deptId, Map<String, String> deptPayload) {
		Update update = new Update();

		for (Map.Entry<String, String> entry : deptPayload.entrySet()) {
			update.set(entry.getKey(), entry.getValue());
		}
		Query updateQuery = new Query();
		updateQuery.addCriteria(Criteria.where("deptId").is(deptId));
		mongoTemplate.updateFirst(updateQuery, update, Department.class);
	}

}
