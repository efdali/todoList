package com.efdalincesu.todolist.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.efdalincesu.todolist.Model.Todo;
import com.efdalincesu.todolist.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ListViewAdapter extends BaseAdapter {

    List<Todo> todos;
    SimpleDateFormat format;
    Date date;
    int day, month, year;
    Todo todo, lastTodo;
    int number = 1;

    public ListViewAdapter(List<Todo> todos) {
        this.todos = todos;
        format = new SimpleDateFormat("dd");
        date = new Date();
        day = Integer.parseInt(format.format(date));
        format = new SimpleDateFormat("MM");
        month = Integer.parseInt(format.format(date));
        format = new SimpleDateFormat("yyyy");
        year = Integer.parseInt(format.format(date));
    }

    @Override
    public int getCount() {
        return todos.size();
    }

    @Override
    public Object getItem(int position) {
        return todos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return todos.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (position != 0) {
            todo = todos.get(position);
            lastTodo = todos.get(position - 1);


        } else {
            todo = todos.get(position);
            lastTodo = null;
        }

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (lastTodo != null) {
            if (todo.getDate() != null && lastTodo.getDate() != null) {
                boolean durum = isTrue(todo.getDay(), todo.getMonth(), lastTodo.getDay(), todo.getMonth());
                if (durum) {
                    viewHolder.day.setVisibility(View.GONE);
                    viewHolder.title.setText(todo.getTitle() + " " + todo.getDate());
                } else {
                    viewHolder.day.setVisibility(View.VISIBLE);
                    viewHolder.day.setText(getText(todo.getDay(), todo.getMonth(), todo.getYear()));
                    viewHolder.title.setText(todo.getTitle() + " " + todo.getDate());
                }
            } else if (todo.getDate() != null && lastTodo.getDate() == null) {
                viewHolder.day.setVisibility(View.VISIBLE);
                viewHolder.day.setText(getText(todo.getDay(), todo.getMonth(), todo.getYear()));
                viewHolder.title.setText(todo.getTitle() + " " + todo.getDate());
            } else {
                if (number == 0) {
                    viewHolder.day.setText("Tarihi Belirsiz");
                    viewHolder.title.setText(todo.getTitle() + " " + todo.getDate());
                    number++;
                } else {
                    viewHolder.day.setVisibility(View.GONE);
                    viewHolder.title.setText(todo.getTitle() + " " + todo.getDate());
                }

            }

        } else {
            if (todo.getDate() == null) {
                viewHolder.day.setText("Tarihi Belirsiz");
                viewHolder.title.setText(todo.getTitle() + " " + todo.getDate());
                number++;
            } else {
                viewHolder.day.setText(getText(todo.getDay(), todo.getMonth(), todo.getYear()));
                viewHolder.title.setText(todo.getTitle() + " " + todo.getDate());
            }
        }


        return convertView;
    }


    public String getText(int todoDay, int todoMonth, int todoYear) {

        String dayS;
        if (todoYear == year) {
            if (todoMonth == month) {
                if (todoDay == day) {
                    dayS = "Bugün";
                } else if (todoDay == day + 1) {
                    dayS = "Yarın";
                } else if (todoDay < day) {
                    dayS = "Geçmiş " + todoDay + "." + todoMonth;
                } else {
                    dayS = "Sonraki Günler " + todoDay + "." + todoMonth;
                }
            } else if (todoMonth == month + 1) {
                dayS = "Sonraki Ay";
            } else if (todoMonth > month + 1) {
                dayS = "Sonraki Aylar";
            } else {
                dayS = "Geçmiş Ay";
            }
        } else if (todoYear < year) {
            dayS = "Geçmiş Yıl";
        } else {
            dayS = "Gelecek Yıl";
        }

        return dayS;
    }

    private class ViewHolder {

        TextView day, title;

        public ViewHolder(View view) {

            day = view.findViewById(R.id.day);
            title = view.findViewById(R.id.todoTitle);
        }

    }

    public boolean isTrue(int day1, int month1, int day2, int month2) {

        return day1 == day2 && month1 == month2;
    }


}
