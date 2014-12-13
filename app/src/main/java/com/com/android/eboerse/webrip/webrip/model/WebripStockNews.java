package com.com.android.eboerse.webrip.webrip.model;

/**
 * Created by tok on 11.12.2014.
 */
public class WebripStockNews {

    String header;
    String img;
    String news;
    String headerFull;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    String time;
    String from;

    public String getHeaderFull() {
        return headerFull;
    }

    public void setHeaderFull(String headerFull) {
        this.headerFull = headerFull;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getNews() {
        return news;
    }

    public void setNews(String news) {
        this.news = news;
    }
}
