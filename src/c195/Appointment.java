/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 *
 * @author A Suzuki
 */
public class Appointment {
    int appointmentId;
    int customerId;
    int userId;
    String title;
    String description;
    String location;
    String contact;
    String type;
    String url;
    Timestamp start;
    Timestamp end;
    Timestamp createDate;
    String createdBy;
    Timestamp lastUpdate;
    String lastUpdateBy;
    
    String startDisplay;
    String endDisplay;
    
    //setter
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setStart(Timestamp start) {
        this.start = start;
        this.startDisplay = convertToDisplay(start);
    }

    public void setEnd(Timestamp end) {
        this.end = end;
        this.endDisplay = convertToDisplay(end);
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }
    
    //getter
    public int getAppointmentId() {
        return appointmentId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getContact() {
        return contact;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public Timestamp getStart() {
        return start;
    }

    public Timestamp getEnd() {
        return end;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public String getStartDisplay() { //always from DB to application
        return startDisplay;
    }

    public String getEndDisplay() { //always from DB to application
        return endDisplay;
    }
    
    
    public String convertToDisplay(Timestamp timestamp){
        LocalDateTime ldt = timestamp.toLocalDateTime();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm");
        String ldts = format.format(ldt);
        return ldts;
    }
    
    /*
        This method is used when you store time on database.
    */
    public Timestamp changeLocalToUTC(Timestamp localTime){
        ZonedDateTime zdt = ZonedDateTime.of(localTime.toLocalDateTime(),
                TimeZone.getDefault().toZoneId());
        ZonedDateTime zdtUTC = zdt.withZoneSameInstant( 
                TimeZone.getTimeZone("UTC").toZoneId());
        Timestamp ts = Timestamp.valueOf(zdtUTC.toLocalDateTime());
        return ts;
    }
    
    /* 
        This method is uded when you retrieve time from database.
    */
    public Timestamp changeUTCToLocal(Timestamp utcTime){
        ZonedDateTime zdt = ZonedDateTime.of(utcTime.toLocalDateTime(), 
                TimeZone.getTimeZone("UTC").toZoneId());
        ZonedDateTime zdtLocal = zdt.withZoneSameInstant(
                TimeZone.getDefault().toZoneId());
        Timestamp ts = Timestamp.valueOf(zdtLocal.toLocalDateTime());
        return ts;
    }   
}

