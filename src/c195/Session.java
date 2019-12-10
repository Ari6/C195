/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.TimeZone;

/**
 *
 * @author A Suzuki
 */
public class Session {
    private static int loginId;
    private static String loginName;
    private static LocalDateTime startDate;
    private static int view;
    private static String locale;
    
    public static void setSession(int loginId, String loginName){
        Session.loginId = loginId;
        Session.loginName = loginName;
        setStartDate();
        Session.view = 0; //0: WEEK 1:MONTH
    }
    public static int getLoginId(){
        return Session.loginId;
    }
    public static String getLoginName(){
        return Session.loginName;
    }
    public static LocalDateTime getStartDate(){
        return Session.startDate;
    }

    public static int getView() {
        return view;
    }
    public static void setView(int view){
        Session.view = view;
    }
    public static void setStartDate(){
        LocalDateTime ldt = LocalDateTime.now();
        ZonedDateTime zdt = ZonedDateTime.of(ldt, TimeZone.getDefault().toZoneId());
        Session.startDate = zdt.withZoneSameInstant(TimeZone.getTimeZone("UTC").toZoneId()).toLocalDateTime();
    }
    public static void setStartDate(LocalDateTime startDateTime){
        LocalDateTime ldt = startDateTime;
        ZonedDateTime zdt = ZonedDateTime.of(ldt, TimeZone.getDefault().toZoneId());
        Session.startDate = zdt.withZoneSameInstant(TimeZone.getTimeZone("UTC").toZoneId()).toLocalDateTime();
    }
    
    public static void writeLog(String log) throws IOException{
        try(FileWriter fw = new FileWriter("log.txt", true)) {
            String form = ZonedDateTime.now() + "," + 
                    Session.getLoginId() + ":" + Session.getLoginName() + ",";
            fw.write(form + log +"\n");
        }
    }
}
