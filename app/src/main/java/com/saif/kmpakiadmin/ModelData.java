package com.saif.kmpakiadmin;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Mosaad on 23/11/2017.
 */

public class ModelData implements Serializable {
    int id;
    String summary;
    Date startDate;
    String date;

    int year;
    int month;
    int day;

    public ModelData(int id,String summary, String startDate) {
        this.summary = summary;
        date = startDate;
        this.id= id;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public int getYear() {
        return Integer.parseInt(date.substring(0, 4));
    }

    public void setYear(int year) {

        this.year = year;
    }

    public int getMonth() {
        return Integer.parseInt(date.substring(5, 7));
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return Integer.parseInt(date.substring(8, 10));
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static ModelData fromJson(String json){
        return new Gson().fromJson(json,ModelData.class);
    }

}
