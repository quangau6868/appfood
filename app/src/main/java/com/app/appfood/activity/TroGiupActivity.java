package com.app.appfood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.app.appfood.R;

public class TroGiupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tro_giup);

        ImageButton backButton = findViewById(R.id.backButton);
        Button button_cau_hoi = findViewById(R.id.button_cau_hoi);
        Button button_chinh_sach = findViewById(R.id.button_chinh_sach);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        button_cau_hoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TroGiupActivity.this, TrungtamTrogiupActivity.class));
            }
        });

        button_chinh_sach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TroGiupActivity.this, DanhGiaActivity.class));
            }
        });

    }
}