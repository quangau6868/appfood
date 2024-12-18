package com.app.appfood.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
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
import com.app.appfood.dao.loaispDao;
import com.app.appfood.model.LoaiSanPham;

import java.util.ArrayList;

public class LoaiSPadapter extends RecyclerView.Adapter<LoaiSPadapter.ViewHolder> {

    private Context context;
    private ArrayList<LoaiSanPham> list;
    private loaispDao loaiSPdao;

    public LoaiSPadapter(Context context, ArrayList<LoaiSanPham> list, loaispDao loaiSPdao) {
        this.context = context;
        this.list = list;
        this.loaiSPdao = loaiSPdao;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_loai_san_pham, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtMaLoai.setText("ID: " + list.get(position).getMaLoai());
        holder.txtTenLoai.setText("Tên Loại: " + list.get(position).getTenLoai());

        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogUpdate(list.get(holder.getAdapterPosition()));
            }
        });

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Hiển thị hộp thoại xác nhận xóa
                new AlertDialog.Builder(context)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc chắn muốn xóa loại sản phẩm này?")
                        .setPositiveButton("Có", (dialog, which) -> {
                            // Thực hiện xóa
                            boolean check = loaiSPdao.xoaLoaiSanPham(list.get(holder.getAdapterPosition()).getMaLoai());
                            if (check) {
                                Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                // Cập nhật lại danh sách
                                list.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());
                                notifyItemRangeChanged(holder.getAdapterPosition(), list.size());
                            } else {
                                Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Không", null)
                        .show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtMaLoai, txtTenLoai;
        ImageView ivEdit, ivDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtMaLoai = itemView.findViewById(R.id.txtMaLoai);
            txtTenLoai = itemView.findViewById(R.id.txtTenLoai);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            ivDelete = itemView.findViewById(R.id.ivDelete);
        }
    }

    private void showDialogUpdate(LoaiSanPham loaiSanPham) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_edit_loai_san_pham, null);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        TextView txtTieuDe = view.findViewById(R.id.tvTitle);
        TextView txtTenLoai = view.findViewById(R.id.etTenLoai);
        Button btnSave = view.findViewById(R.id.btnSave);
        Button btnHuy = view.findViewById(R.id.btnCancel);

        txtTieuDe.setText("Cập nhật thông tin");
        btnSave.setText("Cập nhật");
        txtTenLoai.setText(loaiSanPham.getTenLoai());

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenLoai = txtTenLoai.getText().toString();

                LoaiSanPham loaiSanPham1 = new LoaiSanPham(loaiSanPham.getMaLoai(), tenLoai);
                boolean check = loaiSPdao.suaLoaiSanPham(loaiSanPham1);
                if (check) {
                    Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    loadDATA();
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(context, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

    }

    private void loadDATA() {
        list.clear();
        list = loaiSPdao.getDSLoaiSP();
        notifyDataSetChanged();
    }

}


