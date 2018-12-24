package com.efdalincesu.todolist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.efdalincesu.todolist.DBSqlite.DatabaseUtil;
import com.efdalincesu.todolist.Model.Todo;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    DatabaseUtil db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db=new DatabaseUtil(this);
        boolean durum=db.insertTodo(new Todo("Deneme","Bu Bir Deneme Todo'sudur.","24.12.2018 20:40:18"));
        TextView textView=findViewById(R.id.text);
        Toast.makeText(this,durum+" durum",Toast.LENGTH_SHORT).show();

        List<Todo> todos=db.selectTodos();
        Toast.makeText(this,todos.size()+" size",Toast.LENGTH_LONG).show();
        String message="";

        for (Todo todo : todos){
            message+=todos.toString()+"\n";
        }

        textView.setText(message);

    }
}
