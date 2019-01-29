package com.efdalincesu.todolist.Ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.efdalincesu.todolist.DBSqlite.DatabaseUtil;
import com.efdalincesu.todolist.Model.Todo;
import com.efdalincesu.todolist.R;
import com.efdalincesu.todolist.Services.AlarmManagerService;
import com.efdalincesu.todolist.Ui.Adapter.MainAdapter;
import com.efdalincesu.todolist.Ui.Fragments.DetailsFragment;
import com.efdalincesu.todolist.Utils.CustomClick;
import com.efdalincesu.todolist.Utils.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener, CustomClick {

    public static final String FRAGMENT_TAG = "DetailsFragment";
    public static final String PAST_TODO_KEY = "pref_past";


    Display display;
    int width;
    float dY;
    static DatabaseUtil db;
    Calendar calendar = Calendar.getInstance();

    int day = calendar.get(Calendar.DAY_OF_MONTH);
    int month = calendar.get(Calendar.MONTH);
    int year = calendar.get(Calendar.YEAR);
    String timeString, dateString;

    Toolbar toolbar;
    public static CardView draggableView;
    EditText todoEt;
    RecyclerView belirsizRec, todayRec, tomorrowRec, pastRec, futureRec;
    ImageButton addB, timeB, dateB, ok1, ok2, ok3, ok4, ok5;
    RelativeLayout dateBaseLinear;
    TextView timeText, dateText;
    LinearLayout timeLinear, dateLinear;
    RelativeLayout container1, container2, container3, container4, container5;
    public static List<Todo> belirsizTodos, todayTodos, pastTodos, futureTodos, tomorrowTodos;


    public static MainAdapter belirsizAdap, todayAdap, tomorrowAdap, pastAdap, futureAdap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, AlarmManagerService.class));
        display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        db = new DatabaseUtil(this);
        initPreferences();
        initViews();
        initVar();
        initTodos();


        belirsizAdap = new MainAdapter(belirsizTodos, this);
        belirsizRec.setAdapter(belirsizAdap);

        todayAdap = new MainAdapter(todayTodos, this);
        todayRec.setAdapter(todayAdap);

        tomorrowAdap = new MainAdapter(tomorrowTodos, this);
        tomorrowRec.setAdapter(tomorrowAdap);

        pastAdap = new MainAdapter(pastTodos, this);
        pastRec.setAdapter(pastAdap);

        futureAdap = new MainAdapter(futureTodos, this);
        futureRec.setAdapter(futureAdap);


    }

    public void initPreferences() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int deletedRows = 0;
        if (preferences.getBoolean(PAST_TODO_KEY, false)) {
            deletedRows = db.deleteOldTodo();
        }

        if (deletedRows > 0) {
            Toast.makeText(getApplicationContext(), deletedRows + " adet geçmiş todo silindi." +
                            "Silinmesini istemiyorsanız ayarlardan kapatabilirsiniz."
                    , Toast.LENGTH_LONG).show();
        }


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
        container1 = findViewById(R.id.container1);
        container2 = findViewById(R.id.container2);
        container3 = findViewById(R.id.container3);
        container4 = findViewById(R.id.container4);
        container5 = findViewById(R.id.container5);
        ok1 = findViewById(R.id.ok1);
        ok2 = findViewById(R.id.ok2);
        ok3 = findViewById(R.id.ok3);
        ok4 = findViewById(R.id.ok4);
        ok5 = findViewById(R.id.ok5);


        belirsizRec = findViewById(R.id.belirsizRec);
        todayRec = findViewById(R.id.todayRec);
        tomorrowRec = findViewById(R.id.tommorowRec);
        pastRec = findViewById(R.id.pastRec);
        futureRec = findViewById(R.id.futureRec);
        belirsizRec.setLayoutManager(new LinearLayoutManager(this));
        belirsizRec.setHasFixedSize(true);
        todayRec.setLayoutManager(new LinearLayoutManager(this));
        todayRec.setHasFixedSize(true);
        tomorrowRec.setLayoutManager(new LinearLayoutManager(this));
        tomorrowRec.setHasFixedSize(true);
        pastRec.setLayoutManager(new LinearLayoutManager(this));
        pastRec.setHasFixedSize(true);
        futureRec.setLayoutManager(new LinearLayoutManager(this));
        futureRec.setHasFixedSize(true);
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
        container1.setOnClickListener(this);
        container2.setOnClickListener(this);
        container3.setOnClickListener(this);
        container4.setOnClickListener(this);
        container5.setOnClickListener(this);

        //init List
        belirsizTodos = new ArrayList<>();
        todayTodos = new ArrayList<>();
        tomorrowTodos = new ArrayList<>();
        pastTodos = new ArrayList<>();
        futureTodos = new ArrayList<>();
    }

    public void initVar() {

        timeString = "";
        dateString = null;
        timeText.setText("");
        dateText.setText("");

    }

    public static void refreshAdapter() {
        initTodos();

        belirsizAdap.notifyDataSetChanged();
        todayAdap.notifyDataSetChanged();
        tomorrowAdap.notifyDataSetChanged();
        pastAdap.notifyDataSetChanged();
        futureAdap.notifyDataSetChanged();
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
                belirsizAdap.getFilter().filter(newText);
                return true;
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }
        return true;
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

                } else if (y > width + draggableView.getHeight() / 5) {
                    draggableView.setVisibility(View.GONE);
                    getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG).onDestroy();
                } else
                    draggableView.setY(y);
                break;

            default:
                return false;
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
                    String todoTitle = todoEt.getText().toString().trim();
                    String todoDate = dateString + " " + timeString;
                    if (dateString == null)
                        db.insertTodo(new Todo(todoTitle));
                    else
                        db.insertTodo(new Todo(todoTitle, todoDate));
                    refreshAdapter();
                    todoEt.clearFocus();
                    todoEt.setText("");
                    initVar();
                }
                break;
            case R.id.timeLinear:

                TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeString = hourOfDay + ":" + Util.format(minute);
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
                        dateString = year + "-" + Util.format(month + 1) + "-" + Util.format(dayOfMonth);
                        dateText.setText(dateString);
                    }
                }, year, month, day);
