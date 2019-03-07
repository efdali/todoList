package com.efdalincesu.todolist.DBSqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.efdalincesu.todolist.Model.Todo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DatabaseUtil {

    DBHelper dbHelper;
    SQLiteDatabase database;

    public DatabaseUtil(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void closeConnect(){
        dbHelper.close();
    }

    public ArrayList<Todo> selectTodosASC(){

        ArrayList<Todo> todos=new ArrayList<>();
        database=dbHelper.getReadableDatabase();
        Cursor cursor=database.query(DBHelper.TABLE_NAME,null,null,null,null,null,
                "datetime   ("+DBHelper.DATE_COLUMN+") ASC");

        while (cursor.moveToNext()){
            int id=cursor.getInt(0);
            String title=cursor.getString(1);
            String summary=cursor.getString(2);
            String datenow=cursor.getString(3);
            String date=cursor.getString(4);
            String reminder=cursor.getString(5);
            int statusInt=cursor.getInt(6);
            boolean status;

            if (statusInt==0){
                status=false;
            }else{
                status=true;
            }


            Todo todo=new Todo(id,title,summary,datenow,date,reminder,status);
            todos.add(todo);
        }

        return todos;
    }

    public Todo selectTodo(String todoId){

        String selection=DBHelper.ID_COLUMN + " = ? ";
        database=dbHelper.getReadableDatabase();
        Cursor cursor=database.query(DBHelper.TABLE_NAME,null,selection,new String[]{todoId},null,null,
                null);

        int id=cursor.getInt(0);
        String title=cursor.getString(1);
        String summary=cursor.getString(2);
        String datenow=cursor.getString(3);
        String date=cursor.getString(4);
        String reminder=cursor.getString(5);
        int statusInt=cursor.getInt(6);
        boolean status;
        if (statusInt==0){
            status=false;
        }else{
            status=true;
        }

        Todo todo=new Todo(id,title,summary,datenow,date,reminder,status);

        return todo;
    }

    public boolean insertTodo(Todo todo) {

        database = dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(DBHelper.TITLE_COLUMN,todo.getTitle());
        values.put(DBHelper.SUMMARY_COLUMN,todo.getSummary());
        values.put(DBHelper.DATENOW_COLUMN,todo.getDatenow());
        values.put(DBHelper.DATE_COLUMN,todo.getDate());
        values.put(DBHelper.REMINDER_COLUMN,todo.getReminder());
        values.put(DBHelper.STATUS_COLUMN,todo.isStatus());

        long value=database.insert(DBHelper.TABLE_NAME,null,values);

        return value>0;
    }

    public int updateTodo(Todo todo){

        database=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(DBHelper.TITLE_COLUMN,todo.getTitle());
        values.put(DBHelper.SUMMARY_COLUMN,todo.getSummary());
        values.put(DBHelper.DATENOW_COLUMN,todo.getDatenow());
        values.put(DBHelper.DATE_COLUMN,todo.getDate());
        values.put(DBHelper.REMINDER_COLUMN,todo.getReminder());
        values.put(DBHelper.STATUS_COLUMN,todo.isStatus());


        String selection=DBHelper.ID_COLUMN + " = ? ";
        String[] selectionArgs={todo.getId()+""};

        int count=database.update(DBHelper.TABLE_NAME,values,selection,selectionArgs);

        return count;
    }

    public int deleteTodo(int todoId){

        database=dbHelper.getWritableDatabase();
        String selection=DBHelper.ID_COLUMN + " = ? ";
        String[] selectionArgs={todoId+""};

        int deletedRows=database.delete(DBHelper.TABLE_NAME,selection,selectionArgs);

        return deletedRows;
    }

    public int deleteAllTodo(){
        database=dbHelper.getWritableDatabase();
        int deletedRows=database.delete(DBHelper.TABLE_NAME,null,null);

        return deletedRows;
    }

    public int getRowCount(){

        database=dbHelper.getReadableDatabase();
        String query="Select COUNT(*) from " + DBHelper.TABLE_NAME;
        Cursor cursor=database.rawQuery(query,null);
        cursor.moveToFirst();
        int count=cursor.getInt(0);
        cursor.close();
        return count;
    }

    public int deleteOldTodo(){

        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        String date=format.format(calendar.getTime());

        database=dbHelper.getWritableDatabase();
        String selection=DBHelper.DATE_COLUMN + " < ? ";
        String[] selectionArgs={date};

        int deletedRows=database.delete(DBHelper.TABLE_NAME,selection,selectionArgs);

        return deletedRows;
    }

    public ArrayList<Todo> selectTodosToday() {

        ArrayList<Todo> todos=new ArrayList<>();
        database=dbHelper.getReadableDatabase();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateFormat = format.format(new Date());
        String selection = DBHelper.DATE_COLUMN + " LIKE ?";
        String[] selectionArgs = {dateFormat + "%"};
        Cursor cursor = database.query(DBHelper.TABLE_NAME, null, selection, selectionArgs, null, null,
                null);

        while (cursor.moveToNext()){
            int id=cursor.getInt(0);
            String title=cursor.getString(1);
            String summary=cursor.getString(2);
            String datenow=cursor.getString(3);
            String date=cursor.getString(4);
            String reminder=cursor.getString(5);
            int statusInt=cursor.getInt(6);
            boolean status;

            if (statusInt==0){
                status=false;
            }else{
                status=true;
            }


            Todo todo=new Todo(id,title,summary,datenow,date,reminder,status);
            todos.add(todo);
        }

        return todos;
    }



}
