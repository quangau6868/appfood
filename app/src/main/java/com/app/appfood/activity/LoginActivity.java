package com.app.appfood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.app.appfood.MainActivity;
import com.app.appfood.R;
import com.app.appfood.admin.AdminActivity;
import com.app.appfood.dao.UserDao;
import com.app.appfood.model.User;

public class LoginActivity extends AppCompatActivity {

    private TextView tvsignup,loginForgotPassword;
    private EditText edtemail;
    private EditText edtpassword;
    private Button loginButton;
    private CheckBox showPasswordCheckBox;

    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        tvsignup = findViewById(R.id.tvsignup);
        edtemail = findViewById(R.id.loginEmail);
        edtpassword = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.loginBTN);
        loginForgotPassword = findViewById(R.id.loginForgotPassword);
        showPasswordCheckBox = findViewById(R.id.checkBoxShowPassword);

        userDao = new UserDao(this);

        tvsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        loginForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });

        // Thiết lập sự kiện thay đổi trạng thái của checkbox "Hiện mật khẩu"
        showPasswordCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Hiển thị mật khẩu
                edtpassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT);
            } else {
                // Ẩn mật khẩu
                edtpassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            // Đặt lại con trỏ về cuối trường mật khẩu
            edtpassword.setSelection(edtpassword.getText().length());
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtemail.getText().toString();
                String password = edtpassword.getText().toString();

                if (email.isEmpty()) {
                    edtemail.setError("Email không được để trống!");
                    return;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    edtemail.setError("Email không đúng định dạng!");
                    return;
                }

                if (password.isEmpty()) {
                    edtpassword.setError("Mật khẩu không được để trống!");
                    return;
                }

                User user = new User(email, password);
                user = userDao.CheckLogin(user);
                if (user != null) {
                    int role = user.getRole();
                    if (role == 0) {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công (Admin)", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                    } else if (role == 1) {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("username", user.getUsername());
                        intent.putExtra("email", user.getEmail());
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}