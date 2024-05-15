package com.android.todos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.MyViewHolder>{
    private Context context;
    private List<Todo> todos;

    private onClickItemListener listener;

    public TodoAdapter(Context context, List<Todo> todos, onClickItemListener listener) {
        this.context = context;
        this.todos = todos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(context).inflate(R.layout.item_to_do, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Todo todo = todos.get(position);
        holder.id.setText(String.valueOf(todo.getId()));
        holder.title.setText(String.valueOf(todo.getTitle()));
        holder.status.setText(String.valueOf(todo.getCompleted()));

        holder.itemView.setOnClickListener(v -> {
            listener.onClickItem(todo);
        });

        holder.itemView.setOnLongClickListener(v -> {
            listener.onLongClickItem(todo);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView id, status, title;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.todoID);
            title = itemView.findViewById(R.id.todoTitle);
            status = itemView.findViewById(R.id.status);
        }
    }

    public interface onClickItemListener{
        void onClickItem(Todo todo);
        void onLongClickItem(Todo todo);
    }
}
