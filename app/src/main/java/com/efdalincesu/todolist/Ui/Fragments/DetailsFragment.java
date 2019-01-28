package com.efdalincesu.todolist.Ui.Fragments;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.efdalincesu.todolist.DBSqlite.DatabaseUtil;
import com.efdalincesu.todolist.Model.Todo;
import com.efdalincesu.todolist.R;
import com.efdalincesu.todolist.Ui.MainActivity;
import com.efdalincesu.todolist.Utils.Util;

public class DetailsFragment extends Fragment implements TextWatcher, View.OnClickListener {

    Todo todo;
    TextView titleTV, summaryTV, dateTV, dateNowTV, reminderTV;
    EditText titleET, summaryET, dateNowET;
    Button deleteTodo;
    CardView cardDate, cardReminder;
    DatabaseUtil databaseUtil;
    Switch todoState;

    String dateString = null;
    String timeString = "";
    String reminderString = null;

    public static DetailsFragment getFragment(Todo todo) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("todo", todo);


        DetailsFragment fragment = new DetailsFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    public void readBundle(Bundle bundle) {

        if (bundle != null)
            todo = (Todo) bundle.getSerializable("todo");

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.todo_details_fragment, container, false);
        databaseUtil = new DatabaseUtil(container.getContext());
        titleTV = view.findViewById(R.id.titleTV);
        summaryTV = view.findViewById(R.id.summaryTV);
        dateTV = view.findViewById(R.id.dateTV);
        dateNowTV = view.findViewById(R.id.dateNowTV);
        reminderTV = view.findViewById(R.id.reminderTV);
        titleET = view.findViewById(R.id.titleET);
        summaryET = view.findViewById(R.id.summaryET);
        dateNowET = view.findViewById(R.id.dateNowET);
        deleteTodo = view.findViewById(R.id.deleteBt);
        cardDate = view.findViewById(R.id.card_date);
        cardReminder = view.findViewById(R.id.card_reminder);
        todoState = view.findViewById(R.id.todoState);

        readBundle(getArguments());


        titleET.setText(todo.getTitle());
        summaryET.setText(todo.getSummary());
        dateTV.setText((todo.getDate() == null ? "Tarihi Belirsiz" : todo.getDate()));
        reminderTV.setText(todo.getReminder() == null ? "Kapalı" : todo.getReminder());
        dateNowET.setText(todo.getDatenow());
        todoState.setChecked(todo.isStatus());

        deleteTodo.setOnClickListener(this);
        cardDate.setOnClickListener(this);
        cardReminder.setOnClickListener(this);

        titleET.addTextChangedListener(this);
        summaryET.addTextChangedListener(this);
        return view;
    }

    public void updateDate() {
        if (dateString != null) {
            if (timeString.equals(""))
                dateString = dateString + " " + todo.getTime();

            todo.setDate(dateString);
            databaseUtil.updateTodo(todo);
        }
    }

    public void updateReminder() {
        todo.setReminder(reminderString);
        databaseUtil.updateTodo(todo);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        todo.setTitle(titleET.getText().toString());
        todo.setSummary(summaryET.getText().toString());
        databaseUtil.updateTodo(todo);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MainActivity.refreshAdapter();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.card_date:
                showTodoDatePicker();
                break;

            case R.id.card_reminder:
                showTodoReminderPicker();
                break;

            case R.id.deleteBt:
                databaseUtil.deleteTodo(todo.getId());
                MainActivity.draggableView.setVisibility(View.GONE);
                onDestroy();
                break;
        }
    }

    private void showTodoReminderPicker() {
        dateString = null;
        timeString = "";

        final TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String timeString = hourOfDay + ":" + Util.format(minute);
                reminderString = reminderString + " " + timeString;
                reminderTV.setText(reminderString);
                updateReminder();
            }
        }, 13, 30, true);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final DatePicker picker = new DatePicker(getContext());
        picker.setMinDate(System.currentTimeMillis() - 1000);
        builder.setView(picker);
        builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reminderString = picker.getYear() + "-" + Util.format(picker.getMonth() + 1) + "-" + Util.format(picker.getDayOfMonth());
                reminderTV.setText(reminderString);
                dialog.dismiss();
                timePickerDialog.show();
            }
        });
        builder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNeutralButton("Hatırlatıcıyı Kapat", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reminderString = null;
                reminderTV.setText("Kapalı");
                updateReminder();
            }
        });
        builder.create().show();
    }

    private void showTodoDatePicker() {

        dateString = null;
        timeString = "";
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final DatePicker picker = new DatePicker(getContext());
        picker.setMinDate(System.currentTimeMillis() - 1000);
        builder.setView(picker);
        builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dateString = picker.getYear() + "-" + Util.format(picker.getMonth() + 1) + "-" + Util.format(picker.getDayOfMonth());
                dateTV.setText(dateString);
                dialog.dismiss();
                updateDate();
            }
        });
        builder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNeutralButton("Saat Seç", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dateString = picker.getYear() + "-" + Util.format(picker.getMonth() + 1) + "-" + Util.format(picker.getDayOfMonth());
                dateTV.setText(dateString);

                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeString = hourOfDay + ":" + Util.format(minute);
                        dateString = dateString + " " + timeString;
                        dateTV.setText(dateString);
                        updateDate();
                    }
                }, 13, 30, true);

                timePickerDialog.show();
            }
        });
        builder.create().show();


    }
}
