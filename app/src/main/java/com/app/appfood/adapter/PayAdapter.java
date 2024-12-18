package com.app.appfood.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.appfood.R;
import com.app.appfood.activity.CommentActivity;
import com.app.appfood.activity.PayActivity;
import com.app.appfood.dao.ThanhToanDao;
import com.app.appfood.model.ThanhToan;

import java.util.List;

public class PayAdapter extends RecyclerView.Adapter<PayAdapter.PayViewHolder> {

    private Context context;
    private List<ThanhToan> thanhToanList;
    private ThanhToanDao thanhToanDao;
    private boolean isAdmin; // Thêm biến isAdmin để xác định vai trò

    // Constructor nhận thêm biến isAdmin
    public PayAdapter(Context context, List<ThanhToan> thanhToanList, boolean isAdmin) {
        this.context = context;
        this.thanhToanList = thanhToanList;
        this.thanhToanDao = new ThanhToanDao(context);
        this.isAdmin = isAdmin; // Gán giá trị isAdmin
    }

    @Override
    public PayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_thanh_toan, parent, false);
        return new PayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PayViewHolder holder, int position) {
        ThanhToan thanhToan = thanhToanList.get(position);

        // Gắn dữ liệu vào các view
        holder.tvTenNguoi.setText("Tên người: " + thanhToan.getTenNguoi());
        holder.tvSdt.setText("Số điện thoại: " + thanhToan.getSdt());
        holder.tvDiaChi.setText("Địa chỉ: " + thanhToan.getDiaChi());
        holder.tvSP.setText("Đồ ăn: " + thanhToan.getTenSP());
        holder.tvSL.setText("SL: " + thanhToan.getSoLuong());
        holder.tvTongTien.setText(String.format("Tổng tiền: %,d VND", (long) thanhToan.getTongTien()));
        Log.d("PayAdapter", "isAdmin in onBindViewHolder: " + isAdmin);

        // Hiển thị hoặc ẩn nút dựa trên vai trò
        if (isAdmin) {
            Log.d("PayAdapter", "Admin view being set");
            holder.btnXacNhanGiao.setVisibility(View.VISIBLE);
            holder.btnDaGiao.setVisibility(View.VISIBLE);
            holder.deleteIcon.setVisibility(View.VISIBLE);
            holder.comment.setVisibility(View.GONE);
            holder.tvTrangThai.setVisibility(View.GONE); // Hiển thị TextView trạng thái khi là admin

            // Khi nhấn nút xóa
            holder.deleteIcon.setOnClickListener(v -> {
                thanhToanDao.deleteThanhToan(thanhToan.getId()); // Xóa bản ghi trong cơ sở dữ liệu
                thanhToanList.remove(position); // Xóa đối tượng khỏi danh sách
                notifyItemRemoved(position); // Cập nhật lại giao diện
                notifyItemRangeChanged(position, thanhToanList.size()); // Cập nhật lại các item còn lại
                Toast.makeText(context, "Đã xóa thanh toán", Toast.LENGTH_SHORT).show(); // Thông báo cho người dùng
                Log.d("PayAdapter", "Delete clicked");
            });

            // Kiểm tra trạng thái hiện tại và ẩn các nút nếu cần
            if (thanhToan.getTrangThai() == 1) { // Trạng thái "Đang giao"
                holder.btnXacNhanGiao.setVisibility(View.GONE); // Ẩn nút "Xác nhận giao"
            } else if (thanhToan.getTrangThai() == 2) { // Trạng thái "Đã giao"
                holder.btnDaGiao.setVisibility(View.GONE);
                holder.btnXacNhanGiao.setVisibility(View.GONE);// Ẩn nút "Đã giao"
            } else {
                holder.btnXacNhanGiao.setVisibility(View.VISIBLE); // Hiển thị nút "Xác nhận giao"
                holder.btnDaGiao.setVisibility(View.VISIBLE); // Hiển thị nút "Đã giao"
            }

            // Khi bấm nút "Xác nhận giao" (trạng thái "Đang giao")
            holder.btnXacNhanGiao.setOnClickListener(v -> {
                thanhToanDao.updateTrangThai(thanhToan.getId(), "Đang giao"); // Cập nhật trạng thái thành "Đang giao" (1)
                thanhToan.setTrangThai(1); // Cập nhật trạng thái trong đối tượng thanhToan
                holder.btnXacNhanGiao.setEnabled(false); // Vô hiệu hóa nút "Xác nhận giao"
                holder.btnXacNhanGiao.setVisibility(View.GONE); // Ẩn nút "Xác nhận giao"
                notifyItemChanged(position); // Cập nhật lại giao diện
                Toast.makeText(context, "Đã xác nhận giao hàng", Toast.LENGTH_SHORT).show(); // Thông báo cho người dùng
                Log.d("PayAdapter", "Xác nhận giao clicked");
            });

            // Khi bấm nút "Đã giao" (trạng thái "Đã giao")
            holder.btnDaGiao.setOnClickListener(v -> {
                thanhToanDao.updateTrangThai(thanhToan.getId(), "Đã giao"); // Cập nhật trạng thái thành "Đã giao" (2)
                thanhToan.setTrangThai(2); // Cập nhật trạng thái trong đối tượng thanhToan
                holder.btnDaGiao.setEnabled(false); // Vô hiệu hóa nút "Đã giao"
                holder.btnDaGiao.setVisibility(View.GONE); // Ẩn nút "Đã giao"
                notifyItemChanged(position); // Cập nhật lại giao diện
                Toast.makeText(context, "Đã giao hàng", Toast.LENGTH_SHORT).show(); // Thông báo cho người dùng
                Log.d("PayAdapter", "Đã giao clicked");
            });

        } else {
            Log.d("PayAdapter", "User view being set");
            holder.btnXacNhanGiao.setVisibility(View.GONE);
            holder.btnDaGiao.setVisibility(View.GONE);
            holder.deleteIcon.setVisibility(View.GONE);
            holder.comment.setVisibility(View.GONE);
            holder.tvTrangThai.setVisibility(View.VISIBLE); // Hiển thị TextView trạng thái khi là user

            // Trong onBindViewHolder của PayAdapter
            holder.comment.setOnClickListener(v -> {
                // Lấy thông tin từ đối tượng ThanhToan
                String tenNguoi = thanhToan.getTenNguoi();
                double tongTien = thanhToan.getTongTien();
                String tenSP = thanhToan.getTenSP();  // Lấy tên sản phẩm
                int maSP = thanhToan.getMaSP();      // Lấy mã sản phẩm
                int thanhToanId = thanhToan.getId(); // Lấy thanhToanId

                // Tạo Intent để mở CommentActivity
                Intent intent = new Intent(context, CommentActivity.class);

                // Truyền dữ liệu vào Intent
                intent.putExtra("tenNguoi", tenNguoi);
                intent.putExtra("tongTien", tongTien);
                intent.putExtra("tenSP", tenSP);  // Truyền tên sản phẩm
                intent.putExtra("maSP", maSP);    // Truyền mã sản phẩm
                intent.putExtra("thanhToanId", thanhToanId); // Truyền thanhToanId

                // Bắt đầu CommentActivity
                context.startActivity(intent);
            });


            // Thay đổi thông báo trạng thái dựa trên trạng thái thanh toán
            if (thanhToan.getTrangThai() == 1) {
                holder.tvTrangThai.setText("Đang giao");
                holder.comment.setVisibility(View.GONE); // Không hiển thị nút comment khi đang giao
            } else if (thanhToan.getTrangThai() == 2) {
                holder.tvTrangThai.setText("Đã giao");
                holder.comment.setVisibility(View.VISIBLE); // Hiển thị nút comment khi đã giao
            } else {
                holder.tvTrangThai.setText("Chưa xác nhận");
                holder.comment.setVisibility(View.GONE); // Không hiển thị nút comment khi trạng thái chưa xác nhận
            }

        }
    }

        @Override
    public int getItemCount() {
        return thanhToanList.size();
    }

    public static class PayViewHolder extends RecyclerView.ViewHolder {

        TextView tvTenNguoi, tvSdt, tvDiaChi, tvTongTien, tvTrangThai,tvSP,tvSL;
        ImageView deleteIcon,comment;
        Button btnXacNhanGiao, btnDaGiao;

        public PayViewHolder(View itemView) {
            super(itemView);
            tvSL = itemView.findViewById(R.id.tvSL);
            tvSP = itemView.findViewById(R.id.tvSP);
            tvTenNguoi = itemView.findViewById(R.id.tvTenNguoi);
            tvSdt = itemView.findViewById(R.id.tvSdt);
            tvDiaChi = itemView.findViewById(R.id.tvDiaChi);
            tvTongTien = itemView.findViewById(R.id.tvTongTien);
            tvTrangThai = itemView.findViewById(R.id.tvTrangThai);
            deleteIcon = itemView.findViewById(R.id.delete_iconn);
            comment = itemView.findViewById(R.id.comment);
            btnXacNhanGiao = itemView.findViewById(R.id.btnXacNhanGiao); // Nút "Xác nhận giao"
            btnDaGiao = itemView.findViewById(R.id.btnDaGiao); // Nút "Đã giao"
        }
    }

    // Hàm cập nhật danh sách thanh toán
    public void updateThanhToan(List<ThanhToan> newThanhToans) {
        this.thanhToanList = newThanhToans;
        notifyDataSetChanged();
    }
}
