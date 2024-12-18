package com.app.appfood.admin;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.appfood.R;
import com.app.appfood.adapter.LoaiSPadapter;
import com.app.appfood.dao.loaispDao;
import com.app.appfood.model.LoaiSanPham;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class LoaiSPActivity extends AppCompatActivity {

    private loaispDao loaiSPdao;
    private RecyclerView recyclerViewSP;
    private ArrayList<LoaiSanPham> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_loai_spactivity);

        // thiết kế giao diên chinh + giao dien iteam
        recyclerViewSP = findViewById(R.id.recyclerViewSP);
        FloatingActionButton floatAdd = findViewById(R.id.floatAdd);

        //data
        loaiSPdao = new loaispDao(this);


        //adapter
        loadData();

        floatAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //hiển thị dialog
                showDiaLogThem();
            }
        });


    }

    private void loadData() {
        list = loaiSPdao.getDSLoaiSP();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewSP.setLayoutManager(linearLayoutManager);
        LoaiSPadapter loaiSPadapter = new LoaiSPadapter(this, list, loaiSPdao);
        recyclerViewSP.setAdapter(loaiSPadapter);
        loaiSPadapter.notifyDataSetChanged();
    }

    private void showDiaLogThem() {
        // Tạo dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_loai_san_pham, null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        EditText edtTenLoai = view.findViewById(R.id.etTenLoai);
        Button btnSave = view.findViewById(R.id.btnSave);
        Button btnHuy = view.findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenloai = edtTenLoai.getText().toString();

                if (tenloai.isEmpty()) {
                    Toast.makeText(LoaiSPActivity.this, "Tên loại sản phẩm không được để trống!", Toast.LENGTH_SHORT).show();
                    return;
                }

                LoaiSanPham loaiSanPham = new LoaiSanPham(tenloai);
                boolean check = loaiSPdao.themLoaiSanPham(loaiSanPham);

                if (check) {
                    Toast.makeText(LoaiSPActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                    loadData();
                } else {
                    Toast.makeText(LoaiSPActivity.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
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

}