/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195;

import java.sql.Timestamp;

/**
 *
 * @author A Suzuki
 */
public class Country {
    private int countryId;
    private String country;
    private Timestamp createDate; //Datatime
    private String createdBy;
    private Timestamp lastUpdate; //Timestamp
    private String lastUpdateBy;
    
    //setter
    public void setCountryId(int countryId){ this.countryId = countryId; }
    public void setCountry(String country) { this.country = country; }
    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate; }
    public void setLastUpdate(Timestamp lastUpdate){
        this.lastUpdate = lastUpdate; }
    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy; }
    
    //getter
    public int getCountryId() { return this.countryId; }
    public String getCountry() { return this.country; }
    public Timestamp getCreateDate() { return this.createDate; }
    public String getCreatedBy() { return this.createdBy; }
    public Timestamp getLastUpdate() { return this.lastUpdate; }
    public String getLastUpdateBy() { return this.lastUpdateBy; }
    
}
