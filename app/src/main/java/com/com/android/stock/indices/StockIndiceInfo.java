package com.com.android.stock.indices;
/**
 * für die topflop/zusammensetzung pojo bekommt CSV
 * @author Tok
 *
 */
public class StockIndiceInfo{
	
	private String col0;
	private String col1;
	private String col2;
	private String col3;
	private String col4;
	private String col5;
	private String col6;
	private String col7;
	private String col8;
	private String date;
	
	public StockIndiceInfo(String symbol, String name, String change, String lastTrade, String symbol1, 
			String symbol2, String dayRange, String symbol3, String dontKnow, String date) {
		this.col0 = symbol;
		this.col1 = name;
		this.col2 = change;
		this.col3 = lastTrade;
		this.col4 = symbol1;
		this.col5 = symbol2;
		this.col6 = dayRange;
		this.col7 = symbol3;
		this.col8 = dontKnow;
		this.date = date;
	}
	
	
	public String getCol0() {
		return col0;
	}
	public String getCol1() {
		return col1;
	}
	public String getCol2() {
		return col2;
	}
	public String getCol3() {
		return col3;
	}
	public String getCol4() {
		return col4;
	}
	public String getCol5() {
		return col5;
	}
	public String getCol6() {
		return col6;
	}
	public String getCol7() {
		return col7;
	}
	public String getCol8() {
		return col8;
	}
	public String getDate() {
		return date;
	}
	
}
