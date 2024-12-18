package com.app.appfood.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.app.appfood.database.DBHelper;
import com.app.appfood.model.LoaiSanPham;

import java.util.ArrayList;

public class loaispDao {

    private DBHelper dbHelper;

    public loaispDao(Context context) {
        dbHelper = new DBHelper(context);
    }

    // Lấy danh sách loại sản phẩm
    public ArrayList<LoaiSanPham> getDSLoaiSP() {
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        ArrayList<LoaiSanPham> list = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM Loai_SP", null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                list.add(new LoaiSanPham(cursor.getInt(0), cursor.getString(1)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // them loai san pham
    public boolean themLoaiSanPham(LoaiSanPham loaiSanPham) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("TenLoai", loaiSanPham.getTenLoai());

        long check = sqLiteDatabase.insert("Loai_SP", null, contentValues);
        return check != -1;
    }

    // Sửa loại sản phẩm
    public boolean suaLoaiSanPham(LoaiSanPham loaiSanPham) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TenLoai", loaiSanPham.getTenLoai());

        int check = sqLiteDatabase.update("Loai_SP", contentValues, "MaLoai = ?", new String[]{String.valueOf(loaiSanPham.getMaLoai())});
        return check > 0; // Trả về true nếu cập nhật thành công
    }

    // xóa loại sản phẩm theo mã loại
    public boolean xoaLoaiSanPham(int maLoai) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        // Kiểm tra nếu có sản phẩm tham chiếu đến mã loại
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM SanPham WHERE MaLoai = ?", new String[]{String.valueOf(maLoai)});
        if (cursor.getCount() > 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        // Không có sản phẩm liên kết, tiến hành xóa
        return sqLiteDatabase.delete("Loai_SP", "MaLoai = ?", new String[]{String.valueOf(maLoai)}) > 0;
    }

}


