package com.app.appfood.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.app.appfood.R;
import com.app.appfood.activity.SearchResultsActivity;
import com.app.appfood.adapter.ImageAdapter;
import com.app.appfood.adapter.ProductAdapter;
import com.app.appfood.dao.sanPhamDao;
import com.app.appfood.model.Image;
import com.app.appfood.model.SanPham;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class TrangchuFragment extends Fragment {

    private View mView;
    private ViewPager mviewPager;
    private CircleIndicator mcircleIndicator;
    private List<Image> imageList;
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<SanPham> productList;
    private sanPhamDao productDao;
    private ProgressBar progressBar;
    private EditText searchProduct;
    private ImageView giohang;

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if(mviewPager.getCurrentItem() == imageList.size() -1){
                mviewPager.setCurrentItem(0);
            }else {
                mviewPager.setCurrentItem(mviewPager.getCurrentItem() +1);
            }
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_trang_chu, container, false);

        mviewPager = mView.findViewById(R.id.view_pager);
        mcircleIndicator = mView.findViewById(R.id.circle_indicator);
        recyclerView = mView.findViewById(R.id.RerecyclerViewSP);
        progressBar = mView.findViewById(R.id.progressBar1);
        searchProduct = mView.findViewById(R.id.searchProduct);
        giohang = mView.findViewById(R.id.giohang);

        productDao = new sanPhamDao(getContext());

        imageList = getListImage();
        ImageAdapter adapter = new ImageAdapter(imageList);
        mviewPager.setAdapter(adapter);
        mcircleIndicator.setViewPager(mviewPager);

        giohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mở GiohangFragment thay vì Activity
                GiohangFragment giohangFragment = new GiohangFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, giohangFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });


        // Cài đặt việc tự động chuyển ảnh
        mHandler.postDelayed(mRunnable, 3000);
        mviewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mHandler.removeCallbacks(mRunnable);
                mHandler.postDelayed(mRunnable, 3000);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Hiển thị ProgressBar khi bắt đầu tải dữ liệu
        progressBar.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Lấy danh sách sản phẩm từ sanPhamDao
                productList = productDao.getDanhsachSanPham();

                // Ẩn ProgressBar khi dữ liệu đã tải xong
                progressBar.setVisibility(View.GONE);

                // Kiểm tra nếu có sản phẩm, cập nhật adapter
                if (productList != null && !productList.isEmpty()) {
                    productAdapter = new ProductAdapter(productList, new ProductAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(SanPham sanPham) {
                            // Thực hiện hành động khi click vào sản phẩm
                            ProductDetailFragment productDetailFragment = new ProductDetailFragment();
                            Bundle args = new Bundle();
                            args.putInt("productId", sanPham.getMaSP());
                            productDetailFragment.setArguments(args);

                            getFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, productDetailFragment)
                                    .addToBackStack(null)
                                    .commit();
                        }
                    });
                    recyclerView.setAdapter(productAdapter);
                }
            }
        }, 1500);

        searchProduct.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    String query = searchProduct.getText().toString().trim();
                    if (!TextUtils.isEmpty(query)) {
                        // Chuyển đến màn hình kết quả tìm kiếm
                        Intent intent = new Intent(getContext(), SearchResultsActivity.class);
                        intent.putExtra("query", query); // Truyền từ khóa tìm kiếm
                        startActivity(intent);
                    } else {
                        Toast.makeText(getContext(), "Vui lòng nhập từ khóa tìm kiếm", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });

        // Set OnClickListener cho categoryImage1
        ImageView categoryImage1 = mView.findViewById(R.id.categoryImage1);
        categoryImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị ProgressBar khi bắt đầu tải sản phẩm
                progressBar.setVisibility(View.VISIBLE);

                // Tạo thread để truy vấn sản phẩm theo loại
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // Giả lập thời gian tải dữ liệu
                            Thread.sleep(400);

                            // Lấy danh sách sản phẩm theo loại từ Dao (Ví dụ loại Pizza có mã = 1)
                            List<SanPham> pizzaData = productDao.getSanPhamListByLoai(1);  // Loại Pizza

                            // Cập nhật danh sách sản phẩm trong UI
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    productList.clear();  // Xóa danh sách sản phẩm hiện tại
                                    productList.addAll(pizzaData);  // Thêm sản phẩm theo loại Pizza

                                    // Cập nhật adapter và ẩn ProgressBar
                                    productAdapter.notifyDataSetChanged();
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        ImageView categoryImage2 = mView.findViewById(R.id.categoryImage2);
        categoryImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị ProgressBar khi bắt đầu tải sản phẩm
                progressBar.setVisibility(View.VISIBLE);

                // Tạo thread để truy vấn sản phẩm theo loại
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(400);
                            List<SanPham> ChickenData = productDao.getSanPhamListByLoai(2);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    productList.clear();
                                    productList.addAll(ChickenData);
                                    // Cập nhật adapter và ẩn ProgressBar
                                    productAdapter.notifyDataSetChanged();
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        ImageView categoryImage3 = mView.findViewById(R.id.categoryImage3);
        categoryImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị ProgressBar khi bắt đầu tải sản phẩm
                progressBar.setVisibility(View.VISIBLE);

                // Tạo thread để truy vấn sản phẩm theo loại
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(400);
                            List<SanPham> HumbergerData = productDao.getSanPhamListByLoai(3);

                            // Cập nhật danh sách sản phẩm trong UI
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    productList.clear();  // Xóa danh sách sản phẩm hiện tại
                                    productList.addAll(HumbergerData);  // Thêm sản phẩm theo loại Pizza

                                    // Cập nhật adapter và ẩn ProgressBar
                                    productAdapter.notifyDataSetChanged();
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        ImageView categoryImage4 = mView.findViewById(R.id.categoryImage4);
        categoryImage4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị ProgressBar khi bắt đầu tải sản phẩm
                progressBar.setVisibility(View.VISIBLE);

                // Tạo thread để truy vấn sản phẩm theo loại
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(400);
                            List<SanPham> DrinkData = productDao.getSanPhamListByLoai(4);
                            // Cập nhật danh sách sản phẩm trong UI
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    productList.clear();
                                    productList.addAll(DrinkData);
                                    // Cập nhật adapter và ẩn ProgressBar
                                    productAdapter.notifyDataSetChanged();
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        return mView;
    }
    // tạo danh sách ảnh
    private List<Image> getListImage(){
        List<Image> list = new ArrayList<>();
        list.add(new Image(R.drawable.image1));
        list.add(new Image(R.drawable.image2));
        list.add(new Image(R.drawable.image3));
        return list;
    }
    //khi máy bị thoát đột ngột
    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
    }
    //chạy tiếp ảnh
    @Override
    public void onResume() {
        super.onResume();
        mHandler.postDelayed(mRunnable, 3000);
    }


}

