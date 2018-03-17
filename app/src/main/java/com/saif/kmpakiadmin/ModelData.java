package com.saif.kmpakiadmin;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Mosaad on 23/11/2017.
 */

@IgnoreExtraProperties
public class ModelData implements Serializable {
    String key;
    String title;
    String desc;
    String date;
    public ModelData(){

    }
    public ModelData(String key, String title, String desc, String date) {
        this.key = key;
        this.title = title;
        this.desc = desc;
        this.date = date;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getYear() {
        return Integer.parseInt(date.substring(0, 4));
    }


    public int getMonth() {
        return Integer.parseInt(date.substring(5, 7));
    }

    public int getDay() {
        return Integer.parseInt(date.substring(8, 10));
    }

    @Override
    public String toString() {
        return "ModelData{" +
                "key='" + key + '\'' +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

}
