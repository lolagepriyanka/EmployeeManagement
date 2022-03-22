package com.gslab.employeemanagement.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class Response {
	private String message;
	@JsonInclude(value = Include.NON_EMPTY)
	private List<?> responseBody;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<?> getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(List<?> responseBody) {
		this.responseBody = responseBody;
	}

	public Response(String message, List<?> responseBody) {
		super();
		this.message = message;
		this.responseBody = responseBody;
	}
}
