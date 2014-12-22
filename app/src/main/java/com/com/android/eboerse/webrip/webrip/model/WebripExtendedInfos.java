package com.com.android.eboerse.webrip.webrip.model;

import java.util.ArrayList;

/**
 * Created by tok on 20.12.2014.
 */
public class WebripExtendedInfos {

    String branche;
    String industrie;
    ArrayList<WebripStockComponentOf> componentOf = new ArrayList<WebripStockComponentOf>();

    public String getBranche() {
        return branche;
    }

    public void setBranche(String branche) {
        this.branche = branche;
    }

    public String getIndustrie() {
        return industrie;
    }

    public void setIndustrie(String industrie) {
        this.industrie = industrie;
    }

    public ArrayList<WebripStockComponentOf> getComponentOf() {
        return componentOf;
    }

    public void setComponentOf(ArrayList<WebripStockComponentOf> componentOf) {
        this.componentOf = componentOf;
    }
}
