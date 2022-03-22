package com.gslab.employeemanagement.model;

public class Status {
	private String error;

	public Status(String error) {
		super();
		this.error = error;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
