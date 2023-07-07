package com.example.todo_sample.screen;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todo_sample.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddDescriptionActivity extends AppCompatActivity {

    private EditText editText;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_description);

        editText = findViewById(R.id.editTextTextMultiLine);
        firestore = FirebaseFirestore.getInstance();

    }

    public void buttonBold(View view){
        Spannable spannableString = new SpannableStringBuilder(editText.getText());
        spannableString.setSpan(new StyleSpan(Typeface.BOLD),
                editText.getSelectionStart(),
                editText.getSelectionEnd(),
                0);

        editText.setText(spannableString);
    }
    public void buttonItalics(View view){
        Spannable spannableString = new SpannableStringBuilder(editText.getText());
        spannableString.setSpan(new StyleSpan(Typeface.ITALIC),
                editText.getSelectionStart(),
                editText.getSelectionEnd(),
                0);

        editText.setText(spannableString);

    }
    public void buttonUnderline(View view){
        Spannable spannableString = new SpannableStringBuilder(editText.getText());
        spannableString.setSpan(new UnderlineSpan(),
                editText.getSelectionStart(),
                editText.getSelectionEnd(),
                0);

        editText.setText(spannableString);
    }

    public void buttonNoFormat(View view){
        String stringText = editText.getText().toString();
        editText.setText(stringText);
    }


    public void buttonAlignmentLeft(View view){
        editText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        Spannable spannableString = new SpannableStringBuilder(editText.getText());
        editText.setText(spannableString);
    }

    public void buttonAlignmentCenter(View view){
        editText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        Spannable spannableString = new SpannableStringBuilder(editText.getText());
        editText.setText(spannableString);
    }

    public void buttonAlignmentRight(View view){
        editText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        Spannable spannableString = new SpannableStringBuilder(editText.getText());
        editText.setText(spannableString);
    }

    public void saveDescription(View view){
        firestore.collection("task").document(getIntent().getStringExtra("task_id")).update("description" , editText.getText().toString());
        Intent intent = new Intent(AddDescriptionActivity.this, MainActivity.class);
        startActivity(intent);
    }
}