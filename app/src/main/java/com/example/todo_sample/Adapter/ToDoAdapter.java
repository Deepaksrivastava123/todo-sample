package com.example.todo_sample.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.todo_sample.Model.ToDoModel;
import com.example.todo_sample.R;

import com.example.todo_sample.screen.AddDescriptionActivity;
import com.example.todo_sample.screen.MainActivity;
import com.example.todo_sample.screen.TaskDetailActivity;
import com.example.todo_sample.screen.UploadImageActivity;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {

    private List<ToDoModel> todoList;
    private MainActivity activity;
    private FirebaseFirestore firestore;

    public ToDoAdapter(MainActivity mainActivity , List<ToDoModel> todoList){
        this.todoList = todoList;
        activity = mainActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.each_task , parent , false);
        firestore = FirebaseFirestore.getInstance();

        return new MyViewHolder(view);
    }

    public void deleteTask(int position){
        ToDoModel toDoModel = todoList.get(position);
        firestore.collection("task").document(toDoModel.TaskId).delete();
        todoList.remove(position);
        notifyItemRemoved(position);
    }
    public Context getContext(){
        return activity;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ToDoModel toDoModel = todoList.get(position);
        holder.mCheckBox.setText(toDoModel.getTask());

        if(toDoModel.getImageUrl() == null || toDoModel.getImageUrl() == "") {
            holder.imageView.setImageResource(R.mipmap.ic_launcher);

        }else {
            Glide.with(getContext()).load(toDoModel.getImageUrl()).into(holder.imageView);
        }

        if(toDoModel.getDescription() ==null || toDoModel.getDescription() == "") {
            holder.mAddDescription.setText("Add Description");
        }else {
            holder.mAddDescription.setText(toDoModel.getDescription());

        }

        holder.mCheckBox.setChecked(toBoolean(toDoModel.getStatus()));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(v.getContext(), TaskDetailActivity.class);
                intent.putExtra("task_id",toDoModel.TaskId);
                v.getContext().startActivity(intent);
            }
        });

        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    firestore.collection("task").document(toDoModel.TaskId).update("status" , 1);
                }else{
                    firestore.collection("task").document(toDoModel.TaskId).update("status" , 0);
                }
            }
        });

        holder.mAddDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(v.getContext(), AddDescriptionActivity.class);
                intent.putExtra("task_id",toDoModel.TaskId);
                v.getContext().startActivity(intent);
            }
        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(v.getContext(), UploadImageActivity.class);
                intent.putExtra("task_id",toDoModel.TaskId);
                v.getContext().startActivity(intent);
            }
        });

    }

    private boolean toBoolean(int status){
        return status != 0;
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mAddDescription;
        CheckBox mCheckBox;

        ImageView imageView;

        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mAddDescription = itemView.findViewById(R.id.add_description);
            mCheckBox = itemView.findViewById(R.id.mcheckbox);
            imageView = itemView.findViewById(R.id.my_image_view);
            cardView = itemView.findViewById(R.id.cardview);


        }
    }
}
