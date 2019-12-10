/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195;

import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author A Suzuki
 */
public class City {
    private int cityId;
    private String city;
    private int countryId;
    private Date createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;
    
    //Setters
    public void setCityId(int cityId){ this.cityId = cityId; }
    public void setCity(String city) { this.city = city; }
    public void setCountryId(int countryId) { this.countryId = countryId; }
    public void setCreatedDate(Date createDate) { this.createDate = createDate; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public void setLastUpdate(Timestamp lastUpdate) { this.lastUpdate = lastUpdate; }
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdateBy = lastUpdatedBy;
    }
    
    //getters
    public int getCityId(){ return this.cityId; }
    public String getCity() { return this.city; }
    public int getCountryId() { return this.countryId; }
    public Date getCreateDate() { return this.createDate; }
    public String getCreatedBy() { return this.createdBy; }
    public Timestamp getLastUpdate() { return this.lastUpdate; }
    public String getLastUpdateBy() { return this.lastUpdateBy; }
    
}
