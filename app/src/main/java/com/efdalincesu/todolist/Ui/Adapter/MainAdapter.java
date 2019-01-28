package com.efdalincesu.todolist.Ui.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.efdalincesu.todolist.DBSqlite.DatabaseUtil;
import com.efdalincesu.todolist.Model.Todo;
import com.efdalincesu.todolist.R;
import com.efdalincesu.todolist.Utils.CustomClick;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> implements Filterable {

    List<Todo> todos;
    List<Todo> filteredList;
    CustomClick customClick;
    Context context;

    public MainAdapter(List<Todo> todos, CustomClick customClick) {
        this.todos = todos;
        this.filteredList = todos;
        this.customClick = customClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.row_layout, viewGroup, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {

        final DatabaseUtil db = new DatabaseUtil(context);
        final Todo todo = filteredList.get(position);

        viewHolder.title.setText(todo.getTitle());

        viewHolder.todoContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customClick.click(filteredList.get(position));
            }
        });

        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                viewHolder.deleteBt.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                viewHolder.title.setPaintFlags(isChecked ? Paint.STRIKE_THRU_TEXT_FLAG : 0);
            }
        });

        viewHolder.deleteBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteTodo(todo.getId());
                filteredList.remove(todo);
                notifyItemRemoved(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout todoContainer;
        TextView title;
        CheckBox checkBox;
        ImageButton deleteBt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            todoContainer = itemView.findViewById(R.id.todo_container);
            title = itemView.findViewById(R.id.todoTitle);
            checkBox = itemView.findViewById(R.id.checkBox);
            deleteBt = itemView.findViewById(R.id.deleteB);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String search = constraint.toString();

                if (search.isEmpty())
                    filteredList = todos;
                else {

                    ArrayList<Todo> tempList = new ArrayList<>();

                    for (Todo todo : todos) {
                        if (todo.getTitle().contains(search))
                            tempList.add(todo);
                    }

                    filteredList = tempList;
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (List<Todo>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
