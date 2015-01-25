package com.com.android.eboerse.stock;

import java.util.Date;
/**
 * Pojo StockInfo fuer kurs daten
 * @author Tok
 *
 */
public class StockInfo implements ArrayAdapterable{

	private String daysLow = "";
	private String daysHigh = "";
	private String yearLow = "";
	private String yearHigh = "";
	private String name = "";
	private String lastTradePriceOnly = "";
	private String change = "";
	private String daysRange = "";
	private String dateTime = "";
	private String stockExchange = "";
	private String symbol = "";
    private String currency = "";

	public String getDaysLow() { return daysLow; }
	public String getDaysHigh() { return daysHigh; }
	public String getYearLow() { return yearLow; }
	public String getYearHigh() { return yearHigh; }
	public String getName() { return name; }

	public String getLastTradePriceOnly() { return lastTradePriceOnly; }
	public String getChange() { return change;}
	public String getDaysRange() {return daysRange;}
	public String getDateTime()  {return dateTime;}
	public String getStockExchange() {return stockExchange;}
	public String getSymbol() {return symbol;}

    public String getCurrency() {return currency;}

    public StockInfo(String daysLow, String daysHigh, String yearLow,
			String yearHigh, String name, String lastTradePriceOnly,
			String change, String daysRange, String date, String stockExchange, String symbol, String currency) {
		this.daysLow = daysLow;
		this.daysHigh = daysHigh;
		this.yearLow = yearLow;
		this.yearHigh = yearHigh;
		this.name = name;
		this.lastTradePriceOnly = lastTradePriceOnly;
		this.change = change;
		this.daysRange = daysRange;
		this.dateTime = date;
		this.stockExchange = stockExchange;
		this.symbol = symbol;
        this.currency = currency;

	}

	public String getInfos(){
		if(currency != null){
           return dateTime + "      " + lastTradePriceOnly + "  "+ currency;
        }else{
            return dateTime + "      " + lastTradePriceOnly;
        }
	}

}
