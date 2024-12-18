package com.app.appfood.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.appfood.R;
import com.app.appfood.dao.CartDao;
import com.app.appfood.fragment.GiohangFragment;
import com.app.appfood.model.CartItem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartList;  // Danh sách sản phẩm trong giỏ hàng
    private CartDao cartDao;
    private GiohangFragment giohangFragment; // Tham chiếu đến GiohangFragment để cập nhật giỏ hàng khi có thay đổi

    // Constructor nhận cả danh sách cartList, CartDao và GiohangFragment
    public CartAdapter(List<CartItem> cartList, CartDao cartDao, GiohangFragment giohangFragment) {
        this.cartList = cartList;
        this.cartDao = cartDao;
        this.giohangFragment = giohangFragment;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem product = cartList.get(position);
        holder.productName.setText(product.getTenSanPham());
        holder.productQuantity.setText(" " + product.getSoLuong());
        holder.productPrice.setText(String.format("%,.0f VND", product.getGia()));

        byte[] imageBytes = product.getHinhAnh();
        if (imageBytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            holder.productImage.setImageBitmap(bitmap);
        }

        // Lấy số lượng tối đa từ CartItem
        int maxQuantity = 10;

        // Xử lý sự kiện giảm số lượng
        holder.decreaseQuantity.setOnClickListener(v -> {
            int currentQuantity = product.getSoLuong();
            if (currentQuantity > 1) {  // Đảm bảo số lượng không giảm xuống dưới 1
                product.setSoLuong(currentQuantity - 1);  // Cập nhật số lượng giảm đi 1
                cartDao.capNhatSoLuongSanPham(product);  // Cập nhật trong cơ sở dữ liệu
                holder.productQuantity.setText(String.valueOf(product.getSoLuong()));  // Cập nhật lại TextView hiển thị số lượng
                notifyItemChanged(holder.getAdapterPosition());  // Cập nhật lại RecyclerView
                giohangFragment.updateTotalAmount();  // Cập nhật lại tổng tiền
            }
        });

        // Xử lý sự kiện tăng số lượng
        holder.increaseQuantity.setOnClickListener(v -> {
            int currentQuantity = product.getSoLuong();
            // Kiểm tra số lượng tối đa đã được lấy từ cơ sở dữ liệu và đảm bảo không vượt quá số lượng tối đa
            if (currentQuantity < maxQuantity) {  // Kiểm tra nếu số lượng chưa vượt quá maxQuantity
                product.setSoLuong(currentQuantity + 1);  // Cập nhật số lượng tăng lên 1
                cartDao.capNhatSoLuongSanPham(product);  // Cập nhật trong cơ sở dữ liệu
                holder.productQuantity.setText(String.valueOf(product.getSoLuong()));  // Cập nhật lại TextView hiển thị số lượng
                notifyItemChanged(holder.getAdapterPosition());  // Cập nhật lại RecyclerView
                giohangFragment.updateTotalAmount();  // Cập nhật lại tổng tiền
            } else {
                // Hiển thị thông báo khi số lượng đã đạt tối đa
                Toast.makeText(holder.itemView.getContext(), "Số lượng tối đa là 4!", Toast.LENGTH_SHORT).show();
            }
        });


        // Xử lý sự kiện xóa sản phẩm
        holder.deleteIcon.setOnClickListener(v -> {
            int productId = product.getId();
            boolean isDeleted = cartDao.xoaSanPhamKhoiGioHang(productId);

            if (isDeleted) {
                // Xóa sản phẩm khỏi danh sách giỏ hàng
                cartList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                notifyItemRangeChanged(holder.getAdapterPosition(), cartList.size());

                // Kiểm tra nếu giỏ hàng trống sau khi xóa
                if (cartList.isEmpty()) {
                    if (giohangFragment != null) {
                        giohangFragment.updateCart(cartList);  // Truyền danh sách trống để cập nhật giao diện
                    }
                } else {
                    // Nếu giỏ hàng vẫn còn sản phẩm, chỉ cần cập nhật tổng tiền
                    if (giohangFragment != null) {
                        giohangFragment.updateTotalAmount();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage, decreaseQuantity, increaseQuantity, deleteIcon;
        TextView productName, productQuantity, productPrice;

        public CartViewHolder(View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productQuantity = itemView.findViewById(R.id.product_quantity);
            productPrice = itemView.findViewById(R.id.product_price);
            decreaseQuantity = itemView.findViewById(R.id.decrease_quantity);
            increaseQuantity = itemView.findViewById(R.id.increase_quantity);
            deleteIcon = itemView.findViewById(R.id.delete_icon);
        }
    }

    // Cập nhật giỏ hàng từ bên ngoài (GiohangFragment)
    public void updateCart(List<CartItem> newCartItems) {
        this.cartList = newCartItems;
        notifyDataSetChanged();  // Cập nhật lại RecyclerView
    }

    // Tính tổng tiền giỏ hàng
    public double getTotalPrice() {
        double total = 0;
        for (CartItem product : cartList) {
            total += product.getGia() * product.getSoLuong();
        }
        return total;
    }


}


