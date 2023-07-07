package com.example.todo_sample.screen;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.todo_sample.Model.ToDoModel;
import com.example.todo_sample.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;



public class TaskDetailActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView titleTextView,descTextView,dateTextiew;
    private FirebaseFirestore db;
    private DocumentReference docRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        initviews();
        showData();
    }

    private void showData() {


        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Retrieve the data from the documentSnapshot and parse it into your model class
                    ToDoModel toDoModel = documentSnapshot.toObject(ToDoModel.class);

                    if(toDoModel!=null) {
                        Glide.with(TaskDetailActivity.this).load(toDoModel.getImageUrl()).into(imageView);
                        titleTextView.setText(toDoModel.getTask());
                        descTextView.setText(toDoModel.getDescription());
                    }else {
                        imageView.setImageResource(R.mipmap.ic_launcher);
                    }
                    // ...
                } else {
                    Toast.makeText(TaskDetailActivity.this,"No Record Found",Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TaskDetailActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }

    private void initviews() {
        imageView = findViewById(R.id.imageView);
        titleTextView = findViewById(R.id.title_textview);
        descTextView = findViewById(R.id.description_textview);


        //inti refrences
        db = FirebaseFirestore.getInstance();
        docRef = db.collection("task").document(getIntent().getStringExtra("task_id"));

    }
}