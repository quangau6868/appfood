package com.app.appfood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class ResetPasswordActivity extends AppCompatActivity {

    ImageButton btnImangeReset;
    EditText EditTextPassword, EditTextConfirmPassword;
    Button btnResetPass;
    TextView txtgmail;

    private UserDao userDao;
    private CheckBox showPasswordCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);

        txtgmail = findViewById(R.id.Reset_3);
        btnImangeReset = findViewById(R.id.Reset_myImageButton);
        EditTextPassword = findViewById(R.id.Reset_new_password);
        EditTextConfirmPassword = findViewById(R.id.Reset_confirm_password);
        btnResetPass = findViewById(R.id.Reset_btnConfirm);
        showPasswordCheckBox = findViewById(R.id.checkBoxShowPassword);

        userDao = new UserDao(this);

        btnImangeReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Thiết lập sự kiện thay đổi trạng thái của checkbox "Hiện mật khẩu"
        showPasswordCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Hiển thị mật khẩu
                EditTextPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT);
                EditTextConfirmPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT);
            } else {
                // Ẩn mật khẩu
                EditTextPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                EditTextConfirmPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            EditTextPassword.setSelection(EditTextPassword.getText().length());
            EditTextConfirmPassword.setSelection(EditTextConfirmPassword.getText().length());
        });

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String displayText = "Gmail: " + username;
        txtgmail.setText(displayText);

        btnResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = txtgmail.getText().toString().trim();
                String newpass1 = EditTextPassword.getText().toString().trim();
                String newpass2 = EditTextConfirmPassword.getText().toString().trim();

                if (newpass1.isEmpty()) {
                    EditTextPassword.setError("Mật khẩu không được để trống!");
                    return;
                } else if (newpass1.length() < 6) {
                    EditTextPassword.setError("Mật khẩu phải có ít nhất 6 ký tự!");
                    return;
                }

                // Kiểm tra xác nhận mật khẩu
                if (newpass2.isEmpty()) {
                    EditTextConfirmPassword.setError("Mật khẩu xác nhận không được để trống!");
                    return;
                } else if (!newpass1.equals(newpass2)) {
                    EditTextConfirmPassword.setError("Mật khẩu không khớp!");
                    return;
                }

                // Nếu tất cả các điều kiện đều hợp lệ
                User user = new User(userEmail, newpass1);
                boolean check = userDao.updatePassword(user);
                if (check) {
                    startActivity(new Intent(ResetPasswordActivity.this, SuccesfullyRegisteredActivity.class));
                    Toast.makeText(ResetPasswordActivity.this, "Đặt lại mật khẩu thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ResetPasswordActivity.this, "Đặt lại mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}