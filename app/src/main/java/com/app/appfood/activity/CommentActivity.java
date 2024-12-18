package com.app.appfood.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.appfood.R;
import com.app.appfood.dao.CommentDao;
import com.app.appfood.model.Comment;

import java.text.DecimalFormat;
import java.util.Calendar;

public class CommentActivity extends AppCompatActivity {

    private TextView tvTenNguoi, tvTongTien, tvTenSP;
    private Button btnDanhGia;
    private RatingBar ratingBar;
    private EditText etNgayDanhGia, etNoiDungDanhGia;
    private CommentDao commentDao;
    private int thanhToanId;  // Lưu thanhToanId

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        // Khởi tạo đối tượng CommentDao
        commentDao = new CommentDao(this);

        // Ánh xạ các View
        etNgayDanhGia = findViewById(R.id.etNgayDanhGia);
        etNoiDungDanhGia = findViewById(R.id.etNoiDungDanhGia);
        ratingBar = findViewById(R.id.ratingBar);
        tvTenSP = findViewById(R.id.tvTenSP);
        tvTenNguoi = findViewById(R.id.tvTenNguoi);
        tvTongTien = findViewById(R.id.tvTongTien);
        btnDanhGia = findViewById(R.id.btnDanhGia);

        // Xử lý chọn ngày đánh giá
        etNgayDanhGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        CommentActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                etNgayDanhGia.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);

                datePickerDialog.show();
            }
        });

        // Nhận dữ liệu từ Intent
        String tenNguoi = getIntent().getStringExtra("tenNguoi");
        double tongTien = getIntent().getDoubleExtra("tongTien", 0.0);
        String tenSP = getIntent().getStringExtra("tenSP");
        int maSP = getIntent().getIntExtra("maSP", -1);
        thanhToanId = getIntent().getIntExtra("thanhToanId", -1);

        // Định dạng tổng tiền
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedTongTien = decimalFormat.format(tongTien);

        // Hiển thị thông tin nhận được
        tvTenNguoi.setText("Tên người: " + tenNguoi);
        tvTongTien.setText("Tổng tiền: " + formattedTongTien + " VND");
        tvTenSP.setText("Tên SP: " + tenSP);

        // Xử lý sự kiện khi nhấn nút Đánh giá
        btnDanhGia.setOnClickListener(v -> {
            String ngayDanhGia = etNgayDanhGia.getText().toString().trim();
            String noiDung = etNoiDungDanhGia.getText().toString().trim();
            float rating = ratingBar.getRating();

            // Kiểm tra các trường nhập liệu
            if (ngayDanhGia.isEmpty()) {
                Toast.makeText(CommentActivity.this, "Vui lòng chọn ngày đánh giá!", Toast.LENGTH_SHORT).show();
                return; // Dừng nếu không có ngày đánh giá
            }

            if (noiDung.isEmpty()) {
                Toast.makeText(CommentActivity.this, "Vui lòng nhập nội dung đánh giá!", Toast.LENGTH_SHORT).show();
                return; // Dừng nếu không có nội dung đánh giá
            }

            if (rating == 0) {
                Toast.makeText(CommentActivity.this, "Vui lòng chọn mức đánh giá", Toast.LENGTH_SHORT).show();
                return; // Dừng nếu rating bằng 0
            }

            // Nếu các trường hợp trên đều hợp lệ
            if (maSP != -1 && thanhToanId != -1) {
                Comment comment = new Comment(maSP, tenNguoi, ngayDanhGia, (int) rating, noiDung, tongTien, thanhToanId);
                commentDao.addComment(comment);
                Toast.makeText(CommentActivity.this, "Đánh giá đã được gửi!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(CommentActivity.this, "Không tìm thấy sản phẩm hoặc thanh toán!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
