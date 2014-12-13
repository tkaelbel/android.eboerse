package com.com.android.eboerse.webrip.webrip.model;

import com.com.android.eboerse.stock.ArrayAdapterable;

public class WebripStockNewsInfo implements ArrayAdapterable{

	private String news;
	private String htmlLink;
	private String timeAndSite;
	
	public String getNews() {
		return news;
	}
	public String getTimeAndSite() {
		return timeAndSite;
	}
	public void setTimeAndSite(String timeAndSite) {
		this.timeAndSite = timeAndSite;
	}
	public void setNews(String news) {
		this.news = news;
	}
	public String getHtmlLink() {
		return htmlLink;
	}
	public void setHtmlLink(String htmlLink) {
		this.htmlLink = htmlLink;
	}
	
	

}
