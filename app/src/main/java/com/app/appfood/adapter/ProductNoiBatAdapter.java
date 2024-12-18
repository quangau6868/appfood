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

public class ProductNoiBatAdapter extends RecyclerView.Adapter<ProductNoiBatAdapter.ProductViewHolder> {

    private List<SanPham> productList;
    private OnItemClickListener listener;

    public ProductNoiBatAdapter(List<SanPham> productList, OnItemClickListener listener) {
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout item_hien_thi_san_pham_1 (mỗi item sẽ chứa 2 sản phẩm)
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hien_thi_san_pham_1, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        // Lấy 2 sản phẩm cho mỗi item
        SanPham sanPham1 = productList.get(position * 2); // Sản phẩm 1
        SanPham sanPham2 = (position * 2 + 1 < productList.size()) ? productList.get(position * 2 + 1) : null; // Sản phẩm 2 (nếu có)

        // Gán dữ liệu cho sản phẩm 1
        holder.tenSP1.setText(sanPham1.getTenSP());
        holder.giaBan1.setText(String.format("%,.0f VND", sanPham1.getGiaBan()));
        if (sanPham1.getHinhAnh() != null) {
            Bitmap bitmap1 = BitmapFactory.decodeByteArray(sanPham1.getHinhAnh(), 0, sanPham1.getHinhAnh().length);
            holder.imageView1.setImageBitmap(bitmap1);
        }

        // Thiết lập sự kiện click cho sản phẩm 1
        holder.imageView1.setOnClickListener(v -> listener.onItemClick(sanPham1));

        // Gán dữ liệu cho sản phẩm 2 (nếu có)
        if (sanPham2 != null) {
            holder.tenSP2.setText(sanPham2.getTenSP());
            holder.giaBan2.setText(String.format("%,.0f VND", sanPham2.getGiaBan()));
            if (sanPham2.getHinhAnh() != null) {
                Bitmap bitmap2 = BitmapFactory.decodeByteArray(sanPham2.getHinhAnh(), 0, sanPham2.getHinhAnh().length);
                holder.imageView2.setImageBitmap(bitmap2);
            }

            // Thiết lập sự kiện click cho sản phẩm 2
            holder.imageView2.setOnClickListener(v -> listener.onItemClick(sanPham2));

            // Hiển thị các view của sản phẩm 2
            holder.imageView2.setVisibility(View.VISIBLE);
            holder.tenSP2.setVisibility(View.VISIBLE);
            holder.giaBan2.setVisibility(View.VISIBLE);
        } else {
            // Nếu sản phẩm thứ 2 không có, ẩn các view liên quan đến sản phẩm 2
            holder.imageView2.setVisibility(View.GONE);
            holder.tenSP2.setVisibility(View.GONE);
            holder.giaBan2.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return (int) Math.ceil(productList.size() / 2.0); // Chia số sản phẩm cho 2 và làm tròn lên
    }

    // ViewHolder giữ các View của item_san_pham
    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView1, imageView2;
        TextView tenSP1, tenSP2, giaBan1, giaBan2;

        public ProductViewHolder(View itemView) {
            super(itemView);
            // Ánh xạ các view trong item_san_pham
            imageView1 = itemView.findViewById(R.id.ivHinhAnh1);
            tenSP1 = itemView.findViewById(R.id.tvTenSP1);
            giaBan1 = itemView.findViewById(R.id.tvGiaBan1);

            imageView2 = itemView.findViewById(R.id.ivHinhAnh2);
            tenSP2 = itemView.findViewById(R.id.tvTenSP2);
            giaBan2 = itemView.findViewById(R.id.tvGiaBan2);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(SanPham sanPham);
    }
}