//                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
                break;
            case R.id.container1:
                if (belirsizRec.getVisibility() == View.VISIBLE) {
                    belirsizRec.setVisibility(View.GONE);
                    ok1.setBackgroundResource(R.drawable.ic_arrow_up);
                } else {
                    belirsizRec.setVisibility(View.VISIBLE);
                    ok1.setBackgroundResource(R.drawable.ic_arrow_down);
                }
                break;

            case R.id.container2:
                if (todayRec.getVisibility() == View.VISIBLE) {
                    todayRec.setVisibility(View.GONE);
                    ok2.setBackgroundResource(R.drawable.ic_arrow_up);
                } else {
                    todayRec.setVisibility(View.VISIBLE);
                    ok2.setBackgroundResource(R.drawable.ic_arrow_down);
                }
                break;

            case R.id.container3:
                if (tomorrowRec.getVisibility() == View.VISIBLE) {
                    tomorrowRec.setVisibility(View.GONE);
                    ok3.setBackgroundResource(R.drawable.ic_arrow_up);
                } else {
                    tomorrowRec.setVisibility(View.VISIBLE);
                    ok3.setBackgroundResource(R.drawable.ic_arrow_down);
                }
                break;

            case R.id.container4:
                if (pastRec.getVisibility() == View.VISIBLE) {
                    pastRec.setVisibility(View.GONE);
                    ok4.setBackgroundResource(R.drawable.ic_arrow_up);
                } else {
                    pastRec.setVisibility(View.VISIBLE);
                    ok4.setBackgroundResource(R.drawable.ic_arrow_down);
                }
                break;

            case R.id.container5:
                if (futureRec.getVisibility() == View.VISIBLE) {
                    futureRec.setVisibility(View.GONE);
                    ok5.setBackgroundResource(R.drawable.ic_arrow_up);
                } else {
                    futureRec.setVisibility(View.VISIBLE);
                    ok5.setBackgroundResource(R.drawable.ic_arrow_down);
                }
                break;
        }
    }

    @Override
    public void click(Todo todo) {
        draggableView.setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction().replace(R.id.draggable_view, DetailsFragment.getFragment(todo), FRAGMENT_TAG).commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshAdapter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.closeConnect();
    }


    public static void initTodos() {

        List<Todo> todos = db.selectTodosASC();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        String date = format.format(calendar.getTime());
        calendar.add(Calendar.DATE, +1);
        String dateT = format.format(calendar.getTime());

        Log.d("tarih",date+" "+dateT);
        belirsizTodos.clear();
        todayTodos.clear();
        tomorrowTodos.clear();
        pastTodos.clear();
        futureTodos.clear();

        for (Todo todo : todos) {
            if (todo.getDate() == null) {
                belirsizTodos.add(todo);
            } else if (todo.getDate().contains(date)) {
                todayTodos.add(todo);
            } else if (todo.getDate().contains(dateT)) {
                tomorrowTodos.add(todo);
            } else if (Util.isPast(todo)) {
                pastTodos.add(todo);
            } else {
                futureTodos.add(todo);
            }

        }


    }
}
