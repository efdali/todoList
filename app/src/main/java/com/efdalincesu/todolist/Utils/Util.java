package com.efdalincesu.todolist.Utils;

import com.efdalincesu.todolist.Model.Todo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Util {

    static int day;
    static int month;
    static int year;


    public static String format(int n) {


        return n < 10 ? "0" + n : n + "";
    }

    public static boolean isPast(Todo todo) {

        timeInit();
        int todoDay = todo.getDay();
        int todoMonth = todo.getMonth();
        int todoYear = todo.getYear();
        if (todoYear < year)
            return true;
        else if (todoYear == year) {
            if (todoMonth < month)
                return true;
            else if (todoMonth == month) {
                if (todoDay < day)
                    return true;
                else
                    return false;
            } else
                return false;
        } else
            return false;

    }

    public static ArrayList<Todo> isToday(List<Todo> todos) {

        timeInit();
        ArrayList<Todo> tempList=new ArrayList<>();

        for (Todo todo:todos) {

            int todoDay = todo.getDay();
            int todoMonth = todo.getMonth();
            int todoYear = todo.getYear();
            if (todoYear == year) {
                if (todoMonth == month) {
                    if (todoDay == day)
                        tempList.add(todo);
                }
            }
        }
        return tempList;
    }

    public static void timeInit() {

        SimpleDateFormat format = new SimpleDateFormat("dd");
        Date date = new Date();
        day = Integer.parseInt(format.format(date));
        format = new SimpleDateFormat("MM");
        month = Integer.parseInt(format.format(date));
        format = new SimpleDateFormat("yyyy");
        year = Integer.parseInt(format.format(date));

    }


}
