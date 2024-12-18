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

import com.app.appfood.R;
import com.app.appfood.dao.UserDao;
import com.app.appfood.model.User;

public class SignupActivity extends AppCompatActivity {

    private TextView tvlogin;
    private EditText signupEmail, signupUser;
    private EditText signuppassword;
    private Button signupButton;
    private CheckBox showPasswordCheckBox;

    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        tvlogin = findViewById(R.id.tvlogin);
        signupEmail = findViewById(R.id.signupEmail);
        signupUser = findViewById(R.id.signupUserName);
        signuppassword = findViewById(R.id.signupPassword);
        signupButton = findViewById(R.id.signupBTN);
        showPasswordCheckBox = findViewById(R.id.checkBoxShowPassword1);

        userDao = new UserDao(this);

        tvlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });

        // Thiết lập sự kiện thay đổi trạng thái của checkbox "Hiện mật khẩu"
        showPasswordCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Hiển thị mật khẩu
                signuppassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT);
            } else {
                // Ẩn mật khẩu
                signuppassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            signuppassword.setSelection(signuppassword.getText().length());
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = signupUser.getText().toString().trim();
                String email = signupEmail.getText().toString().trim();
                String password = signuppassword.getText().toString();

                if (email.isEmpty()) {
                    signupEmail.setError("Email không được để trống!");
                    return;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    signupEmail.setError("Email không đúng định dạng!");
                    return;
                }

                if (name.isEmpty()) {
                    signupUser.setError("Họ tên không được để trống!");
                    return;
                } else if (name.length() < 3) {
                    signupUser.setError("Họ tên phải có ít nhất 3 ký tự!");
                    return;
                }

                if (password.isEmpty()) {
                    signuppassword.setError("Mật khẩu không được để trống!");
                    return;
                } else if (password.length() < 6) {
                    signuppassword.setError("Mật khẩu phải có ít nhất 6 ký tự!");
                    return;
                }

                User user = new User(name, email, password);
                int result = userDao.Register(user);
                if (result == 1) {
                    Toast.makeText(SignupActivity.this, "Đăng kí thành công", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (result == -1) {
                    Toast.makeText(SignupActivity.this, "Email đã tồn tại, vui lòng sử dụng email khác", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignupActivity.this, "Đăng kí thất bại", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}