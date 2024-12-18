package com.app.appfood.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.appfood.dao.sanPhamDao;
import com.app.appfood.R;
import com.app.appfood.model.SanPham;

import java.util.List;

public class SPAdapter extends RecyclerView.Adapter<SPAdapter.SanPhamViewHolder> {
    private Context context;
    private List<SanPham> sanPhamList;
    private sanPhamDao sanPhamDao;

    public SPAdapter(Context context, List<SanPham> sanPhamList, sanPhamDao sanPhamDao) {
        this.context = context;
        this.sanPhamList = sanPhamList;
        this.sanPhamDao = sanPhamDao;
    }

    @NonNull
    @Override
    public SanPhamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_san_pham, parent, false);
        return new SanPhamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SanPhamViewHolder holder, int position) {
        SanPham sanPham = sanPhamList.get(position);

        // Gán dữ liệu vào các TextView
        holder.txtMaSP.setText("Mã SP: " + sanPham.getMaSP());
        holder.txtMaLoai.setText("Mã Loại: " + sanPham.getMaLoai());
        holder.txtTenSP.setText("Tên sản phẩm: " + sanPham.getTenSP());
        holder.txtGiaBan.setText("Giá bán: " + sanPham.getGiaBan() + " VND");
        // Kiểm tra nếu sản phẩm còn hàng (true) hoặc hết hàng (false)
        boolean isConHang = sanPham.isConHang();
        String tinhTrang = isConHang ? "Còn hàng" : "Hết hàng";
        holder.txtTinhtrang.setText("Tình trạng sản phẩm: " + tinhTrang);

        holder.txtMoTa.setText("Mô tả: " + sanPham.getMoTa());
        // Chuyển byte[] thành Bitmap
        byte[] hinhAnhBytes = sanPham.getHinhAnh();
        if (hinhAnhBytes != null) {
            Bitmap hinhAnhBitmap = BitmapFactory.decodeByteArray(hinhAnhBytes, 0, hinhAnhBytes.length);
            holder.imgHinhAnh.setImageBitmap(hinhAnhBitmap);
        } else {
            // Nếu không có hình ảnh, có thể đặt một hình ảnh mặc định
            holder.imgHinhAnh.setImageResource(R.drawable.icon_forder);  // Đổi default_image bằng hình ảnh mặc định của bạn
        }


        // Xử lý sự kiện click cho nút Edit
        holder.ivEdit.setOnClickListener(v -> {
            // Gọi hàm hiển thị dialog chỉnh sửa sản phẩm
            showEditDialog(sanPham, position);
        });

        // Xử lý sự kiện click cho nút Delete
        holder.ivDelete.setOnClickListener(v -> {
            // Hiển thị dialog xác nhận xóa
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Xóa sản phẩm")
                    .setMessage("Bạn có chắc chắn muốn xóa sản phẩm này không?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        boolean isDeleted = sanPhamDao.deleteSanPham(sanPham.getMaSP());
                        if (isDeleted) {
                            sanPhamList.remove(position);
                            notifyItemRemoved(position);
                            Toast.makeText(context, "Xóa sản phẩm thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Xóa sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    private void showEditDialog(SanPham sanPham, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_edit_san_pham, null);
        builder.setView(dialogView);

        // Khai báo các EditText và CheckBox
        EditText edtMaLoai = dialogView.findViewById(R.id.etMaLoai);
        EditText edtTenSP = dialogView.findViewById(R.id.etTenSP);
        EditText edtGiaBan = dialogView.findViewById(R.id.etGiaBan);
        EditText edtMoTa = dialogView.findViewById(R.id.etMoTa);
        CheckBox cbConHang = dialogView.findViewById(R.id.cbConHang);
        CheckBox cbHetHang = dialogView.findViewById(R.id.cbHetHang);

        // Hiển thị thông tin sản phẩm hiện tại vào các EditText
        edtMaLoai.setText(String.valueOf(sanPham.getMaLoai()));
        edtTenSP.setText(sanPham.getTenSP());
        edtGiaBan.setText(String.valueOf(sanPham.getGiaBan()));
        edtMoTa.setText(sanPham.getMoTa());

        // Cập nhật trạng thái của CheckBox
        if (sanPham.isConHang()) {
            cbConHang.setChecked(true);
            cbHetHang.setChecked(false);  // Nếu còn hàng, hủy chọn hết hàng
        } else {
            cbConHang.setChecked(false);
            cbHetHang.setChecked(true);  // Nếu hết hàng, hủy chọn còn hàng
        }

        builder.setTitle("Chỉnh sửa sản phẩm")
                .setPositiveButton("Lưu", (dialog, which) -> {
                    // Cập nhật dữ liệu sản phẩm từ EditText
                    String maLoaiString = edtMaLoai.getText().toString();
                    if (!maLoaiString.isEmpty()) {
                        try {
                            int maLoai = Integer.parseInt(maLoaiString); // Chuyển đổi từ String sang int
                            sanPham.setMaLoai(maLoai);  // Cập nhật Mã Loại
                        } catch (NumberFormatException e) {
                            Toast.makeText(context, "Mã Loại không hợp lệ", Toast.LENGTH_SHORT).show();
                            return; // Nếu giá trị không phải là số, không tiếp tục thực hiện
                        }
                    } else {
                        Toast.makeText(context, "Mã Loại không thể để trống", Toast.LENGTH_SHORT).show();
                        return; // Nếu mã loại trống, không thực hiện tiếp
                    }
                    sanPham.setTenSP(edtTenSP.getText().toString());    // Cập nhật Tên Sản Phẩm
                    sanPham.setGiaBan(Double.parseDouble(edtGiaBan.getText().toString()));  // Cập nhật Giá Bán
                    sanPham.setMoTa(edtMoTa.getText().toString());     // Cập nhật Mô Tả

                    // Cập nhật trạng thái Còn Hàng / Hết Hàng
                    sanPham.setConHang(cbConHang.isChecked());  // Nếu cbConHang được chọn, thì set isConHang là true, ngược lại false

                    // Cập nhật sản phẩm trong cơ sở dữ liệu
                    boolean isUpdated = sanPhamDao.updateSanPham(sanPham);
                    if (isUpdated) {
                        notifyItemChanged(position);
                        Toast.makeText(context, "Cập nhật sản phẩm thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Cập nhật sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public int getItemCount() {
        return sanPhamList.size();
    }

    public static class SanPhamViewHolder extends RecyclerView.ViewHolder {
        TextView txtMaSP, txtMaLoai, txtTenSP, txtGiaBan, txtMoTa, txtTinhtrang;
        ImageView imgHinhAnh, ivEdit, ivDelete;

        public SanPhamViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMaSP = itemView.findViewById(R.id.txtMaSP);
            txtMaLoai = itemView.findViewById(R.id.txtMaLoai);
            txtTenSP = itemView.findViewById(R.id.txtTenSP);
            txtGiaBan = itemView.findViewById(R.id.txtGiaBan);
            txtMoTa = itemView.findViewById(R.id.txtMoTa);
            txtTinhtrang = itemView.findViewById(R.id.txtTinhtrang);
            imgHinhAnh = itemView.findViewById(R.id.imgHinhAnh);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            ivDelete = itemView.findViewById(R.id.ivDelete);
        }
    }
}

