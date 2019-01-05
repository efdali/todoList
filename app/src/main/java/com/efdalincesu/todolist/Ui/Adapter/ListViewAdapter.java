package com.efdalincesu.todolist.Ui.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.efdalincesu.todolist.Utils.CustomClick;
import com.efdalincesu.todolist.Model.Todo;
import com.efdalincesu.todolist.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListViewAdapter extends BaseAdapter implements Filterable {

    List<Todo> todos;
    List<Todo> filteredList;
    SimpleDateFormat format;
    Date date;
    int day, month, year;
    Todo todo, lastTodo;
    int dateNumber = 0, pastNumber = 0;


    CustomClick click;

    public ListViewAdapter(List<Todo> todos,CustomClick click) {
        this.todos = todos;
        filteredList=todos;
        format = new SimpleDateFormat("dd");
        date = new Date();
        day = Integer.parseInt(format.format(date));
        format = new SimpleDateFormat("MM");
        month = Integer.parseInt(format.format(date));
        format = new SimpleDateFormat("yyyy");
        year = Integer.parseInt(format.format(date));

        this.click=click;
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return filteredList.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (position != 0) {
            todo = filteredList.get(position);
            lastTodo = filteredList.get(position - 1);
        } else {
            todo = filteredList.get(position);
            lastTodo = null;
        }

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        if (todo.getDate() == null) {

            viewHolder.title.setText(todo.getTitle() + " null " + todo.getDate());
            if (dateNumber == 0) {
                viewHolder.day.setVisibility(View.VISIBLE);
                viewHolder.day.setText("Tarihi Belirsiz");
                dateNumber++;
            }

        } else if (isPast(todo)) {
            viewHolder.title.setText(todo.getTitle() + " " + todo.getDate());
            if (pastNumber == 0) {
                viewHolder.day.setVisibility(View.VISIBLE);
                viewHolder.day.setText("Geçmiş TODO's");
                pastNumber++;
            }
        } else {

            if (lastTodo != null) {
                viewHolder.title.setText(todo.getTitle());
                if (!isTrue(todo.getDay(), todo.getMonth(), lastTodo.getDay(), lastTodo.getMonth())) {
                    viewHolder.day.setVisibility(View.VISIBLE);
                    viewHolder.day.setText(getText(todo.getDay(), todo.getMonth(), todo.getYear()));
                }


            } else {
                viewHolder.day.setVisibility(View.VISIBLE);
                viewHolder.day.setText("last todo null");
                viewHolder.title.setText(todo.getTitle());

            }


        }

        viewHolder.todo_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.click(filteredList.get(position));
            }
        });


        return convertView;
    }


    public String getText(int todoDay, int todoMonth, int todoYear) {

        String dayS = "";
        if (todoYear == year) {
            if (todoMonth == month) {
                if (todoDay == day) {
                    dayS = "Bugün";
                } else if (todoDay == day + 1) {
                    dayS = "Yarın";
                } else {
                    dayS = todoDay + "." + todoMonth + "." + todoYear;
                }
            } else if (todoMonth == month + 1) {
                dayS = "Sonraki Ay";
            } else if (todoMonth > month + 1) {
                dayS = "Sonraki Aylar";
            }
        } else {
            dayS = todoYear + " Yılı";
        }

        return dayS;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String search=constraint.toString();

                if (search.isEmpty())
                    filteredList=todos;
                else{

                    ArrayList<Todo> tempList=new ArrayList<>();

                    for (Todo todo:todos){
                        if (todo.getTitle().contains(search))
                            tempList.add(todo);
                    }

                    filteredList=tempList;
                }

                FilterResults results=new FilterResults();
                results.values=filteredList;

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                    filteredList= (List<Todo>) results.values;
                    notifyDataSetChanged();
            }
        };
    }

    private class ViewHolder {

        TextView day, title;
        LinearLayout todo_container;

        public ViewHolder(View view) {

            day = view.findViewById(R.id.day);
            title = view.findViewById(R.id.todoTitle);
            todo_container=view.findViewById(R.id.todo_container);
        }

    }

    public boolean isPast(Todo todo) {

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


    public boolean isTrue(int day1, int month1, int day2, int month2) {

        return day1 == day2 && month1 == month2;
    }


}
/*
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
 */