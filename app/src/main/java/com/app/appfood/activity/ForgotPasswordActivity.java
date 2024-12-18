package com.app.appfood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.app.appfood.R;
import com.app.appfood.dao.UserDao;
import com.app.appfood.model.User;

public class ForgotPasswordActivity extends AppCompatActivity {

    ImageButton imgbtn;
    EditText forgotEmail;
    Button btnConfirmPass;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);

        imgbtn = findViewById(R.id.forgot_myImageButton);
        forgotEmail = findViewById(R.id.forgot_Email);
        btnConfirmPass = findViewById(R.id.forgot_btnConfirm);

        userDao = new UserDao(this);


        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnConfirmPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = forgotEmail.getText().toString();

                if (email.isEmpty()) {
                    forgotEmail.setError("Email không được để trống!");
                    return;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    forgotEmail.setError("Email không đúng định dạng!");
                    return;
                }

                User user = new User(email);
                int result = userDao.checkEmail(user);
                if (result == -1) {
                    Intent intent = new Intent(ForgotPasswordActivity.this, ResetPasswordActivity.class);
                    intent.putExtra("username", email);
                    startActivity(intent);
                }else {
                    Toast.makeText(ForgotPasswordActivity.this, "Email không tồn tại, vui lòng sử dụng đăng kí email khác", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}