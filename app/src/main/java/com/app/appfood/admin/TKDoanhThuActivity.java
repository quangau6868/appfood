package com.app.appfood.admin;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.appfood.R;
import com.app.appfood.dao.ThanhToanDao;

import java.util.Calendar;

public class TKDoanhThuActivity extends AppCompatActivity {

    private Button btnStartDate, btnEndDate, btnDoanhThu;
    private TextView tvStartDate, tvEndDate, tvDoanhThu;
    private ThanhToanDao thanhToanDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tkdoanh_thu);

        // Khởi tạo các view
        btnStartDate = findViewById(R.id.btnStartDate);
        btnEndDate = findViewById(R.id.btnEndDate);
        btnDoanhThu = findViewById(R.id.btnDoanhThu);
        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        tvDoanhThu = findViewById(R.id.tvDoanhThu);  // Khởi tạo TextView hiển thị tổng doanh thu

        thanhToanDao = new ThanhToanDao(this);

        // Chọn ngày bắt đầu
        btnStartDate.setOnClickListener(v -> {
            showDatePickerDialog(true);
        });

        // Chọn ngày kết thúc
        btnEndDate.setOnClickListener(v -> {
            showDatePickerDialog(false);
        });

        // Xem doanh thu
        btnDoanhThu.setOnClickListener(v -> {
            // Lấy giá trị ngày bắt đầu và ngày kết thúc
            String startDate = tvStartDate.getText().toString(); // Lấy ngày từ TextView
            String endDate = tvEndDate.getText().toString(); // Lấy ngày từ TextView

            // Kiểm tra nếu ngày bắt đầu hoặc kết thúc chưa được chọn
            if (startDate.isEmpty() || endDate.isEmpty()) {
                showToast("Vui lòng chọn ngày bắt đầu và ngày kết thúc.");
            } else {
                // Ghi log để kiểm tra giá trị ngày tháng
                Log.d("TKDoanhThu", "Getting revenue from " + startDate + " to " + endDate);

                // Lấy tổng doanh thu trong khoảng thời gian
                double totalRevenue = thanhToanDao.getDoanhThuTrongKhoangThoiGian(startDate, endDate);

                // Ghi log để kiểm tra tổng doanh thu
                Log.d("TKDoanhThu", "Total revenue: " + totalRevenue);

                // Kiểm tra tổng doanh thu và hiển thị
                if (totalRevenue == 0) {
                    showToast("Không có doanh thu trong khoảng thời gian này.");
                } else {
                    // Định dạng giá trị totalRevenue theo kiểu tiền tệ
                    String formattedRevenue = String.format("%,.0f", totalRevenue);
                    tvDoanhThu.setText("Tổng doanh thu: " + formattedRevenue + " VND");
                }
            }
        });
    }

    private void showDatePickerDialog(boolean isStartDate) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    String selectedDate = String.format("%04d-%02d-%02d", year1, month1 + 1, dayOfMonth);
                    if (isStartDate) {
                        tvStartDate.setText(selectedDate); // Chỉ hiển thị ngày tháng năm
                    } else {
                        tvEndDate.setText(selectedDate); // Chỉ hiển thị ngày tháng năm
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    private void showToast(String message) {
        Toast.makeText(TKDoanhThuActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
