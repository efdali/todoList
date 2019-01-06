package com.efdalincesu.todolist.Model;

import com.efdalincesu.todolist.DBSqlite.DBHelper;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

public class Todo implements Serializable {

    @SerializedName(DBHelper.ID_COLUMN)
    private int id;
    @SerializedName(DBHelper.TITLE_COLUMN)
    private String title;

    @SerializedName(DBHelper.SUMMARY_COLUMN)
    private String summary;

    @SerializedName(DBHelper.DATENOW_COLUMN)
    private String datenow;

    @SerializedName(DBHelper.DATE_COLUMN)
    private String date;

    @SerializedName(DBHelper.REMINDER_COLUMN)
    private String reminder;

    @SerializedName(DBHelper.STATUS_COLUMN)
    private boolean status;


    public Todo(int id, String title, String summary, String datenow, String date, String reminder, boolean status) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.datenow = datenow;
        this.date = date;
        this.reminder=reminder;
        this.status = status;
    }

    public Todo(String title, String summary, String date, boolean status) {
        this.title = title;
        this.summary = summary;
        this.datenow = DateFormat.getDateInstance(DateFormat.LONG).format(new Date());
        this.date = date;
        this.reminder=null;
        this.status = status;
    }

    public Todo(String title, String summary, String date) {
        this.title = title;
        this.summary = summary;
        this.datenow = DateFormat.getDateInstance(DateFormat.LONG).format(new Date());
        this.date = date;
        this.reminder=null;
        this.status = false;
    }

    public Todo(String title, String summary, boolean status) {
        this.title = title;
        this.summary = summary;
        this.datenow = DateFormat.getDateInstance(DateFormat.LONG).format(new Date());
        this.date = null;
        this.reminder=null;
        this.status = status;
    }

    public Todo(String title, String summary) {
        this.title = title;
        this.summary = summary;
        this.datenow = DateFormat.getDateInstance(DateFormat.LONG).format(new Date());
        this.date = null;
        this.reminder=null;
        this.status = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDatenow() {
        return datenow;
    }

    public void setDatenow(String datenow) {
        this.datenow = datenow;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getDay() {

        if (date != null) {
            String date = this.date.split(" ")[0];
            String date1[] = date.split("-");
            return Integer.parseInt(date1[2]);
        } else
            return 0;
    }

    public int getMonth() {

        if (date != null) {
            String date = this.date.split(" ")[0];
            String[] date1 = date.split("-");

            return Integer.parseInt(date1[1]);
        } else
            return 0;
    }

    public int getYear() {
        if (date != null) {
            String date = this.date.split(" ")[0];
            String[] date1 = date.split("-");

            return Integer.parseInt(date1[0]);
        } else
            return 0;
    }

    public String getTime() {

        if (date != null) {
            String[] date = this.date.split(" ");

            if (date.length > 1) {
                return date[1];
            } else
                return "";
        } else
            return "";
    }

    @Override
    public String toString() {
        return "Id : " + id + "\n" +
                "Title : " + title + "\n" +
                "Summary : " + summary + "\n" +
                "DateNow : " + datenow + "\n" +
                "Date : " + date + "\n" +
                "Status: " + (status ? " başarılı" : "başarısız");
    }
}
