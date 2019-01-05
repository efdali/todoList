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

public class DetailsFragment extends Fragment implements TextWatcher {

    Todo todo;
    TextView titleTV, summaryTV, dateTV, dateNowTV, hatirlaticiTV;
    EditText titleET, summaryET, dateNowET;
    Button deleteTodo;
    CardView cardDate, cardHatirlatici;
    DatabaseUtil databaseUtil;
    Switch todoState;

    String dateString = null;
    String timeString = "";

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
        titleET = view.findViewById(R.id.titleET);
        summaryET = view.findViewById(R.id.summaryET);
        dateNowET = view.findViewById(R.id.dateNowET);
        deleteTodo = view.findViewById(R.id.deleteBt);
        cardDate = view.findViewById(R.id.card_date);
        cardHatirlatici = view.findViewById(R.id.card_hatirlatici);
        todoState = view.findViewById(R.id.todoState);

        readBundle(getArguments());


        titleET.setText(todo.getTitle());
        summaryET.setText(todo.getSummary());
        dateTV.setText((todo.getDate()==null ? "Tarihi Belirsiz" : todo.getDate()));
        dateNowET.setText(todo.getDatenow());
        todoState.setChecked(todo.isStatus());

        deleteTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseUtil.deleteTodo(todo.getId());
                MainActivity.draggableView.setVisibility(View.GONE);
                onDestroy();
            }
        });

        cardDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                        update();
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
                                update();
                            }
                        }, 13, 30, true);

                        timePickerDialog.show();
                    }
                });
                builder.create().show();
            }
        });

        titleET.addTextChangedListener(this);
        summaryET.addTextChangedListener(this);
        return view;
    }

    public void update() {
        if (dateString != null) {
            if (timeString.equals(""))
                dateString = dateString + " " + todo.getTime();

            todo.setDate(dateString);
            databaseUtil.updateTodo(todo);
        }
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
}
