package com.com.android.stock.indices;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Stockindiceresultset pojo für indices
 * @author Tok
 *
 */
@Deprecated
public class StockIndiceResultSet {
	
	private List<StockIndiceInfo> row;
	private Object result;
	
	
	public Object getResult() {
		return result;
	}
	
	@JsonProperty("results")
	public void setResult(Object result) {
		this.result = result;
	}

	public List<StockIndiceInfo> getRow() {
		return this.row;
	}

	@JsonProperty("row")
	public void setRow(List<StockIndiceInfo> row) {
		this.row = row;
	}
	
	
	
}
