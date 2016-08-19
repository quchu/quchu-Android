package co.quchu.quchu.model;

import java.io.Serializable;

/**
 * CityModel
 * User: Chenhs
 * Date: 2015-12-09
 * 城市信息
 */
public class CityModel implements Serializable {

    /**
     * cvalue : 厦门
     * cid : 5
     */
    public CityModel(String cityName, int cid, boolean isSelected) {
        this.cvalue = cityName;
        this.cid = cid;
        this.isSelected = isSelected;
    }

    private String cvalue = "";
    private int cid = 0;
    private boolean isSelected = false;

    //1-为国内;0-为国外
    private String isInland = "";

    public String getIsInland() {
        return isInland;
    }

    public void setIsInland(String isInland) {
        this.isInland = isInland;
    }

    public void setCvalue(String cvalue) {
        this.cvalue = cvalue;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getCvalue() {
        return cvalue;
    }

    public int getCid() {
        return cid;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
