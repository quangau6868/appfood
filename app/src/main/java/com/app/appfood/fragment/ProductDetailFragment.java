package com.app.appfood.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.appfood.R;
import com.app.appfood.adapter.CommentAdapter;
import com.app.appfood.dao.CartDao;
import com.app.appfood.dao.CommentDao;
import com.app.appfood.dao.sanPhamDao;
import com.app.appfood.model.Comment;
import com.app.appfood.model.SanPham;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailFragment extends Fragment {

    private TextView tenSP, rating, cookingTime, giaBan, moTa, quantity;
    private ImageView imageSanPham, timestar, timeIcon;
    private ImageButton backButton;
    private SeekBar quantitySeekBar;
    private Button addToCart;
    private SanPham product;
    private sanPhamDao productDao;
    private CartDao cartDao;
    private CommentDao commentDao;
    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;

    private int productId;
    private double originalPrice;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_san_pham, container, false);

        // Ánh xạ các view
        imageSanPham = view.findViewById(R.id.imageSanPham);
        timestar = view.findViewById(R.id.timestar);
        timeIcon = view.findViewById(R.id.timeIcon);
        tenSP = view.findViewById(R.id.tensp);
        quantity = view.findViewById(R.id.tvQuantity);
        rating = view.findViewById(R.id.rating);
        cookingTime = view.findViewById(R.id.cookingTime);
        giaBan = view.findViewById(R.id.giaban);
        moTa = view.findViewById(R.id.mo_ta);
        addToCart = view.findViewById(R.id.addToCart);
        backButton = view.findViewById(R.id.backButton);
        quantitySeekBar = view.findViewById(R.id.seekBarQuantity);
        recyclerView = view.findViewById(R.id.RecyclerViewDanhgia);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        commentDao =  new CommentDao(getContext());

        // Tạo danh sách đánh giá và gán adapter
        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(commentList);
        recyclerView.setAdapter(commentAdapter);
        loadComments(); // Gọi hàm để tải dữ liệu từ DB hoặc API

        // Ẩn BottomNavigationView khi vào trang chi tiết
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.GONE);
        }

        backButton.setOnClickListener(v -> getActivity().onBackPressed());

        // Lấy sản phẩm từ arguments
        if (getArguments() != null) {
            productId = getArguments().getInt("productId");
        }

        // Khởi tạo DAO để truy vấn dữ liệu
        productDao = new sanPhamDao(getContext());

        // Lấy thông tin sản phẩm từ database
        loadProductDetails(productId);

        // Xử lý SeekBar để điều chỉnh số lượng
        quantitySeekBar.setMax(10);
        quantitySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int qty = Math.max(1, progress); // Không cho số lượng nhỏ hơn 1
                quantity.setText(String.valueOf(qty)); // Hiển thị số lượng
                updatePrice(qty); // Cập nhật giá bán
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Không cần xử lý
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Không cần xử lý
            }
        });

        cartDao = new CartDao(getContext());

        addToCart.setOnClickListener(v -> {
            try {
                int qty = Integer.parseInt(quantity.getText().toString());

                SanPham productToAdd = new SanPham(
                        product.getMaSP(),
                        product.getTenSP(),
                        product.getGiaBan(),
                        qty,
                        product.getHinhAnh()
                );
                cartDao.addProductToCart(productToAdd);  // Thêm sản phẩm vào giỏ hàng
                Toast.makeText(getContext(), "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();

                // Cập nhật giỏ hàng thông qua callback
                if (getActivity() instanceof OnCartUpdatedListener) {
                    ((OnCartUpdatedListener) getActivity()).onCartUpdated();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Số lượng không hợp lệ!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void loadComments() {
        // Sử dụng phương thức getCommentsByProductId để lấy danh sách đánh giá
        List<Comment> comments = commentDao.getCommentsByProductId(productId);

        // Kiểm tra xem dữ liệu có hợp lệ không
        if (comments == null || comments.isEmpty()) {
            Log.d("ProductDetailFragment", "Không có bình luận cho sản phẩm này.");
        } else {
            Log.d("ProductDetailFragment", "Có " + comments.size() + " bình luận.");
        }

        // Cập nhật danh sách đánh giá
        commentList.clear();
        commentList.addAll(comments);

        // Thông báo cho adapter rằng dữ liệu đã thay đổi
        commentAdapter.notifyDataSetChanged();
    }


    private void loadProductDetails(int productId) {
        // Lấy sản phẩm theo ID từ database
        product = productDao.getSanPhamById(productId);

        if (product != null) {
            // Hiển thị thông tin sản phẩm lên giao diện
            tenSP.setText(product.getTenSP()); // Tên sản phẩm
            originalPrice = product.getGiaBan(); // Giá gốc
            updatePrice(1); // Thiết lập giá bán ban đầu (số lượng 1)
            moTa.setText(product.getMoTa()); // Mô tả
            quantitySeekBar.setProgress(1); // Khởi tạo SeekBar với số lượng 1

            // Hiển thị hình ảnh sản phẩm
            if (product.getHinhAnh() != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(product.getHinhAnh(), 0, product.getHinhAnh().length);
                imageSanPham.setImageBitmap(bitmap); // Hiển thị hình ảnh
            }

            // Kiểm tra tình trạng sản phẩm (Còn hàng hay hết hàng)
            if (!product.isConHang()) {
                addToCart.setEnabled(false); // Vô hiệu hóa nút "Add to Cart"
                addToCart.setText("Hết hàng"); // Thay đổi văn bản nút
            } else {
                addToCart.setEnabled(true); // Kích hoạt nút "Add to Cart" nếu còn hàng
                addToCart.setText("Thêm vào giỏ hàng"); // Đặt lại văn bản nút
            }

            loadComments(); // Load các bình luận sản phẩm
        } else {
            Log.e("ProductDetailFragment", "Không tìm thấy sản phẩm với ID: " + productId);
        }
    }


    private void updatePrice(int quantity) {
        double totalPrice = originalPrice * quantity;
        giaBan.setText(String.format("%,.0f VND", totalPrice)); // Cập nhật giá bán
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Hiện lại BottomNavigationView khi quay lại
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }

    public interface OnCartUpdatedListener {
        void onCartUpdated();
    }
}
