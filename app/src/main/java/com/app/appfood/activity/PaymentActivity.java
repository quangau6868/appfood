package com.app.appfood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Locale;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.app.appfood.R;

public class PaymentActivity extends AppCompatActivity {
    private TextView tvDetails, tvStatus, tvPaymentMethod;
    private Button btnFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Ánh xạ các view
        tvDetails = findViewById(R.id.tvDetails);
        tvStatus = findViewById(R.id.tvStatus);
        tvPaymentMethod = findViewById(R.id.tvPaymentMethod); // Thêm dòng này
        btnFinish = findViewById(R.id.btnFinish);

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String phone = intent.getStringExtra("phone");
        String address = intent.getStringExtra("address");
        double amount = intent.getDoubleExtra("amount", 0);
        String productName = intent.getStringExtra("productName");
        int quantity = intent.getIntExtra("quantity", 0);
        String date = intent.getStringExtra("date");
        String status = intent.getStringExtra("status");
        String paymentMethod = intent.getStringExtra("paymentMethod"); // Lấy phương thức thanh toán từ Intent

        if (status == null) {
            status = "Đang xử lý"; // Giá trị mặc định nếu không có trạng thái
        }
        if (paymentMethod == null) {
            paymentMethod = "Nhận tiền mặt"; // Giá trị mặc định nếu không có phương thức thanh toán
        }

        // Định dạng số tiền
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedAmount = numberFormat.format(amount);

        // Tạo chuỗi hiển thị chi tiết
        String details = "Tên khách hàng: " + name + "\n" +
                "Số điện thoại: " + phone + "\n" +
                "Địa chỉ: " + address + "\n" +
                "Tên sản phẩm: " + productName + "\n" +
                "Số lượng: " + quantity + "\n" +
                "Số tiền: " + formattedAmount + " VND\n" +
                "Ngày đặt hàng: " + date;

        // Đặt chuỗi vào TextView
        tvDetails.setText(details);
        tvStatus.setText("Trạng thái: " + status);
        tvPaymentMethod.setText("Phương thức thanh toán: " + paymentMethod); // Hiển thị phương thức thanh toán

        // Xử lý nút Hoàn Thành
        btnFinish.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Hoàn Thành")
                    .setMessage("Đơn hàng đã hoàn tất. Cảm ơn bạn!")
                    .setPositiveButton("OK", (dialog, which) -> {
                        dialog.dismiss();
                        finish();
                    })
                    .setIcon(R.drawable.baseline_done_outline_24) // Icon hoàn thành
                    .show();
        });
    }
}
