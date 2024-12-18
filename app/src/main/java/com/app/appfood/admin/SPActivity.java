package com.app.appfood.admin;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.appfood.R;
import com.app.appfood.adapter.SPAdapter;
import com.app.appfood.dao.sanPhamDao;
import com.app.appfood.model.SanPham;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class SPActivity extends AppCompatActivity {

    private sanPhamDao spdao;
    private RecyclerView recyclerViewSP;
    private ArrayList<SanPham> list;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imgHinhAnh;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spactivity);

        recyclerViewSP = findViewById(R.id.recyclerViewSP1);
        FloatingActionButton floatAdd = findViewById(R.id.floatAdd1);

        spdao = new sanPhamDao(this);
        loadData();

        floatAdd.setOnClickListener(view -> showDiaLogThem());
    }

    private void loadData() {
        list = spdao.getDanhsachSanPham();
        recyclerViewSP.setLayoutManager(new LinearLayoutManager(this));
        SPAdapter adapter = new SPAdapter(this, list, spdao);
        recyclerViewSP.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void showDiaLogThem() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_edit_san_pham_1); // layout đã định nghĩa
        dialog.setCancelable(false);

        imgHinhAnh = dialog.findViewById(R.id.imgHinhAnh);
        EditText edtTenSP = dialog.findViewById(R.id.etTenSP);
        EditText edtMaLoai = dialog.findViewById(R.id.etMaLoai);
        EditText edtGiaBan = dialog.findViewById(R.id.etGiaBan);
        EditText edtMoTa = dialog.findViewById(R.id.etMoTa);
        CheckBox chkConHang = dialog.findViewById(R.id.cbConHang);
        CheckBox chkHetHang = dialog.findViewById(R.id.cbHetHang);

        imgHinhAnh.setOnClickListener(v -> openGallery());

        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        Button btnSave = dialog.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> {
            String tenSP = edtTenSP.getText().toString().trim();
            String maLoaiStr = edtMaLoai.getText().toString().trim();
            String giaBanStr = edtGiaBan.getText().toString().trim();
            String moTa = edtMoTa.getText().toString().trim();

            if (maLoaiStr.isEmpty() || giaBanStr.isEmpty()) {
                Toast.makeText(SPActivity.this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra trạng thái của CheckBox
            boolean isConHang = chkConHang.isChecked();
            boolean isHetHang = chkHetHang.isChecked();

            if (!isConHang && !isHetHang) {
                Toast.makeText(SPActivity.this, "Vui lòng chọn trạng thái hàng!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isConHang && isHetHang) {
                Toast.makeText(SPActivity.this, "Chỉ được chọn một trạng thái!", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int maLoai = Integer.parseInt(maLoaiStr);
                double giaBan = Double.parseDouble(giaBanStr);

                if (!spdao.isMaLoaiValid(maLoai)) {
                    Toast.makeText(SPActivity.this, "Mã loại không hợp lệ!", Toast.LENGTH_SHORT).show();
                    return;
                }

                byte[] hinhAnhByteArray = imageToByteArray(imgHinhAnh);

                // Thêm sản phẩm với trạng thái
                SanPham sanPham = new SanPham(maLoai, tenSP, giaBan, moTa, hinhAnhByteArray, isConHang);
                spdao.addSanPham(sanPham);

                loadData();
                Toast.makeText(SPActivity.this, "Sản phẩm đã được thêm!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            } catch (IllegalArgumentException e) {
                Toast.makeText(SPActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }


    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imgHinhAnh.setImageURI(data.getData());
            Toast.makeText(this, "Ảnh đã được chọn!", Toast.LENGTH_SHORT).show();
        }
    }

    private byte[] imageToByteArray(ImageView imageView) {
        if (imageView.getDrawable() == null) {
            return null;
        }
        Bitmap bitmap = ((android.graphics.drawable.BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}
