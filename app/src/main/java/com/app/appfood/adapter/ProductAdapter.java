package com.app.appfood.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.app.appfood.R;
import com.app.appfood.model.SanPham;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<SanPham> productList;
    private OnItemClickListener listener;

    public ProductAdapter(List<SanPham> productList, OnItemClickListener listener) {
        this.productList = productList;
        this.listener = listener;
    }

    // Tạo view holder cho mỗi sản phẩm
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hien_thi_san_pham, parent, false);
        return new ProductViewHolder(itemView);
    }

    // Liên kết dữ liệu với ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        // Lấy sản phẩm tại vị trí hiện tại
        SanPham sanPham = productList.get(position);
        holder.tenSP.setText(sanPham.getTenSP());
        holder.giaBan.setText(String.format("%,.0f VND", sanPham.getGiaBan()));

        // Chuyển đổi byte[] thành hình ảnh
        if (sanPham.getHinhAnh() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(sanPham.getHinhAnh(), 0, sanPham.getHinhAnh().length);
            holder.imageView.setImageBitmap(bitmap);
        }

        // Thiết lập sự kiện click cho sản phẩm
        holder.itemView.setOnClickListener(v -> listener.onItemClick(sanPham));
    }

    @Override
    public int getItemCount() {
        return productList.size(); // Mỗi item là một sản phẩm
    }

    // Lớp ViewHolder để giữ các view trong item_san_pham
    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView tenSP, giaBan;

        public ProductViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ivHinhAnh1);
            tenSP = itemView.findViewById(R.id.tvTenSP1);
            giaBan = itemView.findViewById(R.id.tvGiaBan1);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(SanPham sanPham);
    }
}
