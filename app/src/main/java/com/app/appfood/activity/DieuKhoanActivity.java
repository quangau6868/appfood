package com.app.appfood.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.appfood.R;

public class DieuKhoanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dieu_khoan);

        // Nhận dữ liệu từ Intent
        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");
        String date = getIntent().getStringExtra("date");

        // Gán dữ liệu vào các TextView
        TextView txtTitle = findViewById(R.id.textTitle);
        TextView txtContent = findViewById(R.id.textContent);
        TextView txtDate = findViewById(R.id.textDate);

        txtTitle.setText(title);
        txtContent.setText(content);
        txtDate.setText("Viết ngày: " + date);
    }
}
