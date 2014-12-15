package com.com.android.eboerse.main;

/**
 * wichtige abkuerzungen fuer YQL
 * @author Tok
 *
 */
public class SymbolsGoodToKnow {
	
	public static final String YAHOO_HISTORICAL_FIRST_URL = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20%3D%20%22";
	public static final String YAHOO_HISTORICAL_SEC_URL = "%22%20and%20startDate%20%3D%20%22";
	public static final String YAHOO_HISTORICAL_THIRD_URL = "%22%20and%20endDate%20%3D%20%22";
	public static final String YAHOO_HISTORICAL_FOURTH_URL = "%22&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
	
	public static final String YAHOO_URL_FIRST = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quote%20where%20symbol%20in%20(%22";
	public static final String YAHOO_URL_SECOND = "%22)&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
	
	public static final String YAHOO_URL_IDX_FIRST = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20csv%20where%20url%3D'http%3A%2F%2Fdownload.finance.yahoo.com%2Fd%2Fquotes.csv%3Fs%3D%2540%255E";
	public static final String YAHOO_URL_IDX_SEC = "%26f%3Dsnc1l1%3D.csv'&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
	public static final String YAHOO_URL_IDX_BESTANDTEILE_FIRST = "https://de.finance.yahoo.com/q/cp?s=";
	public static final String YAHOO_URL_NEWS = "https://de.finance.yahoo.com/q/h?s=";
	public static final String YAHOO_URL_AKT_NEWS = "https://de.finance.yahoo.com/nachrichten/thema-top-nachrichten/";
    public static final String YAHOO_URL_NEWS_DETAIL = "https://de.finance.yahoo.com/";

	public static final String UTF_8_AE = "u00c4";
	public static final String UTF_8_OE = "u00d6";
	public static final String UTF_8_ae = "u00e4";
	public static final String UTF_8_ue = "u00fc";
	public static final String UTF_8_oe = "u00f6";
	public static final String UTF_8_ss = "u00df";
	public static final String UTF_8_UE = "u00dc";
    public static final String UTF_8_LP = "u00ab";
    public static final String UTF_8_RP = "u00bb";

    public static final String HTML_AN = "&quot;";

	public static final String DAX = "^GDAXI";
	public static final String TEXDAX = "^TECDAX";
	public static final String MDAX = "^MDAXI";
	public static final String SDAX = "^SDAXI";
	public static final String HDAX = "^GDAXHI";
	public static final String CDAX = "^CDAXX";
	
	public static final String GOLD = "GCG15.CMX";
	public static final String OEL = "BZJ14.NYM";
	public static final String SILBER = "SIH14.CMX";
	public static final String KUPFER = "HGZ13.CMX";
	public static final String PALLADIUM = "PAZ13.NYM";
	public static final String PLATINUM = "PLF14.NYM";
	public static final String CRUDE_OEL = "CLF14.NYM";
	public static final String HEIZ_OEL = "HOZ13.NYM";
	public static final String GAS = "NGZ13.NYM";
	public static final String GASOLINE = "RBZ13.NYM";
	
	public static final String EUR_USD = "EURUSD=X";
	public static final String GBP_USD = "GBPUSD=X";
	public static final String EUR_CHF = "EURCHF=X";
	public static final String USD_CHF = "USDCHF=X";
	public static final String USD_JPY = "USDJPY=X";
	public static final String USD_CAD = "USDCAD=X";
	public static final String EUR_GBR = "EURGBP=X";
	public static final String AUD_USD = "AUDUSD=X";
	
	public static final String EUR_STOXX = "^STOXX50E";
	public static final String FTSE = "^FTSE";
	public static final String CAC = "^FCHI";
	public static final String ATX = "^ATX";
	public static final String RTS = "RTS.RS";
	public static final String AEX = "^AEX";
	public static final String BEL = "^BFX";
	public static final String IBEX = "^IBEX";
	
	public static final String DOW = "^DJI";
	public static final String SP ="^GSPC";
	public static final String NASDAQ = "^NDX";
	public static final String NIKKEI = "^N225";
	public static final String HANG = "^HSI";
	public static final String BSE = "^BSESN";
	public static final String BVSP = "^BVSP";
	public static final String IPC = "^MXX";
	public static final String MERV = "^MERV";
	
	
}
