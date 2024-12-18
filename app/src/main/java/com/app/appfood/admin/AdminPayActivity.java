package com.app.appfood.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.appfood.R;
import com.app.appfood.adapter.PayAdapter;
import com.app.appfood.dao.ThanhToanDao;
import com.app.appfood.model.ThanhToan;

import java.util.List;

public class AdminPayActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPay;
    private PayAdapter payAdapter;
    private ThanhToanDao thanhToanDao;
    private TextView emptyCartMessage, thanhToanTitle;
    private boolean isAdmin = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_pay);

        // Initializing views
        recyclerViewPay = findViewById(R.id.recyclerViewPay1);
        emptyCartMessage = findViewById(R.id.emptyCartMessage2);
        thanhToanTitle = findViewById(R.id.Thanh_toán_1);

        thanhToanDao = new ThanhToanDao(this);

        // Set up RecyclerView
        recyclerViewPay.setLayoutManager(new LinearLayoutManager(this));

        // Get list of ThanhToan
        List<ThanhToan> thanhToanList = thanhToanDao.getAllThanhToan();

        // Check if there are any ThanhToan
        if (thanhToanList.isEmpty()) {
            emptyCartMessage.setVisibility(View.VISIBLE);
            thanhToanTitle.setVisibility(View.GONE);
            recyclerViewPay.setVisibility(View.GONE);
        } else {
            emptyCartMessage.setVisibility(View.GONE);
            thanhToanTitle.setVisibility(View.VISIBLE);
            recyclerViewPay.setVisibility(View.VISIBLE);

            // Setting up adapter for RecyclerView, truyền thêm isAdmin vào Adapter
            payAdapter = new PayAdapter(this, thanhToanList, isAdmin);
            recyclerViewPay.setAdapter(payAdapter);
        }
    }

    // Cập nhật trạng thái giỏ hàng
    public void updateCartStatus() {
        List<ThanhToan> thanhToanList = thanhToanDao.getAllThanhToan();
        if (thanhToanList.isEmpty()) {
            emptyCartMessage.setVisibility(View.VISIBLE);
            thanhToanTitle.setVisibility(View.GONE);
            recyclerViewPay.setVisibility(View.GONE);
        }
    }
}
