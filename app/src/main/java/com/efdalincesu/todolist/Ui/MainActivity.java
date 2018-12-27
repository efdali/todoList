package com.efdalincesu.todolist.Ui;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.efdalincesu.todolist.Adapter.ListViewAdapter;
import com.efdalincesu.todolist.DBSqlite.DatabaseUtil;
import com.efdalincesu.todolist.Model.Todo;
import com.efdalincesu.todolist.R;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {

    Display display;
    int width;
    float dY;
    DatabaseUtil db;
    Calendar calendar = Calendar.getInstance();

    int day = calendar.get(Calendar.DAY_OF_MONTH);
    int month = calendar.get(Calendar.MONTH);
    int year = calendar.get(Calendar.YEAR);
    String timeString, dateString;

    Toolbar toolbar;
    RelativeLayout draggableView;
    EditText todoEt;
    ImageButton addB, timeB, dateB;
    RelativeLayout dateBaseLinear;
    TextView timeText, dateText;
    LinearLayout timeLinear, dateLinear;
    ListView listView;
    ListViewAdapter adapter;
    List<Todo> todos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        db = new DatabaseUtil(this);
        initViews();
        initVar();

        todos = db.selectTodosASC();
        adapter = new ListViewAdapter(todos);
        listView.setAdapter(adapter);


    }

    public void initViews() {


        //View init
        draggableView = findViewById(R.id.draggable_view);
        todoEt = findViewById(R.id.todoEdittext);
        addB = findViewById(R.id.addButton);
        timeB = findViewById(R.id.timeButton);
        dateB = findViewById(R.id.dateButton);
        dateBaseLinear = findViewById(R.id.dateBaseLinear);
        toolbar = findViewById(R.id.toolbar);
        timeText = findViewById(R.id.timeText);
        dateText = findViewById(R.id.dateText);
        dateText.setText(dateString);
        timeLinear = findViewById(R.id.timeLinear);
        dateLinear = findViewById(R.id.dateLinear);
        listView = findViewById(R.id.listView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tüm Görevler");


        //View Listeners
        draggableView.setOnTouchListener(this);

        todoEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                dateBaseLinear.setVisibility(hasFocus ? View.VISIBLE : View.GONE);
                addB.setBackgroundResource(hasFocus ? R.drawable.ic_up : R.drawable.ic_add);
            }
        });

        addB.setOnClickListener(this);
        timeLinear.setOnClickListener(this);
        dateLinear.setOnClickListener(this);
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
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = null;

        if (searchItem != null)
            searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(getApplicationContext(), newText + " text", Toast.LENGTH_SHORT).show();
                return true;
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.sign:
                Toast.makeText(this, "Sign", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.addButton:
                if (todoEt.getText().toString().trim().isEmpty()) {
                    todoEt.requestFocus();
                } else {
                    String todoTitle = todoEt.getText().toString();
                    String todoDate = dateString + " " + timeString;
                    if (dateString == null)
                        db.insertTodo(new Todo(todoTitle, " "));
                    else
                        db.insertTodo(new Todo(todoTitle, " ", todoDate));
                    todos.clear();
                    todos.addAll(db.selectTodosASC());
                    adapter.notifyDataSetChanged();
                    todoEt.clearFocus();
                    todoEt.setText("");
                    initVar();
                }
                break;
            case R.id.timeLinear:

                TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeString = hourOfDay + ":" + format(minute);
                        timeText.setText(timeString);
                        if (dateString == null)
                            dateLinear.performClick();
                    }
                }, 13, 30, true);

                timePickerDialog.show();

                break;
            case R.id.dateLinear:
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateString = year + "-" + format(month + 1) + "-" + format(dayOfMonth);
                        dateText.setText(dateString);
                    }
                }, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.closeConnect();
    }


    public void initVar() {

        timeString = "";
        dateString = null;
        timeText.setText("");
        dateText.setText("");

    }


    public String format(int n) {


        return n < 10 ? "0" + n : n + "";
    }
}
