package com.app.appfood.activity;

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
import com.app.appfood.adapter.ProductAdapter;
import com.app.appfood.dao.sanPhamDao;
import com.app.appfood.fragment.ProductDetailFragment;
import com.app.appfood.model.SanPham;

import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<SanPham> searchResults;
    private sanPhamDao productDao;
    private TextView tvSearchResults, tvNoResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_results);

        recyclerView = findViewById(R.id.recycler_search_results);
        tvSearchResults = findViewById(R.id.tv_search_results);
        tvNoResults = findViewById(R.id.tv_no_results);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productDao = new sanPhamDao(this);

        // Lấy từ khóa tìm kiếm từ Intent
        String query = getIntent().getStringExtra("query");

        // Cập nhật TextView với từ khóa tìm kiếm
        tvSearchResults.setText("Kết quả tìm kiếm cho: " + query);

        // Thực hiện tìm kiếm
        searchResults = productDao.searchProducts(query);

        // Hiển thị kết quả hoặc thông báo không có kết quả
        if (searchResults != null && !searchResults.isEmpty()) {
            productAdapter = new ProductAdapter(searchResults, new ProductAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(SanPham sanPham) {
                    // Xử lý khi nhấn vào sản phẩm
                    ProductDetailFragment productDetailFragment = new ProductDetailFragment();
                    Bundle args = new Bundle();
                    args.putInt("productId", sanPham.getMaSP());
                    productDetailFragment.setArguments(args);

                    // Mở ProductDetailFragment khi nhấn vào sản phẩm
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, productDetailFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
            recyclerView.setAdapter(productAdapter);
            tvNoResults.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            tvNoResults.setVisibility(View.VISIBLE);
        }
    }
}