package com.app.appfood.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.appfood.R;
import com.app.appfood.dao.sanPhamDao;
import com.app.appfood.adapter.ProductNoiBatAdapter;
import com.app.appfood.model.SanPham;

import java.util.ArrayList;
import java.util.List;

public class NoibatFragment extends Fragment {

    private View mView;
    private RecyclerView recyclerViewPizza, recyclerViewGa, recyclerViewHamburger;
    private ProgressBar progressBarPizza, progressBarGa, progressBarHamburger;
    private ProductNoiBatAdapter pizzaAdapter, gaAdapter, hamburgerAdapter;
    private List<SanPham> pizzaList, gaList, hamburgerList;
    private sanPhamDao sanPhamDao;
    private boolean dataLoaded = false; // Biến để theo dõi trạng thái tải dữ liệu

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_noi_bat, container, false);

        // Khởi tạo đối tượng dao
        sanPhamDao = new sanPhamDao(getContext());

        // Khởi tạo RecyclerView và ProgressBar
        recyclerViewPizza = mView.findViewById(R.id.recyclerViewPizza);
        recyclerViewGa = mView.findViewById(R.id.recyclerViewGa);
        recyclerViewHamburger = mView.findViewById(R.id.recyclerViewHamburger);
        progressBarPizza = mView.findViewById(R.id.progressBarPizza);
        progressBarGa = mView.findViewById(R.id.progressBarGa);
        progressBarHamburger = mView.findViewById(R.id.progressBarHamburger);

        // Khởi tạo danh sách sản phẩm
        pizzaList = new ArrayList<>();
        gaList = new ArrayList<>();
        hamburgerList = new ArrayList<>();

        // Thiết lập LayoutManager cho RecyclerView
        recyclerViewPizza.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewGa.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewHamburger.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Tải dữ liệu sản phẩm
        loadProductData();

        // Thiết lập adapter cho các RecyclerView và xử lý sự kiện click
        setupAdapters();

        return mView;
    }

    private void loadProductData() {
        // Hiển thị ProgressBar khi bắt đầu tải
        progressBarPizza.setVisibility(View.VISIBLE);
        progressBarGa.setVisibility(View.VISIBLE);
        progressBarHamburger.setVisibility(View.VISIBLE);

        // Tạo thread giả lập thời gian tải
        new Thread(() -> {
            try {
                // Giả lập thời gian tải dữ liệu
                Thread.sleep(1000);

                // Lấy sản phẩm theo mã loại từ sanPhamDao
                List<SanPham> pizzaData = sanPhamDao.getSanPhamListByLoai(1);  // Loại Pizza
                List<SanPham> gaData = sanPhamDao.getSanPhamListByLoai(2);     // Loại Gà
                List<SanPham> hamburgerData = sanPhamDao.getSanPhamListByLoai(3); // Loại Hamburger

                // Cập nhật danh sách sản phẩm và ẩn ProgressBar
                getActivity().runOnUiThread(() -> {
                    pizzaList.addAll(pizzaData);
                    gaList.addAll(gaData);
                    hamburgerList.addAll(hamburgerData);

                    pizzaAdapter.notifyDataSetChanged();
                    gaAdapter.notifyDataSetChanged();
                    hamburgerAdapter.notifyDataSetChanged();

                    // Tắt ProgressBar và đặt dataLoaded thành true
                    progressBarPizza.setVisibility(View.GONE);
                    progressBarGa.setVisibility(View.GONE);
                    progressBarHamburger.setVisibility(View.GONE);

                    dataLoaded = true; // Đánh dấu là đã tải xong dữ liệu
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void setupAdapters() {
        pizzaAdapter = new ProductNoiBatAdapter(pizzaList, sanPham -> {
            if (dataLoaded) {
                Log.d("NoibatFragment", "Clicked product ID: " + sanPham.getMaSP());
                openProductDetail(sanPham);
            }
        });

        gaAdapter = new ProductNoiBatAdapter(gaList, sanPham -> {
            if (dataLoaded) {
                openProductDetail(sanPham);
            }
        });

        hamburgerAdapter = new ProductNoiBatAdapter(hamburgerList, sanPham -> {
            if (dataLoaded) {
                openProductDetail(sanPham);
            }
        });

        recyclerViewPizza.setAdapter(pizzaAdapter);
        recyclerViewGa.setAdapter(gaAdapter);
        recyclerViewHamburger.setAdapter(hamburgerAdapter);
    }

    private void openProductDetail(SanPham sanPham) {
        if (getActivity() != null && sanPham != null) {
            ProductDetailFragment productDetailFragment = new ProductDetailFragment();
            Bundle args = new Bundle();
            args.putInt("productId", sanPham.getMaSP()); // Truyền ID sản phẩm
            productDetailFragment.setArguments(args); // Truyền dữ liệu vào fragment

            if (getActivity().findViewById(R.id.fragment_container) != null) {
                View bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
                if (bottomNavigationView != null) {
                    bottomNavigationView.setVisibility(View.GONE);
                }

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, productDetailFragment) // Thay thế fragment hiện tại
                        .addToBackStack(null) // Thêm vào back stack
                        .commit();
            } else {
                Log.e("NoibatFragment", "Không tìm thấy fragment_container trong layout");
            }
        }
    }

}
