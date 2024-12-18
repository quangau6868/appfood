package com.app.appfood.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.app.appfood.R;
import com.app.appfood.adapter.FAQAdapter;
import com.app.appfood.model.FAQ;

import java.util.ArrayList;
import java.util.List;

public class TrungtamTrogiupActivity extends AppCompatActivity {

    private RecyclerView faqRecyclerView;
    private FAQAdapter faqAdapter;
    private List<FAQ> faqList;
    private List<FAQ> filteredList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trung_tam_tro_giup);

        // Khởi tạo RecyclerView
        faqRecyclerView = findViewById(R.id.faqRecyclerView);
        faqRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Dữ liệu mẫu
        faqList = new ArrayList<>();
        // Dữ liệu mẫu với chủ đề rõ ràng
        faqList = new ArrayList<>();
        faqList.add(new FAQ("Vì sao tôi không sử dụng/liên kết được Ví ShopeePay với ShopeeFood?", "Gợi ý"));
        faqList.add(new FAQ("Tôi muốn đổi món, giờ giao, thông tin người nhận", "Đặt đồ ăn"));
        faqList.add(new FAQ("Làm thế nào khi Tài xế nhấn 'hoàn tất đơn hàng' nhưng không giao hàng cho tôi?", "Đặt giao hàng"));
        faqList.add(new FAQ("Tôi muốn đánh giá thái độ Tài xế", "Đặt giao hàng"));
        faqList.add(new FAQ("Khi nào tôi nhận được tiền hoàn trả?", "Khuyến mãi"));
        faqList.add(new FAQ("Hướng dẫn liên kết thẻ JCB MB Hi ShopeeFood", "Gợi ý"));
        faqList.add(new FAQ("Vì sao tôi không thể thanh toán đơn hàng bằng tiền mặt?", "Đặt đồ ăn"));
        faqList.add(new FAQ("Hướng dẫn xử lý lỗi thanh toán thường gặp", "Đặt đồ ăn"));

        // Gán adapter cho RecyclerView
        faqAdapter = new FAQAdapter(this, faqList);
        faqRecyclerView.setAdapter(faqAdapter);

        // Khởi tạo các nút danh mục
        Button btnGoiY = findViewById(R.id.btnGoiy);
        Button btnDatDoAn = findViewById(R.id.btnDatDoAn);
        Button btnDatGiaoHang = findViewById(R.id.btnDatGiaoHang);
        Button btnKhuyenMai = findViewById(R.id.btnKhuyenMai);

        // Sự kiện khi nhấn nút "Gợi ý"
        btnGoiY.setOnClickListener(view -> filterFAQ("Gợi ý"));

        // Sự kiện khi nhấn nút "Đặt đồ ăn"
        btnDatDoAn.setOnClickListener(view -> filterFAQ("Đặt đồ ăn"));

        // Sự kiện khi nhấn nút "Đặt giao hàng"
        btnDatGiaoHang.setOnClickListener(view -> filterFAQ("Đặt giao hàng"));

        // Sự kiện khi nhấn nút "Khuyến mãi"
        btnKhuyenMai.setOnClickListener(view -> filterFAQ("Khuyến mãi"));
    }

    // Lọc câu hỏi theo chủ đề
    private void filterFAQ(String category) {
        filteredList = new ArrayList<>();
        for (FAQ faq : faqList) {
            if (faq.getCategory().equals(category)) {
                filteredList.add(faq);
            }
        }
        faqAdapter.updateFAQList(filteredList);
    }
}
