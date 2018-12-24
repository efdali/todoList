package com.efdalincesu.todolist.DBSqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.efdalincesu.todolist.Model.Todo;

import java.util.ArrayList;

public class DatabaseUtil {

    DBHelper dbHelper;
    SQLiteDatabase database;

    public DatabaseUtil(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void closeConnect(){
        dbHelper.close();
    }

    public ArrayList<Todo> selectTodos(){

        ArrayList<Todo> todos=new ArrayList<>();
        database=dbHelper.getReadableDatabase();
        Cursor cursor=database.query(DBHelper.TABLE_NAME,null,null,null,null,null,null);
        cursor.moveToFirst();

        while (cursor.moveToNext()){
            int id=cursor.getInt(0);
            String title=cursor.getString(1);
            String summary=cursor.getString(2);
            String datenow=cursor.getString(3);
            String date=cursor.getString(4);
            int statusInt=cursor.getInt(5);
            boolean status;

            if (statusInt==0){
                status=false;
            }else{
                status=true;
            }


            Todo todo=new Todo(id,title,summary,datenow,date,status);
            todos.add(todo);
        }

        return todos;
    }

    public Todo selectTodo(String todoId){

        String selection=DBHelper.ID_COLUMN + " = ? ";
        database=dbHelper.getReadableDatabase();
        Cursor cursor=database.query(DBHelper.TABLE_NAME,null,selection,new String[]{todoId},null,null,null);
        cursor.moveToFirst();

        int id=cursor.getInt(0);
        String title=cursor.getString(1);
        String summary=cursor.getString(2);
        String datenow=cursor.getString(3);
        String date=cursor.getString(4);
        int statusInt=cursor.getInt(5);
        boolean status;
        if (statusInt==0){
            status=false;
        }else{
            status=true;
        }

        Todo todo=new Todo(id,title,summary,datenow,date,status);

        return todo;
    }

    public boolean insertTodo(Todo todo) {

        database = dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(DBHelper.TITLE_COLUMN,todo.getTitle());
        values.put(DBHelper.SUMMARY_COLUMN,todo.getSummary());
        values.put(DBHelper.DATENOW_COLUMN,todo.getDatenow());
        values.put(DBHelper.DATE_COLUMN,todo.getDate());
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

    public int getRowCount(){

        database=dbHelper.getReadableDatabase();
        String query="Select COUNT(*) from " + DBHelper.TABLE_NAME;
        Cursor cursor=database.rawQuery(query,null);
        cursor.moveToFirst();
        int count=cursor.getInt(0);
        cursor.close();
        return count;
    }

}
