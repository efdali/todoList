package com.efdalincesu.todolist.Ui;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.efdalincesu.todolist.DBSqlite.DatabaseUtil;
import com.efdalincesu.todolist.R;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {

    Display display;
    int width;
    float dY;
    DatabaseUtil db;
    Toolbar toolbar;
    RelativeLayout draggableView;
    EditText todoEt;
    ImageButton addB;
    Button timeB,dateB;
    RelativeLayout dateLinear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();


    }

    public void init() {

        display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        db = new DatabaseUtil(this);
        draggableView = findViewById(R.id.draggable_view);
        todoEt = findViewById(R.id.todoEdittext);
        addB = findViewById(R.id.addButton);
        timeB=findViewById(R.id.timeButton);
        dateB=findViewById(R.id.dateButton);
        dateLinear=findViewById(R.id.dateLinear);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        draggableView.setOnTouchListener(this);
        getSupportActionBar().setTitle("Tüm Görevler");


        todoEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                    dateLinear.setVisibility(hasFocus ? View.VISIBLE : View.GONE);
                    addB.setBackgroundResource(hasFocus ? R.drawable.ic_up : R.drawable.ic_add);
            }
        });

        addB.setOnClickListener(this);
        timeB.setOnClickListener(this);
        dateB.setOnClickListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                dY = draggableView.getY() - event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float y = event.getRawY() + dY;
                if (y < (width / 2)) {

                } else if (y > width + draggableView.getWidth() / 4)
                    draggableView.setVisibility(View.INVISIBLE);
                else
                    draggableView.setY(y);
                break;

            default:
                return false;
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);

        MenuItem searchItem=menu.findItem(R.id.search);
        SearchView searchView=null;

        if (searchItem!=null)
            searchView= (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(getApplicationContext(),newText+" text",Toast.LENGTH_SHORT).show();
                return true;
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        switch (id){
            case R.id.sign:
                Toast.makeText(this,"Sign",Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                Toast.makeText(this,"Settings",Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();

        switch (id){
            case R.id.addButton:
                if (todoEt.getText().toString().trim().isEmpty()) {
                    todoEt.requestFocus();
                }else{
                    todoEt.clearFocus();
                    todoEt.setText("");
                }
                break;
            case R.id.timeButton:

                TimePickerDialog timePickerDialog=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeB.setDrawableT
                    }
                },13,30,true);


                break;
            case R.id.dateButton:

                break;
        }
    }
}
