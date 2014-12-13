package com.com.android.search.json;
/**
 * resultset für pojo
 */
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResultSet {
	private String query;
	private List<Result> result;

	public String getQuery() {
		return this.query;
	}

	@JsonProperty("Query")
	public void setQuery(String query) {
		this.query = query;
	}

	public List<Result> getResult() {
		return this.result;
	}

	@JsonProperty("Result")
	public void setResult(List<Result> result) {
		this.result = result;
	}
}

