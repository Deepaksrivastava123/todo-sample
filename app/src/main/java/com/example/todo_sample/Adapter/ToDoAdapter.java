package com.example.todo_sample.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {

    private List<ToDoModel> todoList;
    private MainActivity activity;
    private FirebaseFirestore firestore;

    private TodoAdapterCallback callback;


    public ToDoAdapter(MainActivity mainActivity, List<ToDoModel> todoList, TodoAdapterCallback callback) {
        this.todoList = todoList;
        activity = mainActivity;
        this.callback = callback;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.each_task, parent, false);
        firestore = FirebaseFirestore.getInstance();

        return new MyViewHolder(view);
    }

    public void deleteTask(int position) {
        ToDoModel toDoModel = todoList.get(position);
        firestore.collection("task").document(toDoModel.TaskId).delete();
        todoList.remove(position);
        notifyItemRemoved(position);
    }

    public Context getContext() {
        return activity;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ToDoModel toDoModel = todoList.get(position);
        holder.mCheckBox.setText(toDoModel.getTask());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUp(toDoModel, v.getContext());
            }
        });

        if (toDoModel.getUrl() == null || toDoModel.getUrl() == "") {
            holder.imageView.setImageResource(R.mipmap.ic_launcher);

        } else {
            Glide.with(getContext()).load(Uri.parse(toDoModel.getUrl())).into(holder.imageView);
        }

        if (toDoModel.getDescription() == null || toDoModel.getDescription() == "") {
            holder.mAddDescription.setText("Add Description");
        } else {
            holder.mAddDescription.setText(toDoModel.getDescription());

        }

        holder.mCheckBox.setChecked(toBoolean(toDoModel.getStatus()));


        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    firestore.collection("task").document(toDoModel.getTaskId()).update("status", 1);
                } else {
                    firestore.collection("task").document(toDoModel.getTaskId()).update("status", 0);
                }
            }
        });

        holder.mAddDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddDescriptionActivity.class);
                intent.putExtra("task_id", toDoModel.getTaskId());
                v.getContext().startActivity(intent);
            }
        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toDoModel.getUrl().equals("")) {
                    callback.saveImage(toDoModel.getTaskId());
                }
            }
        });

    }

    private void showPopUp(ToDoModel toDoModel, Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_popup_layout);

        ImageView imageView = dialog.findViewById(R.id.imageView);
        TextView titleTextview = dialog.findViewById(R.id.title_textview);
        TextView descTextview = dialog.findViewById(R.id.description_textview);

        Glide.with(getContext()).load(Uri.parse(toDoModel.getUrl())).into(imageView);
        titleTextview.setText(toDoModel.getTask());
        descTextview.setText(toDoModel.getDescription());

        dialog.show();
    }


    private boolean toBoolean(int status) {
        return status != 0;
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

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

    public interface TodoAdapterCallback {
        void saveImage(String taskId);
        // Add other callback methods if needed
    }

}
